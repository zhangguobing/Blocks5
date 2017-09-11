package com.playmala.playmala.ui.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.playmala.playmala.R;
import com.playmala.playmala.base.BaseAdapter;
import com.playmala.playmala.model.Activity;
import com.playmala.playmala.model.ActivityUser;
import com.playmala.playmala.ui.activity.adapter.holder.ActivityUserViewHolder;
import com.playmala.playmala.util.Objects;

/**
 * author：zhangguobing on 2017/7/2 15:31
 * email：bing901222@qq.com
 */

public class ActivityUserAdapter extends BaseAdapter<ActivityUser,ActivityUserViewHolder> {

    private ActivityUserViewHolder.UserOperateListener mListener;
    private Activity activity;

    public ActivityUserAdapter(ActivityUserViewHolder.UserOperateListener listener, Activity activity) {
        this.mListener = listener;
        this.activity = activity;
    }

    @Override
    public int getViewLayoutId(int viewType) {
        return R.layout.item_activity_user;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(View view, int viewType) {
        return new ActivityUserViewHolder(view, mListener, activity);
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
