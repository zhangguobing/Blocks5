package com.playmala.playmala.ui.search.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.playmala.playmala.base.BaseController;
import com.squareup.otto.Subscribe;
import com.playmala.playmala.base.BaseAdapter;
import com.playmala.playmala.base.BaseListFragment;
import com.playmala.playmala.model.Activity;
import com.playmala.playmala.model.event.ActivitySearchEvent;
import com.playmala.playmala.controller.UserController;
import com.playmala.playmala.ui.activity.ActivityDetailActivity;
import com.playmala.playmala.ui.search.adapter.ActivityListAdapter;
import com.playmala.playmala.ui.search.adapter.holder.ActivityViewHolder;
import com.playmala.playmala.util.EventUtil;
import com.playmala.playmala.widget.MultiStateView;
import com.playmala.playmala.widget.BottomSpaceItemDecoration;


/**
 * Created by wjb on 2016/4/27.
 */
public class SearchActivityListFragment extends BaseListFragment<Activity,ActivityViewHolder,UserController.UserUiCallbacks>
implements UserController.UserUi{
    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mRecyclerView.addItemDecoration(new BottomSpaceItemDecoration(12));
    }

    @Override
    protected int getDefaultState() {
        return MultiStateView.STATE_CONTENT;
    }

    @Override
    protected boolean isShowRefreshWhenEmpty() {
        return false;
    }

    @Override
    protected String getEmptyTitle() {
        return "搜索不到匹配的活动";
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
    protected boolean getEnableRefresh() {
        return false;
    }


    @Override
    protected boolean getEnableLoadMore() {
        return false;
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

    @Override
    protected BaseController getPresenter() {
        return new UserController();
    }

    @Subscribe
    public void onActivityList(ActivitySearchEvent event){
//        if(event.activities == null || event.activities.isEmpty()) mAdapter.clearItems();
        onFinishRequest(event.activities);
    }

}
