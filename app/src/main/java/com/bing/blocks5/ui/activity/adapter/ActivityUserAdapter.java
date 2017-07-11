package com.bing.blocks5.ui.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bing.blocks5.R;
import com.bing.blocks5.base.BaseAdapter;
import com.bing.blocks5.model.ActivityUser;
import com.bing.blocks5.ui.activity.adapter.holder.ActivityUserViewHolder;
import com.bing.blocks5.util.Objects;

/**
 * author：zhangguobing on 2017/7/2 15:31
 * email：bing901222@qq.com
 */

public class ActivityUserAdapter extends BaseAdapter<ActivityUser,ActivityUserViewHolder> {

    @Override
    public int getViewLayoutId(int viewType) {
        return R.layout.item_activity_user;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(View view, int viewType) {
        return new ActivityUserViewHolder(view);
    }

    @Override
    public void bindViewHolder(ActivityUserViewHolder holder, ActivityUser item, int position) {
        holder.bind(item);
    }

    @Override
    protected boolean areItemsTheSame(ActivityUser oldItem, ActivityUser newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    protected boolean areContentsTheSame(ActivityUser oldItem, ActivityUser newItem) {
        return Objects.equals(oldItem,newItem);
    }


}
