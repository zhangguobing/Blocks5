package com.playmala.playmala.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.playmala.playmala.util.ClickUtils;

import butterknife.ButterKnife;

/**
 * Created by zhangguobing on 2017/4/27.
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder implements IViewHolder<T> {

    protected ViewEventListener<T> mViewEventListener;
    protected T mItem;
    protected int mPosition;

    public BaseViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(v -> {
            if(!ClickUtils.isFastDoubleClick()){
                notifyItemAction(mItem,v);
            }
        });
    }

    @Override
    public void setViewEventListener(ViewEventListener<T> viewEventListener) {
        mViewEventListener = viewEventListener;
    }

    @Override
    public void setItem(T item) {
        mItem = item;
    }

    @Override
    public void setPosition(int position) {
        mPosition = position;
    }

    protected void notifyItemAction(T item, View view) {
        if (mViewEventListener != null) {
            mViewEventListener.onViewEvent(item, getAdapterPosition(), view);
        }
    }

    protected abstract void bind(T item);

    protected String getString(@StringRes int strResId) {
        return itemView.getContext().getString(strResId);
    }

    protected String getString(@StringRes int stringResId, Object... formatArgs) {
        return itemView.getContext().getResources().getString(stringResId, formatArgs);
    }

    @ColorInt
    protected int getColor(@ColorRes int colorResId) {
        return itemView.getContext().getResources().getColor(colorResId);
    }

    protected Drawable getDrawable(@DrawableRes int drawableId){
        return ContextCompat.getDrawable(itemView.getContext(),drawableId);
    }

    protected Context getContext(){
        return itemView.getContext();
    }
}
