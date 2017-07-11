package com.bing.blocks5.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.youth.banner.Banner;

/**
 * author：zhangguobing on 2017/6/22 09:41
 * email：bing901222@qq.com
 * 首页的轮播图，解决与SlidingMenu滑动冲突问题
 */

public class HomeBanner extends Banner {

    private int mLastX = 0;

    private boolean scrollable = true;

    public HomeBanner(Context context) {
        super(context);
    }

    public HomeBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                if(!scrollable){
                    int deltaX = x - mLastX;
                    if(deltaX < 0){
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        mLastX = x;
        return super.dispatchTouchEvent(ev);
    }

    public void setScrollable(boolean state){
        scrollable = state;
    }
}
