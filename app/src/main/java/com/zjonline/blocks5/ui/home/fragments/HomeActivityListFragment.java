package com.zjonline.blocks5.ui.home.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;

import com.lcodecore.tkrefreshlayout.utils.DensityUtil;
import com.zjonline.blocks5.Blocks5App;
import com.zjonline.blocks5.base.BaseAdapter;
import com.zjonline.blocks5.base.BaseListFragment;
import com.zjonline.blocks5.base.BasePresenter;
import com.zjonline.blocks5.model.Activity;
import com.zjonline.blocks5.presenter.ActivityPresenter;
import com.zjonline.blocks5.ui.activity.ActivityDetailActivity;
import com.zjonline.blocks5.ui.search.adapter.ActivityListAdapter;
import com.zjonline.blocks5.ui.search.adapter.holder.ActivityViewHolder;
import com.zjonline.blocks5.widget.MultiStateView;
import com.zjonline.blocks5.widget.BottomSpaceItemDecoration;

import java.util.List;


/**
 * Created by wjb on 2016/4/27.
 */
public class HomeActivityListFragment extends BaseListFragment<Activity,ActivityViewHolder,ActivityPresenter.ActivityUiCallbacks>
   implements ActivityPresenter.ActivityListUi,AppBarLayout.OnOffsetChangedListener{

    private static final String KEY_ACTIVITY_TYPE_ID = "key_activity_type_id";

    private int activity_type_id;

    private long refreshTime  = 0;
    private boolean mInit = false;
    private boolean isLoad = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser){
            if(refreshTime == 0 || refreshTime - System.currentTimeMillis() > 30*60*1000){
                lazyLoad();
            }
             //解决下拉刷新 和tabBarLayout中嵌套时上下滚动冲突
            if(this.getContext() != null){
                Blocks5App application = (Blocks5App) this.getContext().getApplicationContext();
                if(application.mAppBarLayout != null){
                    application.mAppBarLayout.addOnOffsetChangedListener(this);
                }
            }else{
                //viewpager中第一页加载的太早,getContext还拿不到,做个延迟
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(HomeActivityListFragment.this.getContext() != null){
                            Blocks5App application = (Blocks5App) HomeActivityListFragment.this.getContext().getApplicationContext();
                            if(application.mAppBarLayout != null){
                                application.mAppBarLayout.addOnOffsetChangedListener(HomeActivityListFragment.this);
                            }
                        }
                    }
                },100);
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    protected BasePresenter getPresenter() {
        return new ActivityPresenter();
    }

    public static HomeActivityListFragment newInstance(int activity_type_id) {
        Bundle args = new Bundle();
        args.putInt(KEY_ACTIVITY_TYPE_ID, activity_type_id);
        HomeActivityListFragment fragment = new HomeActivityListFragment();
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
        getCallbacks().getActivityList(activity_type_id,mPage = 1);
    }

    private void lazyLoad(){
        if(!mInit) return;
        if(isLoad) return;
        isLoad = true;
        activity_type_id = getArguments().getInt(KEY_ACTIVITY_TYPE_ID);
        getCallbacks().getActivityList(activity_type_id,mPage);
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
}
