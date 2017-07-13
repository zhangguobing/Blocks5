package com.bing.blocks5.base;

import android.util.Log;

import com.google.common.base.Preconditions;
import com.bing.blocks5.AppConfig;
import com.bing.blocks5.AppCookie;
import com.bing.blocks5.api.ApiClient;
import com.bing.blocks5.api.ResponseError;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * author：zhangguobing on 2017/6/19 10:21
 * email：bing901222@qq.com
 */

public abstract class BaseController<U extends BaseController.Ui<UC>, UC> {

    protected final ApiClient mApiClient;

    private final Set<U> mUis;
    private final Set<U> mUnmodifiableUis;

    private boolean mInited;

    protected String mToken = AppCookie.getToken();

    public BaseController() {
        mApiClient = ApiClient.getInstance();
        mUis = new CopyOnWriteArraySet<>();
        mUnmodifiableUis = Collections.unmodifiableSet(mUis);
    }

    protected final void init() {
        Preconditions.checkState(!mInited, "Already inited");
        onInited();
        mInited = true;
    }

    protected final void suspend() {
        Preconditions.checkState(mInited, "Not inited");
        onSuspended();
        mInited = false;
    }

    protected void onInited() {}

    protected void onSuspended() {}

    public final boolean isInited() {
        return mInited;
    }

    protected abstract UC createUiCallbacks(U ui);

    protected synchronized final void attachUi(U ui) {
        Preconditions.checkArgument(ui != null, "ui cannot be null");
        Preconditions.checkState(!mUis.contains(ui), "UI is already attached");
        mUis.add(ui);
        ui.setCallbacks(createUiCallbacks(ui));
    }

    protected synchronized final void startUi(U ui) {
        Preconditions.checkArgument(ui != null, "ui cannot be null");
        Preconditions.checkState(mUis.contains(ui), "ui is not attached");
        populateUi(ui);
    }

    protected synchronized final void detachUi(U ui) {
        Preconditions.checkArgument(ui != null, "ui cannot be null");
        Preconditions.checkState(mUis.contains(ui), "ui is not attached");
        ui.setCallbacks(null);
        mUis.remove(ui);
    }

    protected synchronized void populateUi(U ui) {}

    protected synchronized final void populateUis() {
        if (AppConfig.DEBUG) {
            Log.d(getClass().getSimpleName(), "populateUis");
        }
        for (U ui : mUis) {
            populateUi(ui);
        }
    }

    protected final Set<U> getUis() {
        return mUnmodifiableUis;
    }

    protected int getId(U ui) {
        return ui.hashCode();
    }

    protected synchronized U findUi(final int id) {
        for (U ui : mUis) {
            if (getId(ui) == id) {
                return ui;
            }
        }
        return null;
    }

    public interface Ui<UC> {
        void setCallbacks(UC callbacks);

        UC getCallbacks();

        void onResponseError(ResponseError error);
    }

}
