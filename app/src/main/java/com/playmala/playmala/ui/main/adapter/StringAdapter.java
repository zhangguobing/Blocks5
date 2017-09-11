package com.playmala.playmala.ui.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.playmala.playmala.R;
import com.playmala.playmala.base.BaseAdapter;
import com.playmala.playmala.ui.main.viewholder.StringViewHolder;

/**
 * author：zhangguobing on 2017/6/22 20:49
 * email：bing901222@qq.com
 */

public class StringAdapter extends BaseAdapter<String,StringViewHolder> {
    public StringAdapter() {
    }

    @Override
    public int getViewLayoutId(int viewType) {
        return R.layout.rv_item;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(View view, int viewType) {
        return new StringViewHolder(view);
    }

    @Override
    public void bindViewHolder(StringViewHolder holder, String item, int position) {
        holder.bind(item);
    }

    @Override
    protected boolean areItemsTheSame(String oldItem, String newItem) {
        return false;
    }

    @Override
    protected boolean areContentsTheSame(String oldItem, String newItem) {
        return false;
    }
}
