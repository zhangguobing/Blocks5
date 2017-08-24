package com.bing.blocks5.controller;

import android.text.TextUtils;

import com.bing.blocks5.base.BaseController;
import com.bing.blocks5.model.UploadToken;
import com.bing.blocks5.model.User;
import com.squareup.otto.Subscribe;
import com.bing.blocks5.AppCookie;
import com.bing.blocks5.api.ApiResponse;
import com.bing.blocks5.api.RequestCallback;
import com.bing.blocks5.api.ResponseError;
import com.bing.blocks5.model.Config;
import com.bing.blocks5.model.LoginBean;
import com.bing.blocks5.model.event.UserChangeEvent;
import com.bing.blocks5.repository.ConfigManager;
import com.bing.blocks5.util.EventUtil;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author：zhangguobing on 2017/6/19 11:27
 * email：bing901222@qq.com
 */

public class LoginAuthController extends BaseController<LoginAuthController.LoginAuthUi,LoginAuthController.LoginAuthUiCallbacks> {

    @Override
    protected LoginAuthUiCallbacks createUiCallbacks(final LoginAuthUi ui) {
        return new LoginAuthUiCallbacks() {
            @Override
            public void login(String phone, String captcha) {
                doLogin(getId(ui), phone, captcha);
            }

            @Override
            public void captcha(String phone,String type) {
                doCaptcha(getId(ui), phone, type);
            }

            @Override
            public void forget(String phone) {
                doForget(getId(ui), phone);
            }

            @Override
            public void resetPassword(String phone, String captcha, String password) {
                doResetPassword(getId(ui),phone,captcha,password);
            }

            @Override
            public void fetchConfig() {
                doConfig(getId(ui));
            }

            @Override
            public void getUploadToken() {
                doGetUploadToken(getId(ui));
            }

            @Override
            public void getUserById(int user_id) {
                doGetUserById(getId(ui), user_id);
            }
        };
    }

    private void doLogin(final int callingId, String userName, String captcha){
        mApiClient.loginAuthService()
                .login(userName,captcha)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse<LoginBean>>() {
                    @Override
                    public void onResponse(ApiResponse<LoginBean> response) {
                        LoginAuthUi ui = findUi(callingId);
                        if(ui instanceof LoginUi){
                            ((LoginUi) ui).loginFinish(response.data);
                        }
                        // 发送用户账户改变的事件
                        EventUtil.sendEvent(new UserChangeEvent(response.data));
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }


    private void doCaptcha(final int callingId, String phone,String type){
        mApiClient.loginAuthService()
                .captcha(phone,type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse captcha) {
                        LoginAuthUi ui = findUi(callingId);
                        if(ui instanceof LoginUi){
                            ((LoginUi) ui).receiveCaptcha(captcha.message);
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    private void doForget(final int callingId, String phone){
        mApiClient.loginAuthService()
                .forget(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse captcha) {
                        LoginAuthUi ui = findUi(callingId);
                        if(ui instanceof ForgetPwdUi){
                            ((ForgetPwdUi) ui).receiveCaptcha(captcha.message);
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    private void doResetPassword(final int callingId, String phone, String captcha, String password){
        mApiClient.loginAuthService()
                .resetPassword(phone,captcha,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse response) {
                        LoginAuthUi ui = findUi(callingId);
                        if(ui instanceof ForgetPwdUi){
                            ((ForgetPwdUi) ui).resetPasswordComplete(response.message);
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    private void doConfig(final int callingId){
        mApiClient.loginAuthService()
                .config(mToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse<Config>>() {
                    @Override
                    public void onResponse(ApiResponse<Config> response) {
                        LoginAuthUi ui = findUi(callingId);
                        if(ui instanceof HomeUi){
                            ((HomeUi)ui).receiveConfig(response.data);
                            ConfigManager.getInstance().saveOrUpdateConfig(response.data);
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
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

    private void doGetUserById(final int callingId, int user_id){
        mApiClient.userService()
                .getUserById(user_id,mToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<ApiResponse<User>>() {
                    @Override
                    public void onResponse(ApiResponse<User> response) {
                        LoginAuthUi ui = findUi(callingId);
                        if(ui instanceof HomeUi) {
                            ((HomeUi)(ui)).receiveUser(response.data);
                        }
                    }
                    @Override
                    public void onFailure(ResponseError error) {
                        findUi(callingId).onResponseError(error);
                    }
                });
    }

    public interface LoginAuthUiCallbacks {
        void login(String phone,String captcha);
        void captcha(String phone,String type);
        void forget(String phone);
        void resetPassword(String phone,String captcha,String password);
        void fetchConfig();
        void getUploadToken();
        void getUserById(int user_id);
    }

    public interface LoginAuthUi extends BaseController.Ui<LoginAuthUiCallbacks>{

    }
    public interface LoginUi extends LoginAuthUi {
        void loginFinish(LoginBean loginBean);
        void receiveCaptcha(String message);
    }

    public interface ForgetPwdUi extends LoginAuthUi {
        void receiveCaptcha(String message);
        void resetPasswordComplete(String message);
    }

    public interface HomeUi extends LoginAuthUi {
        void receiveConfig(Config config);
        void receiveUser(User user);
    }

    @Override
    protected void onInited() {
        super.onInited();
        EventUtil.register(this);
    }

    @Override
    protected void onSuspended() {
        EventUtil.unregister(this);
        super.onSuspended();
    }

    @Subscribe
    public void onUserChanged(UserChangeEvent event) {
        LoginBean loginBean = event.getLoginResult();
        if (loginBean != null && !TextUtils.isEmpty(loginBean.getToken())) {
            AppCookie.saveUserInfo(loginBean.getUser());
            AppCookie.saveLastPhone(loginBean.getUser().getPhone());
            AppCookie.saveToken(loginBean.getToken());
            mApiClient.setToken(loginBean.getToken());
        } else {
            AppCookie.saveUserInfo(null);
            AppCookie.saveToken(null);
            mApiClient.setToken(null);
        }
        populateUis();
    }
}
