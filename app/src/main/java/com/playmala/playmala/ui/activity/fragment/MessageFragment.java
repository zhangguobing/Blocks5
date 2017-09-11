package com.playmala.playmala.ui.activity.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.playmala.playmala.AppCookie;
import com.playmala.playmala.Constants;
import com.playmala.playmala.R;
import com.playmala.playmala.api.ResponseError;
import com.playmala.playmala.base.BaseController;
import com.playmala.playmala.base.BasePresenterFragment;
import com.playmala.playmala.base.ContentView;
import com.playmala.playmala.controller.ActivityController;
import com.playmala.playmala.model.Activity;
import com.playmala.playmala.model.Comment;
import com.playmala.playmala.model.event.ActivityMessageFilterEvent;
import com.playmala.playmala.model.event.ActivityNoticeUpdateEvent;
import com.playmala.playmala.ui.activity.ActivityMessageActivity;
import com.playmala.playmala.ui.activity.adapter.CommentAdapter;
import com.playmala.playmala.ui.user.UserDetailActivity;
import com.playmala.playmala.util.EventUtil;
import com.playmala.playmala.util.KeyboardChangeListener;
import com.playmala.playmala.util.TimeUtil;
import com.playmala.playmala.util.ToastUtil;
import com.playmala.playmala.widget.MessageHeadView;
import com.playmala.playmala.widget.MultiStateView;
import com.playmala.playmala.widget.topsheet.TopSheetBehavior;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.NormalDialog;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.squareup.otto.Subscribe;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * author：zhangguobing on 2017/7/20 15:48
 * email：bing901222@qq.com
 * 活动留言
 */
@ContentView(R.layout.fragment_message)
public class MessageFragment extends BasePresenterFragment<ActivityController.ActivityUiCallbacks>
    implements View.OnClickListener,ActivityController.CommentUi,View.OnLongClickListener{

    private static final String EXTRA_IS_TEAM = "is_team";
    private static final String EXTRA_ACTIVITY = "activity";

    @Bind(R.id.multi_state_view)
    public MultiStateView mMultiStateView;
    @Bind(R.id.refresh_layout)
    public TwinklingRefreshLayout mRefreshLayout;
    @Bind(R.id.recycler_view)
    public RecyclerView mRecyclerView;
    @Bind(R.id.btn_send)
    Button mSendBtn;
    @Bind(R.id.et_content)
    EditText mContentEditText;
    @Bind(R.id.iv_down)
    ImageView mDownImg;
    @Bind(R.id.layout_notice_board)
    View mNoticeBoardLayout;
    @Bind(R.id.tv_notice_board_title)
    TextView mNoticeBoardTitleTv;
    @Bind(R.id.tv_notice_content)
    TextView mNoticeContentTv;
    @Bind(R.id.tv_notice_time)
    TextView mNoticeTimeTv;
    @Bind(R.id.tv_report)
    TextView mReportTv;

    private CommentAdapter mAdapter;

    //是否能下拉加载更多
    private boolean isEnablePullRefresh = false;
    private boolean isSending = false;
    private Comment mCurSendingComment;
    private int mReadySendPosition;
    //是否页面已经加载完毕
    private boolean isPageLoadFinish = false;

    private Activity activity;
    private String is_team;

    //公告栏是否是展开状态
    private boolean isNoticeBoardExpand = false;

    //默认全部留言
    private String is_activity_creator = "0";

    //是否是过滤留言列表
    private boolean isFilter;

    private TopSheetBehavior mBehavior;

    private int mPage = 1;

    public static MessageFragment newInstance(String is_team, Activity activity) {
        Bundle args = new Bundle();
        MessageFragment fragment = new MessageFragment();
        args.putString(EXTRA_IS_TEAM,is_team);
        args.putParcelable(EXTRA_ACTIVITY, activity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        is_team = getArguments().getString(EXTRA_IS_TEAM);
        activity = getArguments().getParcelable(EXTRA_ACTIVITY);

        MessageHeadView headerView = new MessageHeadView(getContext());
        mRefreshLayout.setHeaderView(headerView);

        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                if(isEnablePullRefresh){
                    load();
                }
            }
        });

        mAdapter = new CommentAdapter(this,this);
        //去掉动画，解决局部刷新时闪烁问题
        ((SimpleItemAnimator)mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        mSendBtn.setOnClickListener(this);
        mDownImg.setOnClickListener(this);

        if("0".equals(is_team)){
            mNoticeBoardTitleTv.setText("游客公告栏");
            mNoticeContentTv.setText(activity.getGuest_notice());
            if(!TextUtils.isEmpty(activity.getGuest_notice())){
                mNoticeTimeTv.setText(activity.getGuest_notice_time());
            }
        }else{
            mNoticeBoardTitleTv.setText("团队公告栏");
            mNoticeContentTv.setText(activity.getTeam_notice());
            if(!TextUtils.isEmpty(activity.getTeam_notice())){
                mNoticeTimeTv.setText(activity.getTeam_notice_time());
            }
        }

        mReportTv.setVisibility(AppCookie.getUserInfo().getId() == activity.getCreator().getId() ? View.GONE : View.VISIBLE);

        mBehavior = TopSheetBehavior.from(mNoticeBoardLayout);
        mBehavior.setTopSheetCallback(new TopSheetBehavior.TopSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, @TopSheetBehavior.State int newState) {
                if(newState == TopSheetBehavior.STATE_COLLAPSED){
                    isNoticeBoardExpand = false;
                    ObjectAnimator.ofFloat(mDownImg,View.ROTATION.getName(),-180,0).start();
                }else if(newState == TopSheetBehavior.STATE_EXPANDED){
                    isNoticeBoardExpand = true;
                    ObjectAnimator.ofFloat(mDownImg,View.ROTATION.getName(),180).start();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        mContentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mSendBtn.setEnabled(s.toString().trim().length() > 0);
            }
        });

        new KeyboardChangeListener(getActivity()).setKeyBoardListener((isShow, keyboardHeight) -> {
            if(isShow){
                if(mAdapter.getItemCount() > 0){
                    recycleViewScrollToBottom();
                }
            }
        });

        load();
    }

    /**
     * 打开或者关闭活动公告栏
     */
    public void toggleNoticeBoard() {
        if(isNoticeBoardExpand){
            mBehavior.setState(TopSheetBehavior.STATE_COLLAPSED);
        }else{
            mBehavior.setState(TopSheetBehavior.STATE_EXPANDED);
        }
        isNoticeBoardExpand = !isNoticeBoardExpand;
    }


    private void recycleViewScrollToBottom(){
        if(mAdapter.getItemCount() > 0){
            mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
        }
    }

    private void load(){
        Map<String,String> params = new HashMap<>();
        params.put("token", AppCookie.getToken());
        params.put("is_team",is_team);
        params.put("is_activity_creator",is_activity_creator);
        params.put("page_index", mPage + "");
        params.put("page_size","15");
        getCallbacks().getComments(String.valueOf(activity.getId()),params);
    }

    @Override
    protected BaseController getPresenter() {
        return new ActivityController();
    }

    @Override
    public void onResponseError(ResponseError error) {
        mMultiStateView.setPtrRefreshComplete();
        if(mAdapter.getItemCount() == 0){
            mMultiStateView.setState(MultiStateView.STATE_EMPTY)
                    .setTitle(error.getMessage())
                    .setPtrHandler(new PtrDefaultHandler() {
                        @Override
                        public void onRefreshBegin(PtrFrameLayout frame) {
                            load();
                        }
                    });
        }
        if(error.getCode() == 5){
            ((ActivityMessageActivity)getActivity()).handleNotTeamException(error.getMessage());
        }
    }

    @OnClick({R.id.tv_report})
    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.image_photo:
               final Object posOb = v.getTag(R.id.tag_click_content);
               if (posOb != null) {
                   int pos = (int) posOb;
                   Comment comment = mAdapter.getItem(pos);
                   UserDetailActivity.create(getContext(),comment.getUser_id());
               }
               break;
           case R.id.btn_send:
               sendTextMessage();
               break;
           case R.id.iv_down:
               toggleNoticeBoard();
               break;
           case R.id.icon_progress_failed:
               final NormalDialog dialog = new NormalDialog(getContext());
               int color = ContextCompat.getColor(getContext(),R.color.primary_text);
               dialog.setCanceledOnTouchOutside(false);
               dialog.isTitleShow(false)
                       .cornerRadius(5)
                       .content("重新发送?")
                       .contentGravity(Gravity.CENTER)
                       .contentTextColor(color)
                       .dividerColor(R.color.divider)
                       .btnTextSize(15.5f, 15.5f)
                       .btnTextColor(color,color)
                       .widthScale(0.75f)
                       .show();
               dialog.setOnBtnClickL(dialog::dismiss, () -> {
                   dialog.dismiss();
                   int position = (int) v.getTag(R.id.tag_click_content);
                   mReadySendPosition = position;
                   mCurSendingComment = mAdapter.getItem(position);
                   mCurSendingComment.setSend_state(Constants.SendState.SENDING);
                   mAdapter.setItem(position,mCurSendingComment);
                   recycleViewScrollToBottom();
                   getCallbacks().addComment(String.valueOf(activity.getId()),mCurSendingComment.getContent(),Integer.parseInt(is_team));
               });
               break;
           case R.id.tv_report:
               toggleNoticeBoard();
               showSelectReportContentDialog();
               break;
       }
    }

    private void showSelectReportContentDialog() {
        final String[] contentOptions = {"泄露隐私", "人身攻击", "淫秽色情", "垃圾广告", "敏感信息"};
        final ActionSheetDialog dialog = new ActionSheetDialog(getContext(), contentOptions, null);
        dialog.isTitleShow(false).show();
        dialog.setOnOperItemClickL((parent, view, position, id) -> {
            dialog.dismiss();
            String content = contentOptions[position];
            showLoading(R.string.label_being_something);
            getCallbacks().report(0,activity.getId(),content);
        });
    }

    private void sendTextMessage(){
        if(!isPageLoadFinish) return;
        if(isSending) return;
        isSending = true;
        String text = mContentEditText.getText().toString().trim();
        if(TextUtils.isEmpty(text)) return;
        mContentEditText.setText("");

        mCurSendingComment = null;
        mCurSendingComment = new Comment();
        mCurSendingComment.setUser_id(AppCookie.getUserInfo().getId());
        mCurSendingComment.setContent(text);
        mCurSendingComment.setSend_state(Constants.SendState.SENDING);
        mCurSendingComment.setCreated_at(TimeUtil.getDate());

        Comment.CreatorBean creatorBean = new Comment.CreatorBean();
        creatorBean.setAvatar(AppCookie.getUserInfo().getAvatar());
        creatorBean.setId(AppCookie.getUserInfo().getId());
        creatorBean.setNick_name(AppCookie.getUserInfo().getNick_name());

        mCurSendingComment.setCreator(creatorBean);

        mAdapter.addItem(mCurSendingComment);
        recycleViewScrollToBottom();

        mReadySendPosition = mAdapter.getItemCount() > 0 ? mAdapter.getItemCount()-1 : 0;

        getCallbacks().addComment(String.valueOf(activity.getId()),text,Integer.parseInt(is_team));
    }

    @Override
    public void getCommentSuccess(List<Comment> comments) {
        if(isDetached()) return;
        mMultiStateView.setPtrRefreshComplete();
        mRefreshLayout.finishRefreshing();
        if (comments != null && !comments.isEmpty()) {
            Collections.reverse(comments);
            if(isEnablePullRefresh && !isFilter){
                mAdapter.addItems(0,comments);
                mRecyclerView.scrollToPosition(comments.size() - 1);
            }else{
                mMultiStateView.setState(MultiStateView.STATE_CONTENT);
                mAdapter.setItems(comments);
                mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
                isEnablePullRefresh = true;
                isPageLoadFinish = true;
            }
            if(comments.size() < 15){
                mRefreshLayout.setEnableRefresh(false);
            }
            mPage++;
        }else{
            mRefreshLayout.setEnableRefresh(false);
            if(isFilter || mAdapter.getItemCount() == 0){
                isPageLoadFinish = true;
                mMultiStateView.setState(MultiStateView.STATE_EMPTY)
                        .setTitle("暂无留言")
                        .setPtrHandler(new PtrDefaultHandler() {
                            @Override
                            public void onRefreshBegin(PtrFrameLayout frame) {
                                load();
                            }
                        });
            }else{
                ToastUtil.showText("没有更多了");
            }
        }
        if(isFilter){
            cancelLoading();
            isFilter = false;
        }
    }

    @Override
    public void addCommentSuccess(Comment comment) {
        mCurSendingComment.setSend_state(Constants.SendState.SUCCESS);
        mAdapter.setItem(mReadySendPosition,mCurSendingComment);
        isSending = false;
        if(mMultiStateView.getState() == MultiStateView.STATE_EMPTY){
            mMultiStateView.setState(MultiStateView.STATE_CONTENT);
        }
    }

    @Override
    public void addCommentFail(String msg) {
        mCurSendingComment.setSend_state(Constants.SendState.FAILED);
        mAdapter.setItem(mReadySendPosition,mCurSendingComment);
        ToastUtil.showText(msg);
        isSending = false;
    }

    @Override
    public void reportSuccess() {
        cancelLoading();
        ToastUtil.showText("已成功举报");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventUtil.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventUtil.unregister(this);
    }

    @Subscribe
    public void onCommentsFilter(ActivityMessageFilterEvent event){
        if(getUserVisibleHint() && isVisible()){
            showLoading(R.string.label_being_loading);
            is_activity_creator = event.is_activity_creator ? "1" : "0";
            isFilter = true;
            load();
        }
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()){
            case R.id.text_content:
                final Object posOb = view.getTag(R.id.tag_click_content);
                if (posOb != null) {
                    int pos = (int) posOb;
                    Comment comment = mAdapter.getItem(pos);
                    if(AppCookie.getUserInfo().getId() != comment.getCreator().getId()){
                        final String[] contentOptions = {"泄露隐私", "人身攻击", "淫秽色情", "垃圾广告", "敏感信息"};
                        final ActionSheetDialog dialog = new ActionSheetDialog(getContext(), contentOptions, null);
                        dialog.isTitleShow(false).show();
                        dialog.setOnOperItemClickL((parent, v, position, id) -> {
                            dialog.dismiss();
                            String content = contentOptions[position];
                            showLoading(R.string.label_being_something);
                            getCallbacks().report(1, comment.getId(), content);
                        });
                    }
                }
                break;
        }
        return true;
    }

    @Subscribe
    public void onNoticeContentUpdate(ActivityNoticeUpdateEvent event){
        if(is_team.equals(event.notice_type)){
            mNoticeContentTv.setText(event.notice_content);
        }
    }
}
