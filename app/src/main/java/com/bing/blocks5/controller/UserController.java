package com.bing.blocks5.controller;

import com.bing.blocks5.api.ApiResponse;
import com.bing.blocks5.api.RequestCallback;
import com.bing.blocks5.api.ResponseError;
import com.bing.blocks5.base.BasePresenter;
import com.bing.blocks5.model.User;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author：zhangguobing on 2017/6/20 17:30
 * email：bing901222@qq.com
 */

public class UserController extends BasePresenter<UserController.UserUi,UserController.UserUiCallbacks> {

    @Override
    protected UserUiCallbacks createUiCallbacks(UserUi ui) {
        return new UserUiCallbacks() {
            @Override
            public void register(String token,String nickName,String sex,String password,String avatar) {
                doRegister(getId(ui),token,nickName,sex,password,avatar);
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
            public void getUserById(int user_id) {
                doGetUserById(getId(ui), user_id);
            }

            @Override
            public void updateUser(int age, String job, String address, String avatar, String content, String image_url_1, String image_url_2, String image_url_3) {
                doUpdateUser(getId(ui),age,job,address,avatar,content,image_url_1,image_url_2,image_url_3);
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
                              String image_url_2,String image_url_3){
        mApiClient.userService()
                .updateUser(mToken,age,job,address,avatar,content,image_url_1,image_url_2,image_url_3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse<User>>() {
                    @Override
                    public void onResponse(ApiResponse<User> response) {
                        UserUi userUi = findUi(callingId);
                        if(userUi instanceof ProfileUi){
                            ((ProfileUi)userUi).updateUserCallback(response.data);
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    public interface UserUi extends BasePresenter.Ui<UserController.UserUiCallbacks>{

    }

    public interface LoginNextUi extends UserUi{
        void registerSuccess(String message);
    }

    public interface IdentityUi extends UserUi{
        void identitySuccess(String msg);
    }

    public interface UserListUi extends UserUi{
        void followSuccess();
    }

    public interface UserDetailUi extends UserUi{
        void followSuccess();
        void loadUserCallback(User user);
    }

    public interface ProfileUi extends UserUi{
        void loadUserCallback(User user);
        void updateUserCallback(User user);
    }

    public interface SettingUi extends UserUi{
        void loadUserCallback(User user);
    }

    public interface UserUiCallbacks{
        void register(String token,String nickName,String sex,String password,String avatar);
        void identity(String identityName,String identityCode);
        void follow(String follow_id);
        void getUserById(int user_id);
        void updateUser(int age, String job,String address,
                        String avatar,String content,String image_url_1,
                        String image_url_2,String image_url_3);
    }


}
