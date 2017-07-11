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

public abstract class BasePresenterFragment<UC> extends BaseFragment implements BasePresenter.Ui<UC> {

    private UC mCallbacks;

    private BasePresenter mBasePresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBasePresenter = getPresenter();
        Preconditions.checkState(mBasePresenter != null, "mBasePresenter can't be null");
        mBasePresenter.attachUi(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mBasePresenter.init();
        mBasePresenter.startUi(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mBasePresenter.suspend();
    }


    @Override
    public void onDestroy() {
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
