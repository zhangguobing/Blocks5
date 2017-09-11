package com.playmala.playmala.base;

import android.view.View;

/**
 * Created by zhangguobing on 2017/6/5.
 */
public interface ViewEventListener<T> {
   void onViewEvent(T item, int position, View view);
}