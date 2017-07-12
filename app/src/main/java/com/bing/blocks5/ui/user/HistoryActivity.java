package com.bing.blocks5.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bing.blocks5.base.BaseAdapter;
import com.bing.blocks5.base.BaseListActivity;
import com.bing.blocks5.base.BasePresenter;
import com.bing.blocks5.model.Activity;
import com.bing.blocks5.controller.ActivityUserController;
import com.bing.blocks5.ui.activity.ActivityDetailActivity;
import com.bing.blocks5.ui.search.adapter.ActivityListAdapter;
import com.bing.blocks5.ui.search.adapter.holder.ActivityViewHolder;
import com.bing.blocks5.widget.BottomSpaceItemDecoration;
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
    protected BasePresenter getPresenter() {
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
