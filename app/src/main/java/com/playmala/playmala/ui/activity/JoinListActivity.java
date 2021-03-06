package com.playmala.playmala.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.playmala.playmala.base.BaseController;
import com.lcodecore.tkrefreshlayout.utils.DensityUtil;
import com.playmala.playmala.base.BaseAdapter;
import com.playmala.playmala.base.BaseListActivity;
import com.playmala.playmala.model.Activity;
import com.playmala.playmala.controller.ActivityController;
import com.playmala.playmala.ui.search.adapter.ActivityListAdapter;
import com.playmala.playmala.ui.search.adapter.holder.ActivityViewHolder;
import com.playmala.playmala.widget.BottomSpaceItemDecoration;

import java.util.List;

/**
 * author：zhangguobing on 2017/7/7 16:41
 * email：bing901222@qq.com
 */

public class JoinListActivity extends BaseListActivity<Activity,ActivityViewHolder,ActivityController.ActivityUiCallbacks>
        implements ActivityController.ActivityListUi{

    //活动创建者ID
    private static final String EXTRA_JOIN_USER_ID = "extra_user_id";
    private static final String EXTRA_TITLE = "extra_title";

    private int mJoinUserId;

    @Override
    protected String getEmptyTitle() {
        return "暂无活动";
    }

    public static void create(Context context, int join_user_id, String title){
        Intent intent = new Intent(context, JoinListActivity.class);
        intent.putExtra(EXTRA_JOIN_USER_ID,join_user_id);
        intent.putExtra(EXTRA_TITLE,title);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mRecyclerView.addItemDecoration(new BottomSpaceItemDecoration(DensityUtil.dp2px(this,20)));
        setTitle(getIntent().getStringExtra(EXTRA_TITLE));
    }

    @Override
    protected void refreshPage() {
        getCallbacks().getActivityListByJoinId(mJoinUserId,mPage = 1);
    }

    @Override
    protected void nextPage() {
        getCallbacks().getActivityListByJoinId(mJoinUserId,mPage);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mJoinUserId = getIntent().getIntExtra(EXTRA_JOIN_USER_ID,0);
        getCallbacks().getActivityListByJoinId(mJoinUserId,mPage);
    }

    @Override
    protected BaseController getPresenter() {
        return new ActivityController();
    }

    @Override
    protected void onItemClick(int position, Activity item) {
        ActivityDetailActivity.create(this,item.getId()+"");
    }

    @Override
    protected BaseAdapter<Activity, ActivityViewHolder> getAdapter() {
        return new ActivityListAdapter();
    }

    @Override
    public void activityListCallback(List<Activity> activities) {
        onFinishRequest(activities);
    }
}
