package com.playmala.playmala.controller;

import com.playmala.playmala.api.ApiResponse;
import com.playmala.playmala.api.RequestCallback;
import com.playmala.playmala.api.ResponseError;
import com.playmala.playmala.base.BaseController;
import com.playmala.playmala.model.Activity;
import com.playmala.playmala.model.User;
import com.playmala.playmala.model.event.ActivitySearchEvent;
import com.playmala.playmala.model.event.UserSearchEvent;
import com.playmala.playmala.util.EventUtil;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author：zhangguobing on 2017/7/2 13:38
 * email：bing901222@qq.com
 */

public class SearchController extends BaseController<SearchController.SearchUi,SearchController.SearchUiCallbacks> {

    @Override
    protected SearchUiCallbacks createUiCallbacks(SearchUi ui) {
        return new SearchUiCallbacks() {
            @Override
            public void getActivityListByIdAndState(int activity_id, String state) {
                doGetActivityListByIdAndState(getId(ui), activity_id, state);
            }

            @Override
            public void getUserById(int user_id) {
                doGetUserById(getId(ui), user_id);
            }
        };
    }

    private void doGetActivityListByIdAndState(final int callingId, int activity_id, String state){
        mApiClient.activityService()
                .getActivityListByIdAndState(mToken,activity_id, "15", state)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse<List<Activity>>>() {
                    @Override
                    public void onResponse(ApiResponse<List<Activity>> response) {
                        findUi(callingId).activityListCallback(response.data);
                        EventUtil.sendEvent(new ActivitySearchEvent(response.data));
                    }
                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    private void doGetUserById(final int callingId, int user_id){
        mApiClient.userService()
                .getUserById(user_id,mToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse<User>>() {
                    @Override
                    public void onResponse(ApiResponse<User> response) {
                        User user = response.data;
                        findUi(callingId).userCallBack(user);
                        EventUtil.sendEvent(new UserSearchEvent(user));
                    }
                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                        EventUtil.sendEvent(new UserSearchEvent(null));
                    }
                });
    }

    public interface SearchUiCallbacks{
        void getActivityListByIdAndState(int activity_id, String state);
        void getUserById(int user_id);
    }

    public interface SearchUi extends BaseController.Ui<SearchUiCallbacks>{
       void activityListCallback(List<Activity> activities);
       void userCallBack(User user);
    }

}
