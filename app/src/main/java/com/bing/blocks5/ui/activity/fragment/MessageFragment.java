package com.bing.blocks5.ui.activity.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bing.blocks5.AppCookie;
import com.bing.blocks5.R;
import com.bing.blocks5.api.ResponseError;
import com.bing.blocks5.base.BaseController;
import com.bing.blocks5.base.BasePresenterFragment;
import com.bing.blocks5.base.ContentView;
import com.bing.blocks5.controller.ActivityController;
import com.bing.blocks5.model.Comment;
import com.bing.blocks5.ui.activity.adapter.CommentAdapter;
import com.bing.blocks5.ui.user.UserDetailActivity;
import com.bing.blocks5.util.KeyboardChangeListener;
import com.bing.blocks5.util.ToastUtil;
import com.bing.blocks5.widget.MessageHeadView;
import com.bing.blocks5.widget.MultiStateView;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * author：zhangguobing on 2017/7/20 15:48
 * email：bing901222@qq.com
 * 活动留言
 */
@ContentView(R.layout.fragment_message)
public class MessageFragment extends BasePresenterFragment<ActivityController.ActivityUiCallbacks>
    implements View.OnClickListener,ActivityController.CommentUi{

    private static final String EXTRA_IS_TEAM = "is_team";
    private static final String EXTRA_ACTIVITY_ID = "activity_id";

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

    private CommentAdapter mAdapter;

    private boolean isPullRefresh = false;

    public static MessageFragment newInstance(String is_team,String activity_id) {
        Bundle args = new Bundle();
        MessageFragment fragment = new MessageFragment();
        args.putString(EXTRA_IS_TEAM,is_team);
        args.putString(EXTRA_ACTIVITY_ID, activity_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        MessageHeadView headerView = new MessageHeadView(getContext());
        mRefreshLayout.setHeaderView(headerView);

        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                if(isPullRefresh){
                    load();
                }
            }
        });

        mAdapter = new CommentAdapter(this);
        //去掉动画，解决局部刷新时闪烁问题
        ((SimpleItemAnimator)mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);


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


    private void recycleViewScrollToBottom(){
        if(mAdapter.getItemCount() > 0){
            mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
        }
    }

    private void load(){
        String is_team = getArguments().getString(EXTRA_IS_TEAM);
        String activity_id = getArguments().getString(EXTRA_ACTIVITY_ID);
        Map<String,String> params = new HashMap<>();
        params.put("token", AppCookie.getToken());
        params.put("is_team",is_team);
        if(isPullRefresh && mAdapter.getItemCount() > 0){
            params.put("last_at",mAdapter.getItem(mAdapter.getItemCount()-1).getCreated_at());
        }else{
            params.put("page_index","1");
        }
        getCallbacks().getComments(activity_id,params);
    }

    @Override
    protected BaseController getPresenter() {
        return new ActivityController();
    }

    @Override
    public void onResponseError(ResponseError error) {
        super.onResponseError(error);
        if(mAdapter.getItemCount() == 0){
            mMultiStateView.setState(MultiStateView.STATE_ERROR)
                    .setButton(v -> onRetryClick());
        }
    }

    protected void onRetryClick() {
        mMultiStateView.setState(MultiStateView.STATE_LOADING);
        load();
    }

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
       }
    }

    @Override
    public void getCommentSuccess(List<Comment> comments) {
        if(isDetached()) return;
        if (comments != null && !comments.isEmpty()) {
            Collections.reverse(comments);
            if(isPullRefresh){
                mAdapter.addItems(0,comments);
                mRefreshLayout.finishRefreshing();
                mRecyclerView.scrollToPosition(comments.size() - 1);
            }else{
                mMultiStateView.setState(MultiStateView.STATE_CONTENT);
                mAdapter.setItems(comments);
                mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
                isPullRefresh = true;
            }
            if(comments.size() < 10){
                mRefreshLayout.setEnableRefresh(false);
            }
        }else{
            if(mAdapter.getItemCount() == 0){
                mMultiStateView.setState(MultiStateView.STATE_EMPTY)
                        .setTitle("暂无留言")
                        .setButton(v -> onRetryClick());
            }else{
                ToastUtil.showText("没有更多了");
            }
        }

    }

}
