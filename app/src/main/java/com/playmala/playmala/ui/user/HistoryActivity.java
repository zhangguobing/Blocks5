package com.playmala.playmala.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.playmala.playmala.base.BaseAdapter;
import com.playmala.playmala.base.BaseListActivity;
import com.playmala.playmala.base.BaseController;
import com.playmala.playmala.model.Activity;
import com.playmala.playmala.controller.ActivityUserController;
import com.playmala.playmala.ui.activity.ActivityDetailActivity;
import com.playmala.playmala.ui.search.adapter.ActivityListAdapter;
import com.playmala.playmala.ui.search.adapter.holder.ActivityViewHolder;
import com.playmala.playmala.widget.BottomSpaceItemDecoration;
import com.lcodecore.tkrefreshlayout.utils.DensityUtil;

import java.util.List;

/**
 * author：zhangguobing on 2017/7/11 14:24
 * email：bing901222@qq.com
 */

public class HistoryActivity extends BaseListActivity<Activity,ActivityViewHolder,ActivityUserController.ActivityUserUiCallbacks>
     implements ActivityUserController.HistoryCollectUi{


    public static void create(Context context){
        Intent intent = new Intent(context,HistoryActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected String getEmptyTitle() {
        return "暂无历史活动";
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mRecyclerView.addItemDecoration(new BottomSpaceItemDecoration(DensityUtil.dp2px(this,20)));
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getCallbacks().getHistoryOrCollectActivity(false,mPage,mPageSize);
    }

    @Override
    protected void refreshPage() {
        getCallbacks().getHistoryOrCollectActivity(false,mPage = 1,mPageSize);
    }

    protected void nextPage() {
        getCallbacks().getHistoryOrCollectActivity(false,mPage++,mPageSize);
    }

    @Override
    protected BaseController getPresenter() {
        return new ActivityUserController();
    }

    @Override
    protected void onItemClick(int position, Activity item) {
        ActivityDetailActivity.create(this,item.getId() +"");
    }

    @Override
    protected BaseAdapter<Activity, ActivityViewHolder> getAdapter() {
        return new ActivityListAdapter();
    }

    @Override
    public void onActivitiesResult(List<Activity> activities) {
        onFinishRequest(activities);
    }
}
