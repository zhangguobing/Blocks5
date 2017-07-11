package com.bing.blocks5.presenter;

import com.bing.blocks5.api.ApiResponse;
import com.bing.blocks5.api.RequestCallback;
import com.bing.blocks5.api.ResponseError;
import com.bing.blocks5.base.BasePresenter;
import com.bing.blocks5.model.Activity;
import com.bing.blocks5.model.ActivityUser;

import java.util.List;

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

            @Override
            public void getUsersByActivityId(int is_sign, int activity_id, String sex) {
                doGetUsersByActivityId(getId(ui),is_sign, activity_id, sex);
            }

            @Override
            public void getHistoryOrCollectActivity(boolean is_collect, int page_index, int page_size) {
                doGetHistoryOrCollectActivity(getId(ui), is_collect, page_index, page_size);
            }
        };
    }

    private void doGetUsersByActivityId(final int callingId, int is_sign, int activity_id, String sex){
        mApiClient.activityUserService()
                .getUsersByActivityId(activity_id, mToken, is_sign, sex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse<List<ActivityUser>>>() {
                    @Override
                    public void onResponse(ApiResponse<List<ActivityUser>> response) {
                        ActivityUserUi ui = findUi(callingId);
                        if(ui instanceof SignUpList){
                            ((SignUpList)ui).onSignUpList(response.data);
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    //签到
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


    private void doGetHistoryOrCollectActivity(final int callingId, boolean is_collect, int page_index, int page_size){
        mApiClient.activityUserService()
                .getHistoryOrCollectActivity(mToken, is_collect ? 1 : 0,page_index,page_size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse<List<Activity>>>() {
                    @Override
                    public void onResponse(ApiResponse<List<Activity>> response) {
                        ActivityUserUi ui = findUi(callingId);
                        if(ui instanceof HistoryCollectUi){
                            ((HistoryCollectUi)ui).onActivitiesResult(response.data);
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    public interface ActivityUserUiCallbacks{
        void sign(int activity_id);
        void getUsersByActivityId(int is_sign,int activity_id,String sex);
        void getHistoryOrCollectActivity(boolean is_collect, int page_index, int page_size);
    }

    public interface ActivityUserUi extends BasePresenter.Ui<ActivityUserPresenter.ActivityUserUiCallbacks>{

    }

    public interface SignUpList extends ActivityUserUi{
        void onSignUpList(List<ActivityUser> users);
    }

    public interface ScanBarcodeUi extends ActivityUserUi{
        void signSuccess(String msg);
        void signFail(String msg);
    }

    public interface HistoryCollectUi extends ActivityUserUi{
        void onActivitiesResult(List<Activity> activities);
    }
}
