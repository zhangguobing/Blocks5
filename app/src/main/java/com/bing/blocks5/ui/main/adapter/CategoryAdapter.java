package com.bing.blocks5.ui.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bing.blocks5.R;
import com.bing.blocks5.base.BaseAdapter;
import com.bing.blocks5.model.Config;
import com.bing.blocks5.ui.main.viewholder.CategoryViewHolder;
import com.bing.blocks5.util.Objects;

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

    @Override
    protected boolean areItemsTheSame(Config.ActivityTypesBean oldItem, Config.ActivityTypesBean newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    protected boolean areContentsTheSame(Config.ActivityTypesBean oldItem, Config.ActivityTypesBean newItem) {
        return Objects.equals(oldItem,newItem);
    }
}
