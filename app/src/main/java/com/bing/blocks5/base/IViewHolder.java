package com.bing.blocks5.base;

/**
 * Created by zhangguobing on 2017/6/5.
 */
public interface IViewHolder<T> {

    void setViewEventListener(ViewEventListener<T> viewEventListener);

    void setItem(T item);

    void setPosition(int position);
}
