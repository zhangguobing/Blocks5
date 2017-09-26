package com.playmala.playmala.base;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangguobing on 2017/6/5.
 */
public abstract class BaseAdapter<T,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter implements IAdapter<T> {

    private List<T> mItems = new ArrayList<>();
    private ViewEventListener<T> mViewEventListener;


    @Override
    public void setItems(final List<T> update) {
       mItems = update;
       notifyDataSetChanged();
    }

    @Override
    public void addItems(List<T> items) {
        int oldSize = mItems.size();
        mItems.addAll(items);
        notifyItemRangeInserted(oldSize, items.size());
    }

    public void addItems(int position, List<T> items){
        mItems.addAll(position,items);
        notifyItemRangeInserted(position,items.size());
    }

    @Override
    public void addItem(T item) {
        mItems.add(item);
        notifyItemInserted(mItems.size() - 1);
    }

    @Override
    public void delItem(T item) {
        mItems.remove(item);
        notifyItemRemoved(mItems.indexOf(item));
    }

    @Override
    public void clearItems() {
        mItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public T getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public List<T> getItems() {
        return mItems;
    }

    @Override
    public void setItem(int position, T item) {
        mItems.set(position, item);
        notifyItemChanged(position);
    }

    @Override
    public void setViewEventListener(ViewEventListener<T> viewEventListener) {
        mViewEventListener = viewEventListener;
    }

    @Override
    public ViewEventListener<T> getViewEventListener() {
        return mViewEventListener;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(getViewLayoutId(viewType), parent, false);
        return createViewHolder(view, viewType);
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof IViewHolder) {
            IViewHolder<T> holder = (IViewHolder) viewHolder;
            holder.setViewEventListener(mViewEventListener);
            holder.setItem(getItem(position));
            holder.setPosition(position);
        }
        bindViewHolder((VH) viewHolder, getItem(position), position);
    }

    public abstract int getViewLayoutId(int viewType);

    public abstract RecyclerView.ViewHolder createViewHolder(View view, int viewType);

    public abstract void bindViewHolder(VH holder, T item, int position);

}
