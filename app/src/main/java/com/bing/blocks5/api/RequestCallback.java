package com.zjonline.blocks5.api;

import android.util.Log;

import rx.Subscriber;

/**
 * Created by zhangguobing on 2017/4/21.
 */
public abstract class RequestCallback<T> extends Subscriber<T> {

    private static final String TAG = "RequestCallback";

    @Override
    public final void onNext(T t) {
        onResponse(t);
    }

    @Override
    public final void onError(Throwable throwable) {
        if (throwable instanceof ResponseError) {
            onFailure((ResponseError) throwable);
        } else {
            Log.e(TAG, "throwable isn't instance of ResponseError");
        }
    }

    @Override
    public void onStart() {

    }

    public void onResponse(T response) {
    }

    public void onFailure(ResponseError error) {
    }

    @Override
    public void onCompleted() {
    }
}