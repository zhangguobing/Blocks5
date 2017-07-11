package com.bing.blocks5.ui.home.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bing.blocks5.R;
import com.bing.blocks5.base.BaseAdapter;
import com.bing.blocks5.ui.home.viewholder.StringViewHolder;

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
