package com.playmala.playmala.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.playmala.playmala.R;
import com.lcodecore.tkrefreshlayout.IHeaderView;
import com.lcodecore.tkrefreshlayout.OnAnimEndListener;

/**
 * author：zhangguobing on 2017/6/13 13:56
 * 消息刷新头控件
 */

public class MessageHeadView extends FrameLayout implements IHeaderView {
    private ImageView refreshArrow;
    private ImageView loadingView;
    private TextView refreshTextView;

    public MessageHeadView(Context context) {
        this(context, null);
    }

    public MessageHeadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MessageHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View rootView = View.inflate(getContext(), R.layout.layout_refresh_view, null);
        refreshArrow = (ImageView) rootView.findViewById(R.id.iv_arrow);
        refreshTextView = (TextView) rootView.findViewById(R.id.tv);
        loadingView = (ImageView) rootView.findViewById(R.id.iv_loading);
        addView(rootView);
    }

    public void setArrowResource(@DrawableRes int resId) {
        refreshArrow.setImageResource(resId);
    }

    public void setTextColor(@ColorInt int color) {
        refreshTextView.setTextColor(color);
    }

    public void setPullDownStr(String pullDownStr1) {
        pullDownStr = pullDownStr1;
    }

    public void setReleaseRefreshStr(String releaseRefreshStr1) {
        releaseRefreshStr = releaseRefreshStr1;
    }

    public void setRefreshingStr(String refreshingStr1) {
        refreshingStr = refreshingStr1;
    }

    private String pullDownStr = "下拉加载更多";
    private String releaseRefreshStr = "松开加载更多";
    private String refreshingStr = "正在加载，请稍候...";
    private String refreshCompleteStr ="加载完成";

    private boolean isAnimation = false;

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPullingDown(float fraction, float maxHeadHeight, float headHeight) {
        if (fraction < 1f  && !isAnimation && refreshArrow.getRotation() == 180){
            isAnimation = true;
            refreshTextView.setText(pullDownStr);
            rotationAnimation(180, 0);
        }
        if (fraction > 1f && !isAnimation && refreshArrow.getRotation() == 0){
            isAnimation = true;
            refreshTextView.setText(releaseRefreshStr);
            rotationAnimation(0, 180);
        }
    }

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {
        if (fraction < 1f && !isAnimation && refreshArrow.getRotation() == 180) {
            isAnimation = true;
            refreshTextView.setText(pullDownStr);
            rotationAnimation(180, 0);
            if (refreshArrow.getVisibility() == GONE) {
                refreshArrow.setVisibility(VISIBLE);
                loadingView.setVisibility(GONE);
            }
        }
    }

    public void rotationAnimation(float from, final float to) {
       ValueAnimator animator = ValueAnimator.ofFloat(from, to);
        animator.addUpdateListener(valueAnimator -> {
            float values = (float) valueAnimator.getAnimatedValue();
            refreshArrow.setRotation(values);
            if (values == to) {
                isAnimation = false;
            }
        });
        animator.setRepeatCount(0);
        animator.setDuration(150);
        animator.start();
    }

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {
        refreshTextView.setText(refreshingStr);
        refreshArrow.setVisibility(GONE);
        loadingView.setVisibility(VISIBLE);
        ((AnimationDrawable) loadingView.getDrawable()).start();
    }

    @Override
    public void onFinish(OnAnimEndListener listener) {
        refreshTextView.setText(refreshCompleteStr);
        listener.onAnimEnd();
    }

    @Override
    public void reset() {
        refreshArrow.setVisibility(VISIBLE);
        loadingView.setVisibility(GONE);
        refreshTextView.setText(pullDownStr);
    }
}
