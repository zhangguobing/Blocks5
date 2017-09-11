package com.playmala.playmala.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.playmala.playmala.R;
import com.playmala.playmala.util.DensityUtil;

/**
 * author：zhangguobing on 2017/6/25 11:56
 * email：bing901222@qq.com
 */

public class ProgressWebView extends WebView{

    private ProgressBar mProgressBar;

    public ProgressWebView(Context context) {
        super(context);
        init(context);
    }

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProgressWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mProgressBar = new ProgressBar(context, null,
                android.R.attr.progressBarStyleHorizontal);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 5));
        mProgressBar.setLayoutParams(layoutParams);

        Drawable drawable = getContext().getResources().getDrawable(
                R.drawable.webview_progress_bar);
        mProgressBar.setProgressDrawable(drawable);
        addView(mProgressBar);

        getSettings().setUseWideViewPort(true);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setSupportZoom(false);
        getSettings().setAllowFileAccess(true);
        getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        getSettings().setBuiltInZoomControls(false);
        getSettings().setDefaultTextEncodingName("utf-8");
        getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);

        setWebChromeClient(new WebChromeClient());
    }

    private class WebChromeClient extends android.webkit.WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mProgressBar.setVisibility(GONE);
            } else {
                if (mProgressBar.getVisibility() == GONE)
                    mProgressBar.setVisibility(VISIBLE);
                mProgressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) mProgressBar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        mProgressBar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }

}
