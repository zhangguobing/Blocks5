package com.playmala.playmala.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;

import com.playmala.playmala.api.ResponseError;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.playmala.playmala.R;
import com.playmala.playmala.util.ToastUtil;
import com.playmala.playmala.widget.LoadMoreView;
import com.playmala.playmala.widget.MultiStateView;
import com.playmala.playmala.widget.RefreshHeadView;

import java.util.List;

import butterknife.Bind;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * author：ZhangGuoBing on 2017/6/5 16:22
 * email：bing901222@qq.com
 */
@ContentView(R.layout.activity_base_list)
public abstract class BaseListActivity<T,VH extends RecyclerView.ViewHolder,UC> extends BasePresenterActivity<UC> {

    public static final int DEFAULT_PAGE_SIZE = 15;
    public static final int DEFAULT_PAGE = 1;

    @Bind(R.id.multi_state_view)
    public MultiStateView mMultiStateView;
    @Bind(R.id.refresh_layout)
    public TwinklingRefreshLayout mRefreshLayout;
    @Bind(R.id.recycler_view)
    public RecyclerView mRecyclerView;

    protected BaseAdapter<T,VH> mAdapter;

    protected int mPageSize = DEFAULT_PAGE_SIZE;
    protected int mPage = DEFAULT_PAGE;

    private boolean mEnableLoadMore = true;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        //下拉刷新头部控件
        RefreshHeadView headerView = new RefreshHeadView(this);
        mRefreshLayout.setHeaderView(headerView);

        mRefreshLayout.setBottomView(new LoadMoreView(this));
        mRefreshLayout.setEnableLoadmore(getEnableLoadMore());
        mRefreshLayout.setAutoLoadMore(getEnableLoadMore());
        mRefreshLayout.setEnableRefresh(getEnableRefresh());
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                refreshPage();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if(mEnableLoadMore){
                    nextPage();
                }
            }
        });

        mAdapter = getAdapter();
        mAdapter.setViewEventListener((item, position, view) -> onItemClick(position, item));

        //去掉动画，解决局部刷新时闪烁问题
        ((SimpleItemAnimator)mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mMultiStateView.setState(getDefaultState());
    }

    protected boolean isShowRefreshWhenEmpty(){
        return true;
    }

    protected void refreshPage() {}

    protected void nextPage() {}

    protected abstract void onItemClick(int position, T item);

    protected boolean getEnableLoadMore(){
        return true;
    }

    @DrawableRes
    protected int getEmptyIcon() {
        return R.mipmap.ic_empty;
    }

    protected String getEmptyTitle() {
        return "暂无数据";
    }

    protected boolean isDisplayError() {
        return mMultiStateView.getState() != MultiStateView.STATE_CONTENT;
    }

    protected boolean getEnableRefresh() {
        return mMultiStateView.getState() == MultiStateView.STATE_CONTENT
                || mMultiStateView.getState() == MultiStateView.STATE_EMPTY;
    }

    @MultiStateView.State
    protected int getDefaultState() {
        return MultiStateView.STATE_LOADING;
    }

    protected abstract BaseAdapter<T,VH> getAdapter();


    protected void onRetryClick() {
        mMultiStateView.setState(MultiStateView.STATE_LOADING);
        refreshPage();
    }

    @Override
    public void onResponseError(ResponseError error) {
        super.onResponseError(error);
        resetRefreshLayout();
        if(mAdapter.getItemCount() == 0){
            mMultiStateView.setState(MultiStateView.STATE_ERROR)
                    .setTitle(error.getMessage())
                    .setButton(v -> onRetryClick());
        }
    }

    private void resetRefreshLayout(){
        if(isActivityDestroyed()) return;
        mRefreshLayout.finishRefreshing();
        mRefreshLayout.finishLoadmore();
        mMultiStateView.setPtrRefreshComplete();
    }

    public boolean isActivityDestroyed(){
        return Build.VERSION.SDK_INT >= 17 ? isDestroyed() : isFinishing();
    }

    public void onFinishRequest(List<T> items) {
        resetRefreshLayout();
        if (items != null && !items.isEmpty()) {
            if (mPage == 1) {
                mAdapter.setItems(items);
                mMultiStateView.setState(MultiStateView.STATE_CONTENT);
            } else {
                mAdapter.addItems(items);
            }
            mEnableLoadMore = items.size() >= mPageSize;
            if(mEnableLoadMore)  mPage ++;
            mRefreshLayout.setEnableLoadmore(mEnableLoadMore);
        } else {
            if (mPage == 1) {
                mMultiStateView.setState(MultiStateView.STATE_EMPTY)
                        .setTitle(getEmptyTitle());
                if(isShowRefreshWhenEmpty()){
                    mMultiStateView.setPtrHandler(new PtrDefaultHandler() {
                        @Override
                        public void onRefreshBegin(PtrFrameLayout frame) {
                            refreshPage();
                        }
                    });
                }
            } else {
                ToastUtil.showText("没有更多了");
            }
            mRefreshLayout.setEnableLoadmore(mEnableLoadMore = false);
        }
        mRefreshLayout.setEnableRefresh(getEnableRefresh());
    }
}
