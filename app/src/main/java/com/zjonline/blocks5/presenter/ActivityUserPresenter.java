package com.zjonline.blocks5.presenter;

import com.zjonline.blocks5.api.ApiResponse;
import com.zjonline.blocks5.api.RequestCallback;
import com.zjonline.blocks5.api.ResponseError;
import com.zjonline.blocks5.base.BasePresenter;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author：zhangguobing on 2017/7/5 10:44
 * email：bing901222@qq.com
 */

public class ActivityUserPresenter extends BasePresenter<ActivityUserPresenter.ActivityUserUi,ActivityUserPresenter.ActivityUserUiCallbacks> {

    @Override
    protected ActivityUserUiCallbacks createUiCallbacks(ActivityUserUi ui) {
        return new ActivityUserUiCallbacks() {
            @Override
            public void sign(int activity_id) {
                doSign(getId(ui), activity_id);
            }
        };
    }

    private void doSign(final int callingId, int activity_id){
        mApiClient.activityUserService()
                .sign(activity_id,mToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse response) {
                        ActivityUserUi ui = findUi(callingId);
                        if(ui instanceof ScanBarcodeUi){
                            ((ScanBarcodeUi)ui).signSuccess(response.message);
                        }
                    }
                    @Override
                    public void onFailure(ResponseError error) {
                        ActivityUserUi ui = findUi(callingId);
                        if(ui instanceof ScanBarcodeUi){
                            ((ScanBarcodeUi)ui).signFail(error.getMessage());
                        }
                    }
                });
    }

    public interface ActivityUserUiCallbacks{
        void sign(int activity_id);
    }

    public interface ActivityUserUi extends BasePresenter.Ui<ActivityUserPresenter.ActivityUserUiCallbacks>{

    }

    public interface ScanBarcodeUi extends ActivityUserUi{
        void signSuccess(String msg);
        void signFail(String msg);
    }
}
