package com.bing.blocks5.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.bing.blocks5.api.ResponseError;
import com.bing.blocks5.util.ToastUtil;

/**
 * author：zhangguobing on 2017/6/19 11:07
 * email：bing901222@qq.com
 */

public abstract class BasePresenterActivity<UC> extends BaseActivity implements BaseController.Ui<UC> {

    private UC mCallbacks;

    private BaseController mBaseController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseController = getPresenter();
        Preconditions.checkState(mBaseController != null, "mBaseController can't be null");
        mBaseController.attachUi(this);
        loadUiData();
    }


    protected void loadUiData(){

    }

    @Override
    protected void onResume() {
        super.onResume();
        mBaseController.init();
        mBaseController.startUi(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBaseController.suspend();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBaseController.detachUi(this);
    }


    protected abstract BaseController getPresenter();

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
