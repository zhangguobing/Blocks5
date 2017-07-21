package com.bing.blocks5.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bing.blocks5.AppCookie;
import com.bing.blocks5.Constants;
import com.bing.blocks5.R;
import com.bing.blocks5.api.ResponseError;
import com.bing.blocks5.base.BaseController;
import com.bing.blocks5.base.BasePresenterActivity;
import com.bing.blocks5.base.ContentView;
import com.bing.blocks5.controller.UserController;
import com.bing.blocks5.model.FeedBack;
import com.bing.blocks5.ui.setting.adapter.FeedBackAdapter;
import com.bing.blocks5.ui.user.UserDetailActivity;
import com.bing.blocks5.util.KeyboardChangeListener;
import com.bing.blocks5.util.TimeUtil;
import com.bing.blocks5.util.ToastUtil;
import com.bing.blocks5.widget.MessageHeadView;
import com.bing.blocks5.widget.MultiStateView;
import com.flyco.dialog.widget.NormalDialog;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * author：zhangguobing on 2017/7/19 16:30
 * email：bing901222@qq.com
 */
@ContentView(R.layout.activity_feedback)
public class FeedBackActivity extends BasePresenterActivity<UserController.UserUiCallbacks>
implements View.OnClickListener,UserController.FeedBackUi{

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

    private int mPage = 1;

    private FeedBackAdapter mAdapter;

    private boolean isSending = false;
    private FeedBack mCurSendingFeedBack;
    private int mReadySendPosition;

    public static void create(Context context){
        Intent intent = new Intent(context,FeedBackActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        MessageHeadView headerView = new MessageHeadView(this);
        mRefreshLayout.setHeaderView(headerView);

        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                getCallbacks().getFeedBack(mPage);
            }
        });
        mAdapter = new FeedBackAdapter(this);
        //去掉动画，解决局部刷新时闪烁问题
        ((SimpleItemAnimator)mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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

        mContentEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendTextMessage();
            }
            return true;
        });

        new KeyboardChangeListener(this).setKeyBoardListener((isShow, keyboardHeight) -> {
            if(isShow){
                if(mAdapter.getItemCount() > 0){
                    recycleViewScrollToBottom();
                }
            }
        });
    }


    private void sendTextMessage(){
        if(isSending) return;
        isSending = true;
        String text = mContentEditText.getText().toString().trim();
        if(TextUtils.isEmpty(text)) return;
        mContentEditText.setText("");

        mCurSendingFeedBack = null;
        mCurSendingFeedBack  = new FeedBack();
        mCurSendingFeedBack.setUser_id(AppCookie.getUserInfo().getId());
        mCurSendingFeedBack.setContent(text);
        mCurSendingFeedBack.setSend_state(Constants.SendState.SENDING);
        mCurSendingFeedBack.setCreated_at(TimeUtil.getDate());

        mAdapter.addItem(mCurSendingFeedBack);

        recycleViewScrollToBottom();

        mReadySendPosition = mAdapter.getItemCount() > 0 ? mAdapter.getItemCount()-1 : 0;

        getCallbacks().addFeedBack(text);
    }

    private void recycleViewScrollToBottom(){
        if(mAdapter.getItemCount() > 0){
            mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getCallbacks().getFeedBack(mPage);
    }

    @Override
    protected BaseController getPresenter() {
        return new UserController();
    }

    @OnClick(R.id.btn_send)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_photo:
                final Object posOb = v.getTag(R.id.tag_click_content);
                if (posOb != null) {
                    int pos = (int) posOb;
                    FeedBack feedBack = mAdapter.getItem(pos);
                    UserDetailActivity.create(this,feedBack.getUser_id());
                }
                break;
            case R.id.btn_send:
                sendTextMessage();
                break;
            case R.id.icon_progress_failed:
                final NormalDialog dialog = new NormalDialog(this);
                int color = ContextCompat.getColor(this,R.color.primary_text);
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
                    mCurSendingFeedBack = mAdapter.getItem(position);
                    mCurSendingFeedBack.setSend_state(Constants.SendState.SENDING);
                    mAdapter.setItem(position,mCurSendingFeedBack);
                    recycleViewScrollToBottom();
                    getCallbacks().addFeedBack(mCurSendingFeedBack.getContent());
                });
                break;
        }
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
        getCallbacks().getFeedBack(mPage);
    }

    @Override
    public void loadFeedBackSuccess(List<FeedBack> feedBacks) {
        if (feedBacks != null && !feedBacks.isEmpty()) {
            if(mPage == 1){
                mMultiStateView.setState(MultiStateView.STATE_CONTENT);
                mAdapter.setItems(feedBacks);
                mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
            }else{
                mAdapter.addItems(0,feedBacks);
                mRefreshLayout.finishRefreshing();
                mRecyclerView.scrollToPosition(feedBacks.size() - 1);
            }
            mPage ++;
            if(feedBacks.size() < 10){
                mRefreshLayout.setEnableRefresh(false);
            }
        }else{
            if(mAdapter.getItemCount() == 0){
                mMultiStateView.setState(MultiStateView.STATE_EMPTY)
                        .setTitle("暂无意见反馈")
                        .setButton(v -> onRetryClick());
            }else{
                ToastUtil.showText("没有更多了");
            }
        }
    }

    @Override
    public void addFeedBackSuccess(FeedBack feedBack) {
        mCurSendingFeedBack.setSend_state(Constants.SendState.SUCCESS);
        mAdapter.setItem(mReadySendPosition,mCurSendingFeedBack);
        isSending = false;
    }

    @Override
    public void addFeedBackFail(String msg) {
        mCurSendingFeedBack.setSend_state(Constants.SendState.FAILED);
        mAdapter.setItem(mReadySendPosition,mCurSendingFeedBack);
        ToastUtil.showText(msg);
        isSending = false;
    }
}
