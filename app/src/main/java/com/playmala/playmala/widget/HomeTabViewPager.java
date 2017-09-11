package com.playmala.playmala.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by wjb on 2016/5/23.
 * 首页的ViewPager，解决与SlidingMenu滑动冲突问题
 */
public class HomeTabViewPager extends ViewPager {

    private int mLastX = 0;
    private int mLastY = 0;

    private boolean scrollable = true;

    public HomeTabViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();


        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:

                if(!scrollable){
                    getParent().requestDisallowInterceptTouchEvent(false);
                    break;
                }
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;

                if(getCurrentItem()==0 && deltaY > 0){
                    getParent().requestDisallowInterceptTouchEvent(true);
                    break;
                }
                if(getCurrentItem()==0 && deltaX >0 ){
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        mLastX = x;
        mLastY = y;
        return super.dispatchTouchEvent(ev);
    }

    public void setScrollable(boolean state){
        scrollable = state;
    }
}
