package com.bing.blocks5.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bing.blocks5.base.BaseAdapter;
import com.bing.blocks5.base.BaseListActivity;
import com.bing.blocks5.base.BasePresenter;
import com.bing.blocks5.model.Activity;
import com.bing.blocks5.presenter.ActivityUserPresenter;
import com.bing.blocks5.ui.activity.ActivityDetailActivity;
import com.bing.blocks5.ui.search.adapter.ActivityListAdapter;
import com.bing.blocks5.ui.search.adapter.holder.ActivityViewHolder;

import java.util.List;

/**
 * author：zhangguobing on 2017/7/11 14:24
 * email：bing901222@qq.com
 * 活动收藏列表页
 */

public class FavouriteActivity extends BaseListActivity<Activity,ActivityViewHolder,ActivityUserPresenter.ActivityUserUiCallbacks>
     implements ActivityUserPresenter.HistoryCollectUi{

    public static void create(Context context){
        Intent intent = new Intent(context,FavouriteActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getCallbacks().getHistoryOrCollectActivity(true,mPage,mPageSize);
    }

    @Override
    protected void refreshPage() {
        getCallbacks().getHistoryOrCollectActivity(true,mPage = 1,mPageSize);
    }

    @Override
    protected BasePresenter getPresenter() {
        return new ActivityUserPresenter();
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
