package com.bing.blocks5.ui.main.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.view.View;

import com.bing.blocks5.api.ResponseError;
import com.bing.blocks5.base.BaseController;
import com.bing.blocks5.model.event.MainActivityListFilterEvent;
import com.bing.blocks5.ui.main.MainActivity;
import com.bing.blocks5.ui.main.request.MainActivityListParams;
import com.bing.blocks5.util.EventUtil;
import com.lcodecore.tkrefreshlayout.utils.DensityUtil;
import com.bing.blocks5.base.BaseAdapter;
import com.bing.blocks5.base.BaseListFragment;
import com.bing.blocks5.model.Activity;
import com.bing.blocks5.controller.ActivityController;
import com.bing.blocks5.ui.activity.ActivityDetailActivity;
import com.bing.blocks5.ui.search.adapter.ActivityListAdapter;
import com.bing.blocks5.ui.search.adapter.holder.ActivityViewHolder;
import com.bing.blocks5.widget.MultiStateView;
import com.bing.blocks5.widget.BottomSpaceItemDecoration;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wjb on 2016/4/27.
 */
public class MainActivityListFragment extends BaseListFragment<Activity,ActivityViewHolder,ActivityController.ActivityUiCallbacks>
   implements ActivityController.ActivityListUi,AppBarLayout.OnOffsetChangedListener{

    private static final String KEY_PARMAS = "key_parmas";

    private MainActivityListParams mainActivityListParams;

    private long refreshTime  = 0;
    private boolean mInit = false;
    private boolean isLoad = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser){
            if(refreshTime == 0 || refreshTime - System.currentTimeMillis() > 30*60*1000){
                lazyLoad();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    protected BaseController getPresenter() {
        return new ActivityController();
    }

    public static MainActivityListFragment newInstance(MainActivityListParams params) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_PARMAS, params);
        MainActivityListFragment fragment = new MainActivityListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mRecyclerView.addItemDecoration(new BottomSpaceItemDecoration(DensityUtil.dp2px(getContext(),20)));
        mInit = true;
        lazyLoad();
    }

    @Override
    protected void refreshPage() {
        getCallbacks().getActivityList(mainActivityListParams,mPage = 1);
    }

    @Override
    protected void nextPage() {
        getCallbacks().getActivityList(mainActivityListParams,mPage);
    }

    private void lazyLoad(){
        if(!mInit) return;
        if(isLoad) return;
        isLoad = true;
        mainActivityListParams = getArguments().getParcelable(KEY_PARMAS);
        getCallbacks().getActivityList(mainActivityListParams,mPage = 1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mInit = false;
        isLoad = false;
        refreshTime = 0;
    }

    @Override
    protected int getDefaultState() {
        return MultiStateView.STATE_LOADING;
    }

    @Override
    protected void onItemClick(int position, Activity item) {
        ActivityDetailActivity.create(getContext(),item.getId()+"");
    }

    @Override
    protected BaseAdapter<Activity, ActivityViewHolder> getAdapter() {
        return new ActivityListAdapter();
    }

    @Override
    public void activityListCallback(List<Activity> activities) {
        onFinishRequest(activities);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        mRefreshLayout.setEnabled(verticalOffset == 0);
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
    public void onFilter(MainActivityListFilterEvent event){
        getCallbacks().getActivityList(mainActivityListParams = event.params,mPage = 1);
    }
}
