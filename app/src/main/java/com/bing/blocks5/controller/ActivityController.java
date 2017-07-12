package com.bing.blocks5.controller;

import com.bing.blocks5.api.ApiResponse;
import com.bing.blocks5.api.RequestCallback;
import com.bing.blocks5.api.ResponseError;
import com.bing.blocks5.base.BasePresenter;
import com.bing.blocks5.model.Activity;
import com.bing.blocks5.ui.activity.request.CreateActivityParams;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author：zhangguobing on 2017/7/1 16:21
 * email：bing901222@qq.com
 */

public class ActivityController extends BasePresenter<ActivityController.ActivityUi,ActivityController.ActivityUiCallbacks> {

    @Override
    protected ActivityUiCallbacks createUiCallbacks(ActivityUi ui) {
        return new ActivityUiCallbacks() {
            @Override
            public void createActivity(CreateActivityParams params) {
                doCreateActivity(getId(ui), params);
            }

            @Override
            public void getActivityList(int activity_type_id, int page_index) {
                doGetActivityList(getId(ui), activity_type_id, page_index);
            }

            @Override
            public void getActivityList(int user_id, int activity_state, int page_index) {
                doGetActivityList(getId(ui), user_id,activity_state, page_index);
            }

            @Override
            public void getActivityListByUserId(int user_id, int page_index) {
                doGetActivityListByUserId(getId(ui), user_id, page_index);
            }

            @Override
            public void getActivityListByJoinId(int join_user_id, int page_index) {
                doGetActivityListByJoinId(getId(ui), join_user_id, page_index);
            }

            @Override
            public void getActivityListByJoinIdAndState(int join_user_id, int activity_state, int page_index) {
                doGetActivityListByJoinIdAndState(getId(ui), join_user_id,activity_state, page_index);
            }

            @Override
            public void getActivityById(int activity_id) {
                doGetActivityById(getId(ui), activity_id);
            }

            @Override
            public void collectActivity(int activity_id) {
                doCollectActivity(getId(ui), activity_id);
            }

            @Override
            public void join(int activity_id) {
                doJoin(getId(ui), activity_id);
            }

            @Override
            public void report(int activity_id, String content) {
                doReportActivity(getId(ui), activity_id, content);
            }

            @Override
            public void start(int activity_id) {
                doStartActivity(getId(ui), activity_id);
            }
        };
    }

    private void doCreateActivity(final int callingId, CreateActivityParams params){
        mApiClient.activityService()
                .createActivity(mToken,params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse response) {
                        ActivityUi ui = findUi(callingId);
                        if(ui instanceof CreateActivityUi){
                            ((CreateActivityUi)ui).createActivitySuccess(response.message);
                        }
                    }
                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }


    private void doGetActivityList(final int callingId, int activity_type_id, int page_index){
        mApiClient.activityService()
                .getActivityList(mToken,activity_type_id,page_index)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse<List<Activity>>>() {
                    @Override
                    public void onResponse(ApiResponse<List<Activity>> response) {
                        ActivityUi ui = findUi(callingId);
                        if(ui instanceof ActivityListUi){
                            ((ActivityListUi)ui).activityListCallback(response.data);
                        }
                    }
                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    private void doGetActivityList(final int callingId, int user_id, int state, int page_index){
        mApiClient.activityService()
                .getActivityList(mToken,user_id,state,page_index)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse<List<Activity>>>() {
                    @Override
                    public void onResponse(ApiResponse<List<Activity>> response) {
                        ActivityUi ui = findUi(callingId);
                        if(ui instanceof ActivityListUi){
                            ((ActivityListUi)ui).activityListCallback(response.data);
                        }
                    }
                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    private void doGetActivityListByUserId(final int callingId, int user_id, int page_index){
        mApiClient.activityService()
                .getActivityListByUserId(mToken,user_id,page_index)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse<List<Activity>>>() {
                    @Override
                    public void onResponse(ApiResponse<List<Activity>> response) {
                        ActivityUi ui = findUi(callingId);
                        if(ui instanceof ActivityListUi){
                            ((ActivityListUi)ui).activityListCallback(response.data);
                        }
                    }
                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    private void doGetActivityListByJoinId(final int callingId, int join_user_id, int page_index){
        mApiClient.activityService()
                .getActivityListByJoinId(mToken,join_user_id,page_index)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse<List<Activity>>>() {
                    @Override
                    public void onResponse(ApiResponse<List<Activity>> response) {
                        ActivityUi ui = findUi(callingId);
                        if(ui instanceof ActivityListUi){
                            ((ActivityListUi)ui).activityListCallback(response.data);
                        }
                    }
                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    private void doGetActivityListByJoinIdAndState(final int callingId, int join_user_id, int state, int page_index){
        mApiClient.activityService()
                .getActivityListByJoinIdAndState(mToken,join_user_id,state,page_index)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse<List<Activity>>>() {
                    @Override
                    public void onResponse(ApiResponse<List<Activity>> response) {
                        ActivityUi ui = findUi(callingId);
                        if(ui instanceof ActivityListUi){
                            ((ActivityListUi)ui).activityListCallback(response.data);
                        }
                    }
                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    private void doCollectActivity(final int callingId, int activity_id){
        mApiClient.activityService()
                .collectActivity(activity_id,mToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse response) {
                        ActivityUi ui = findUi(callingId);
                        if(ui instanceof ActivityDetailUi){
                            ((ActivityDetailUi)ui).onCollectSuccess(response.message);
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }


    private void doReportActivity(final int callingId, int activity_id, String content){
        mApiClient.activityService()
                .reportActivity(mToken,0,activity_id,content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse response) {
                        ActivityUi ui = findUi(callingId);
                        if(ui instanceof ActivityDetailUi){
                            ((ActivityDetailUi)ui).reportSuccess();
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    private void doStartActivity(final int callingId, int activity_id){
        mApiClient.activityService()
                .startActivity(activity_id,mToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse response) {
                        ActivityUi ui = findUi(callingId);
                        if(ui instanceof SignUpUi) {
                            ((SignUpUi)ui).startActivitySuccess();
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    private void doGetActivityById(final int callingId, int activity_id){
        mApiClient.activityService()
                .getActivityById(activity_id,mToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse<Activity>>() {
                    @Override
                    public void onResponse(ApiResponse<Activity> response) {
                        ActivityUi ui = findUi(callingId);
                        if(ui instanceof ActivityDetailUi){
                            ((ActivityDetailUi)ui).getActivitySuccess(response.data);
                        }
                    }
                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    //报名
    private void doJoin(final int callingId, int activity_id){
        mApiClient.activityUserService()
                .joinActivity(activity_id,mToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse response) {
                        ActivityController.ActivityUi ui = findUi(callingId);
                        if(ui instanceof ActivityController.ActivityDetailUi){
                            ((ActivityController.ActivityDetailUi)ui).joinSuccess();
                        }
                    }
                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    public interface ActivityUiCallbacks{
        void createActivity(CreateActivityParams params);
        void getActivityList(int activity_type_id, int page_index);
        void getActivityList(int user_id, int activity_state, int page_index);
        void getActivityListByUserId(int user_id, int page_index);
        void getActivityListByJoinId(int join_user_id, int page_index);
        void getActivityListByJoinIdAndState(int join_user_id, int activity_state, int page_index);
        void getActivityById(int activity_id);
        void collectActivity(int activity_id);
        void join(int activity_id);
        void report(int activity_id,String content);
        void start(int activity_id);
    }

    public interface ActivityUi extends BasePresenter.Ui<ActivityUiCallbacks>{

    }

    public interface CreateActivityUi extends ActivityUi{
        void createActivitySuccess(String msg);
    }

    public interface ActivityListUi extends ActivityUi{
        void activityListCallback(List<Activity> activities);
    }

    public interface SignUpUi extends ActivityUi{
        void startActivitySuccess();
    }

    public interface ActivityDetailUi extends ActivityUi{
        void getActivitySuccess(Activity activity);
        void onCollectSuccess(String msg);
        void joinSuccess();
        void reportSuccess();
    }
}
