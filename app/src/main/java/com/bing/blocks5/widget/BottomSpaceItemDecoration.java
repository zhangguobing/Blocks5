package com.bing.blocks5.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * author：zhangguobing on 2017/7/3 11:23
 * email：bing901222@qq.com
 */

public class BottomSpaceItemDecoration extends RecyclerView.ItemDecoration{

    private int space;

    public BottomSpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = space;
    }
}
