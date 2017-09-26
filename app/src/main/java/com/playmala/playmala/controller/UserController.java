package com.playmala.playmala.controller;

import android.text.TextUtils;

import com.playmala.playmala.AppCookie;
import com.playmala.playmala.api.ApiResponse;
import com.playmala.playmala.api.RequestCallback;
import com.playmala.playmala.api.ResponseError;
import com.playmala.playmala.base.BaseController;
import com.playmala.playmala.model.FeedBack;
import com.playmala.playmala.model.UploadToken;
import com.playmala.playmala.model.User;
import com.playmala.playmala.model.event.UserInfoChangeEvent;
import com.playmala.playmala.util.EventUtil;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author：zhangguobing on 2017/6/20 17:30
 * email：bing901222@qq.com
 */

public class UserController extends BaseController<UserController.UserUi,UserController.UserUiCallbacks> {

    @Override
    protected UserUiCallbacks createUiCallbacks(UserUi ui) {
        return new UserUiCallbacks() {
            @Override
            public void register(String token,String nickName,String sex,String password,String avatar) {
                if(TextUtils.isEmpty(avatar)){
                    doRegister(getId(ui),token,nickName,sex,password);
                }else{
                    doRegister(getId(ui),token,nickName,sex,password,avatar);
                }

            }

            @Override
            public void identity(String identityName, String identityCode) {
                doIdentity(getId(ui),identityName,identityCode);
            }

            @Override
            public void follow(String follow_id) {
                doFollow(getId(ui), follow_id);
            }

            @Override
            public void cancelFollow(String follow_id) {
                doCancelFollow(getId(ui), follow_id);
            }

            @Override
            public void getUserById(int user_id) {
                doGetUserById(getId(ui), user_id);
            }

            @Override
            public void updateUser(int age, String job, String address, String avatar,
                                   String content, String image_url_1, String image_url_2,
                                   String image_url_3,String nick_name) {
                doUpdateUser(getId(ui),age,job,address,avatar,content,image_url_1,image_url_2,image_url_3,nick_name);
            }

            @Override
            public void getFeedBack(int page_index) {
                doGetFeedBack(getId(ui), page_index);
            }

            @Override
            public void addFeedBack(String content) {
                doAddFeedBack(getId(ui), content);
            }
            @Override
            public void getUploadToken() {
                doGetUploadToken(getId(ui));
            }
        };
    }

    private void doRegister(final int callingId,String token,String nickName,String sex,String password,String avatar){
        mApiClient.userService()
                .register(token, nickName, sex, password, avatar)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse response) {
                        UserUi ui = findUi(callingId);
                        if(ui instanceof LoginNextUi){
                            ((LoginNextUi)ui).registerSuccess(response.message);
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    private void doRegister(final int callingId,String token,String nickName,String sex,String password){
        mApiClient.userService()
                .register(token, nickName, sex, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse response) {
                        UserUi ui = findUi(callingId);
                        if(ui instanceof LoginNextUi){
                            ((LoginNextUi)ui).registerSuccess(response.message);
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    private void doIdentity(final int callingId,String identityName,String identityCode){
        mApiClient.userService()
                .identity(mToken, identityName, identityCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse response) {
                        UserUi ui = findUi(callingId);
                        if(ui instanceof IdentityUi){
                            ((IdentityUi)ui).identitySuccess(response.message);
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    private void doFollow(final int callingId,String follow_id){
        mApiClient.userService()
                .follow(mToken, follow_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse response) {
                        UserUi ui = findUi(callingId);
                        if(ui instanceof UserListUi){
                            ((UserListUi)ui).followSuccess();
                        }else if(ui instanceof UserDetailUi){
                            ((UserDetailUi)ui).followSuccess();
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    //取消关注
    private void doCancelFollow(final int callingId,String follow_id){
        mApiClient.userService()
                .cancelFollow(mToken, follow_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse response) {
                        UserUi ui = findUi(callingId);
                        if(ui instanceof UserDetailUi){
                            ((UserDetailUi)ui).cancelFollowSuccess();
                        }else if(ui instanceof UserListUi){
                            ((UserListUi)ui).cancelFollowSuccess();
                        }
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
                        UserUi userUi = findUi(callingId);
                        if(userUi instanceof ProfileUi){
                            ((ProfileUi)userUi).loadUserCallback(response.data);
                        }else if(userUi instanceof UserDetailUi){
                            ((UserDetailUi)userUi).loadUserCallback(response.data);
                        }else if(userUi instanceof SettingUi){
                            ((SettingUi)userUi).loadUserCallback(response.data);
                        }

                    }
                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    /**
     * 修改当前用户信息
     */
    private void doUpdateUser(final int callingId, int age, String job,String address,
                              String avatar,String content,String image_url_1,
                              String image_url_2,String image_url_3,String nick_name){
        mApiClient.userService()
                .updateUser(mToken,age,job,address,avatar,content,image_url_1,image_url_2,image_url_3,nick_name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse<User>>() {
                    @Override
                    public void onResponse(ApiResponse<User> response) {
                        UserUi userUi = findUi(callingId);
                        if(userUi instanceof ProfileUi){
                            User user = response.data;
                            ((ProfileUi)userUi).updateUserCallback(user);
                            EventUtil.sendEvent(new UserInfoChangeEvent(user));
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    private void doGetFeedBack(final int callingId,int page_index){
        mApiClient.userService()
                .getFeedBack(mToken, page_index, "15")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse<List<FeedBack>>>() {
                    @Override
                    public void onResponse(ApiResponse<List<FeedBack>> response) {
                        UserUi ui = findUi(callingId);
                        if(ui instanceof FeedBackUi){
                            ((FeedBackUi)ui).loadFeedBackSuccess(response.data);
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    private void doAddFeedBack(final int callingId,String content){
        mApiClient.userService()
                .addFeedBack(mToken, content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse<FeedBack>>() {
                    @Override
                    public void onResponse(ApiResponse<FeedBack> response) {
                        UserUi ui = findUi(callingId);
                        if(ui instanceof FeedBackUi){
                            ((FeedBackUi)ui).addFeedBackSuccess(response.data);
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        UserUi ui = findUi(callingId);
                        if(ui instanceof FeedBackUi){
                            ((FeedBackUi)ui).addFeedBackFail(error.getMessage());
                        }
                    }
                });
    }

    private void doGetUploadToken(final int callingId){
        mApiClient.loginAuthService()
                .getUploadToken(mToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse<UploadToken>>() {
                    @Override
                    public void onResponse(ApiResponse<UploadToken> response) {
                        if(response.data != null){
                            AppCookie.saveUploadToken(response.data.getToken());
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    public interface UserUi extends BaseController.Ui<UserController.UserUiCallbacks>{

    }

    public interface LoginNextUi extends UserUi{
        void registerSuccess(String message);
    }

    public interface IdentityUi extends UserUi{
        void identitySuccess(String msg);
    }

    public interface UserListUi extends UserUi{
        void followSuccess();
        void cancelFollowSuccess();
    }

    public interface UserDetailUi extends UserUi{
        void followSuccess();
        void cancelFollowSuccess();
        void loadUserCallback(User user);
    }

    public interface ProfileUi extends UserUi{
        void loadUserCallback(User user);
        void updateUserCallback(User user);
    }

    public interface SettingUi extends UserUi{
        void loadUserCallback(User user);
    }

    public interface FeedBackUi extends UserUi{
        void loadFeedBackSuccess(List<FeedBack> feedBacks);
        void addFeedBackSuccess(FeedBack feedBack);
        void addFeedBackFail(String msg);
    }

    public interface UserUiCallbacks{
        void register(String token,String nickName,String sex,String password,String avatar);
        void identity(String identityName,String identityCode);
        void follow(String follow_id);
        void cancelFollow(String follow_id);
        void getUserById(int user_id);
        void updateUser(int age, String job,String address,
                        String avatar,String content,String image_url_1,
                        String image_url_2,String image_url_3,String nick_name);
        void getFeedBack(int page_index);
        void addFeedBack(String content);
        void getUploadToken();
    }


}
