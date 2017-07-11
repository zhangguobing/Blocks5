package com.bing.blocks5.base;

import android.os.AsyncTask;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by zhangguobing on 2017/6/5.
 */
public abstract class BaseAdapter<T,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter implements IAdapter<T> {

    private List<T> mItems;
    private ViewEventListener<T> mViewEventListener;

    private int dataVersion = 0;

    @Override
    public void setItems(final List<T> update) {
        dataVersion ++;
        if(mItems == null){
            if(update == null){
                return;
            }
            mItems = update;
            notifyDataSetChanged();
        }else if(update == null){
            int oldSize = mItems.size();
            mItems = null;
            notifyItemRangeRemoved(0, oldSize);
        }else {
            final int startVersion = dataVersion;
            final List<T> oldItems = mItems;
            new AsyncTask<Void, Void, DiffUtil.DiffResult>() {
                @Override
                protected DiffUtil.DiffResult doInBackground(Void... voids) {
                    return DiffUtil.calculateDiff(new DiffUtil.Callback() {
                        @Override
                        public int getOldListSize() {
                            return oldItems.size();
                        }

                        @Override
                        public int getNewListSize() {
                            return update.size();
                        }

                        @Override
                        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                            T oldItem = oldItems.get(oldItemPosition);
                            T newItem = update.get(newItemPosition);
                            return BaseAdapter.this.areItemsTheSame(oldItem, newItem);
                        }

                        @Override
                        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                            T oldItem = oldItems.get(oldItemPosition);
                            T newItem = update.get(newItemPosition);
                            return BaseAdapter.this.areContentsTheSame(oldItem, newItem);
                        }
                    });
                }

                @Override
                protected void onPostExecute(DiffUtil.DiffResult diffResult) {
                    if (startVersion != dataVersion) {
                        // ignore update
                        return;
                    }
                    mItems = update;
                    diffResult.dispatchUpdatesTo(BaseAdapter.this);

                }
            }.execute();
        }
    }

    @Override
    public void addItems(List<T> items) {
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public void addItem(T item) {
        mItems.add(item);
        notifyDataSetChanged();
    }

    @Override
    public void delItem(T item) {
        mItems.remove(item);
        notifyDataSetChanged();
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

    protected abstract boolean areItemsTheSame(T oldItem, T newItem);

    protected abstract boolean areContentsTheSame(T oldItem, T newItem);

}
