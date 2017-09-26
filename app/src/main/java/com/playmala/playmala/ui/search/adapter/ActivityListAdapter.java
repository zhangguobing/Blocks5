package com.playmala.playmala.ui.search.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.playmala.playmala.R;
import com.playmala.playmala.base.BaseAdapter;
import com.playmala.playmala.model.Activity;
import com.playmala.playmala.ui.search.adapter.holder.ActivityViewHolder;

/**
 * author：zhangguobing on 2017/7/2 14:11
 * email：bing901222@qq.com
 */

public class ActivityListAdapter extends BaseAdapter<Activity,ActivityViewHolder> {
    @Override
    public int getViewLayoutId(int viewType) {
        return R.layout.item_activity;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(View view, int viewType) {
        return new ActivityViewHolder(view);
    }

    @Override
    public void bindViewHolder(ActivityViewHolder holder, Activity item, int position) {
        holder.bind(item);
    }
}
