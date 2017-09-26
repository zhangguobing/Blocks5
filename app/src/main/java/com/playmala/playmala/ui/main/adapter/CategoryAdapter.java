package com.playmala.playmala.ui.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.playmala.playmala.R;
import com.playmala.playmala.base.BaseAdapter;
import com.playmala.playmala.model.Config;
import com.playmala.playmala.ui.main.viewholder.CategoryViewHolder;

/**
 * Created by tian on 2017/9/6.
 */

public class CategoryAdapter extends BaseAdapter<Config.ActivityTypesBean,CategoryViewHolder> {
    @Override
    public int getViewLayoutId(int viewType) {
        return R.layout.item_actvivity_category;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(View view, int viewType) {
        return new CategoryViewHolder(view);
    }

    @Override
    public void bindViewHolder(CategoryViewHolder holder, Config.ActivityTypesBean item, int position) {
        holder.bind(item);
    }
}
