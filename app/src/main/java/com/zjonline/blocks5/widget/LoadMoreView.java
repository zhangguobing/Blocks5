package com.zjonline.blocks5.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.IBottomView;
import com.lcodecore.tkrefreshlayout.utils.DensityUtil;

/**
 * author：zhangguobing on 2017/6/9 10:59
 * email：bing901222@qq.com
 * 底部加载更多View
 */

public class LoadMoreView extends RelativeLayout implements IBottomView {

    private Drawable mLoadingDrawable;

    public LoadMoreView(Context context) {
        this(context,null);
    }

    public LoadMoreView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int size = DensityUtil.dp2px(context,34);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,size);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        setLayoutParams(params);

        setGravity(Gravity.CENTER);

        TextView textView = new TextView(getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);
        textView.setText("正在加载...");
        textView.setGravity(Gravity.CENTER);
        textView.setCompoundDrawablePadding(DensityUtil.dp2px(getContext(),10));

        mLoadingDrawable = ContextCompat.getDrawable(getContext(),com.lcodecore.tkrefreshlayout.R.drawable.anim_loading_view);
        mLoadingDrawable.setBounds(0,0,mLoadingDrawable.getIntrinsicWidth(),mLoadingDrawable.getIntrinsicHeight());
        textView.setCompoundDrawables(mLoadingDrawable,null,null,null);

        addView(textView);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPullingUp(float fraction, float maxBottomHeight, float bottomHeight) {

    }

    @Override
    public void startAnim(float maxBottomHeight, float bottomHeight) {
        ((AnimationDrawable)mLoadingDrawable).start();
    }

    @Override
    public void onPullReleasing(float fraction, float maxBottomHeight, float bottomHeight) {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void reset() {

    }
}
