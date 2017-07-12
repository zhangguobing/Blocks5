package com.bing.blocks5.ui.search.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.squareup.otto.Subscribe;
import com.bing.blocks5.base.BaseAdapter;
import com.bing.blocks5.base.BaseListFragment;
import com.bing.blocks5.base.BasePresenter;
import com.bing.blocks5.model.Activity;
import com.bing.blocks5.model.event.ActivitySearchEvent;
import com.bing.blocks5.controller.UserController;
import com.bing.blocks5.ui.activity.ActivityDetailActivity;
import com.bing.blocks5.ui.search.adapter.ActivityListAdapter;
import com.bing.blocks5.ui.search.adapter.holder.ActivityViewHolder;
import com.bing.blocks5.util.EventUtil;
import com.bing.blocks5.widget.MultiStateView;
import com.bing.blocks5.widget.BottomSpaceItemDecoration;


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
    protected BasePresenter getPresenter() {
        return new UserController();
    }

    @Subscribe
    public void onActivityList(ActivitySearchEvent event){
        onFinishRequest(event.activities);
    }

}
