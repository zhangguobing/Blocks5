package com.bing.blocks5.base;


import java.util.List;

/**
 * Created by zhangguobing on 2017/6/5.
 */
public interface IAdapter<T> {

    void setItems(List<T> items);

    void addItem(T item);

    void delItem(T item);

    void addItems(List<T> items);

    void clearItems();

    void setItem(int position, T item);

    T getItem(int position);

    List<T> getItems();

    ViewEventListener<T> getViewEventListener();

    void setViewEventListener(ViewEventListener<T> viewEventListener);
}