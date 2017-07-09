package com.zjonline.blocks5.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.zjonline.blocks5.R;
import com.zjonline.blocks5.api.ResponseError;
import com.zjonline.blocks5.util.ToastUtil;

/**
 * author：zhangguobing on 2017/6/19 11:07
 * email：bing901222@qq.com
 */

public abstract class BasePresenterActivity<UC> extends BaseActivity implements BasePresenter.Ui<UC> {

    private UC mCallbacks;

    private BasePresenter mBasePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBasePresenter = getPresenter();
        Preconditions.checkState(mBasePresenter != null, "mBasePresenter can't be null");
        mBasePresenter.attachUi(this);
        loadUiData();
    }


    protected void loadUiData(){

    }

    @Override
    protected void onResume() {
        super.onResume();
        mBasePresenter.init();
        mBasePresenter.startUi(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBasePresenter.suspend();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBasePresenter.detachUi(this);
    }


    protected abstract BasePresenter getPresenter();

    @Override
    public void setCallbacks(UC callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public UC getCallbacks() {
        return mCallbacks;
    }

    @Override
    public void onResponseError(ResponseError error) {
        cancelLoading();
        ToastUtil.showText(error.getMessage());
    }
}
