package com.bing.blocks5.ui.search.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bing.blocks5.R;
import com.bing.blocks5.base.BaseAdapter;
import com.bing.blocks5.model.Activity;
import com.bing.blocks5.ui.search.adapter.holder.ActivityViewHolder;
import com.bing.blocks5.util.Objects;

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

    @Override
    protected boolean areItemsTheSame(Activity oldItem, Activity newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    protected boolean areContentsTheSame(Activity oldItem, Activity newItem) {
        return Objects.equals(oldItem,newItem);
    }
}
