package com.playmala.playmala.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by tian on 2017/9/4.
 */

public class NoScrollViewPager extends ViewPager{

    private boolean mNoScroll = false;

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setNoScroll(boolean noScroll) {
        this.mNoScroll = noScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(mNoScroll) return false;
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(mNoScroll) return false;
        return super.onInterceptTouchEvent(ev);
    }
}
