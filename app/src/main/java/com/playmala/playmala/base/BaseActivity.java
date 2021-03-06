package com.playmala.playmala.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.aitangba.swipeback.SwipeBackActivity;
import com.playmala.playmala.util.AndroidBug54971Workaround;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.playmala.playmala.Blocks5App;
import com.playmala.playmala.R;
import com.playmala.playmala.util.ActivityStack;
import com.playmala.playmala.util.HideKeyBoardUtil;
import com.playmala.playmala.util.ViewUtil;
import com.playmala.playmala.widget.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：zhangguobing on 2017/6/14 13:54
 * email：bing901222@qq.com
 */

public abstract class BaseActivity extends SwipeBackActivity {

    private KProgressHUD mLoading;

    @Nullable
    @Bind(R.id.title_bar)
    TitleBar mTitleBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onBeforeContentView();
        setContentView(getLayoutId());
        if(isFixAndroidBug54971Workaround())  AndroidBug54971Workaround.assistActivity(findViewById(android.R.id.content));
        ButterKnife.bind(this);
        initTitleBar();
        initView(savedInstanceState);
        ActivityStack.create().add(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        ActivityStack.create().remove(this);
        Blocks5App.getContext().getRefWatcher().watch(this);
        ViewUtil.fixInputMethodManagerLeak(this);
    }

    protected void onBeforeContentView(){

    }

    protected boolean isFixAndroidBug54971Workaround(){
        return true;
    }

    protected void initTitleBar(){
        if(mTitleBar != null){
            int whiteColor = ContextCompat.getColor(this,R.color.white);
            mTitleBar.setLeftImageResource(R.mipmap.ic_navigate_back);
            mTitleBar.setTitle(getTitle());
            mTitleBar.setTitleColor(whiteColor);
            mTitleBar.setActionTextColor(whiteColor);
            mTitleBar.setLeftClickListener(this::onTitleLeftClick);
        }
    }

    public void onTitleLeftClick(View view){
        finish();
    }

    @Nullable
    public TitleBar getTitleBar() {
        return mTitleBar;
    }

    @Override
    public void setTitle(CharSequence title) {
        if (mTitleBar != null) {
            mTitleBar.setTitle(title);
        }
    }

    @Override
    public void setTitle(@StringRes int titleResId) {
        if (mTitleBar != null) {
            mTitleBar.setTitle(titleResId);
        }
    }


    protected int getLayoutId() {
        for (Class c = getClass(); c != Context.class; c = c.getSuperclass()) {
            ContentView annotation = (ContentView) c.getAnnotation(ContentView.class);
            if (annotation != null) {
                return annotation.value();
            }
        }
        return 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        HideKeyBoardUtil.init(this);
    }

    protected void initView(Bundle savedInstanceState) {

    }

    protected final void showLoading(@StringRes int textResId) {
        showLoading(getString(textResId));
    }

    protected final void showLoading(String text) {
        cancelLoading();
        mLoading = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(text)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        mLoading.show();
    }

    protected final void cancelLoading() {
        if (mLoading != null && mLoading.isShowing()) {
            mLoading.dismiss();
        }
    }

}
