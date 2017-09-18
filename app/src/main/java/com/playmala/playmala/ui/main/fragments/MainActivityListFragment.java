package com.playmala.playmala.ui.main.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;

import com.playmala.playmala.base.BaseController;
import com.playmala.playmala.model.event.MainActivityListFilterEvent;
import com.playmala.playmala.model.request.MainActivityListParams;
import com.playmala.playmala.util.EventUtil;
import com.lcodecore.tkrefreshlayout.utils.DensityUtil;
import com.playmala.playmala.base.BaseAdapter;
import com.playmala.playmala.base.BaseListFragment;
import com.playmala.playmala.model.Activity;
import com.playmala.playmala.controller.ActivityController;
import com.playmala.playmala.ui.activity.ActivityDetailActivity;
import com.playmala.playmala.ui.search.adapter.ActivityListAdapter;
import com.playmala.playmala.ui.search.adapter.holder.ActivityViewHolder;
import com.playmala.playmala.widget.MultiStateView;
import com.playmala.playmala.widget.BottomSpaceItemDecoration;
import com.squareup.otto.Subscribe;

import java.util.List;


/**
 * Created by wjb on 2016/4/27.
 */
public class MainActivityListFragment extends BaseListFragment<Activity,ActivityViewHolder,ActivityController.ActivityUiCallbacks>
   implements ActivityController.ActivityListUi,AppBarLayout.OnOffsetChangedListener{

    private static final String KEY_TYPE_ID = "key_type_id";

    private MainActivityListParams mainActivityListParams = MainActivityListParams.getDefault();

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
    protected String getEmptyTitle() {
        return "暂无活动";
    }

    @Override
    protected BaseController getPresenter() {
        return new ActivityController();
    }

    public static MainActivityListFragment newInstance(int type_id) {
        Bundle args = new Bundle();
        args.putInt(KEY_TYPE_ID, type_id);
        MainActivityListFragment fragment = new MainActivityListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mainActivityListParams.activity_type_id = getArguments().getInt(KEY_TYPE_ID);
        mRecyclerView.addItemDecoration(new BottomSpaceItemDecoration(DensityUtil.dp2px(getContext(),10)));
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
        mainActivityListParams.area = event.params.area;
        mainActivityListParams.city = event.params.city;
        mainActivityListParams.begin_at = event.params.begin_at;
        mainActivityListParams.end_at = event.params.end_at;
        mainActivityListParams.sort_type = event.params.sort_type;
        mainActivityListParams.state = event.params.state;
        getCallbacks().getActivityList(mainActivityListParams,mPage = 1);
    }
}
