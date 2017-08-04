package com.bing.blocks5;

import android.text.TextUtils;

import com.bing.blocks5.model.LoginBean;
import com.bing.blocks5.util.PreferenceUtil;

/**
 * Created by zhangguobing on 2017/4/21.
 */
public class AppCookie {

    public static boolean isLoggin() {
        return getUserInfo() != null && !TextUtils.isEmpty(getToken());
    }

    /**
     * 保存用户信息
     */
    public static void saveUserInfo(LoginBean.User user) {
        PreferenceUtil.set(Constants.Persistence.USER_INFO, user);
    }

    /**
     * 获取用户信息
     * @return
     */
    public static LoginBean.User getUserInfo() {
        return PreferenceUtil.getObject(Constants.Persistence.USER_INFO, LoginBean.User.class);
    }

    /**
     * 保存最后一次登录的手机号
     * @param phone
     */
    public static void saveLastPhone(String phone) {
        PreferenceUtil.set(Constants.Persistence.LAST_LOGIN_PHONE, phone);
    }

    /**
     * 获取最后一次登录的手机号
     * @return
     */
    public static String getLastPhone() {
        return PreferenceUtil.getString(Constants.Persistence.LAST_LOGIN_PHONE, "");
    }

    public static void saveToken(String token){
        PreferenceUtil.set(Constants.Persistence.TOKEN, token);
    }

    public static String getToken(){
        return PreferenceUtil.getString(Constants.Persistence.TOKEN, null);
    }

    public static void saveUploadToken(String uploadToken){
        PreferenceUtil.set(Constants.Persistence.UPLOAD_TOKEN, uploadToken);
    }

    public static String getUploadToken(){
        return PreferenceUtil.getString(Constants.Persistence.UPLOAD_TOKEN, null);
    }

    /**
     * 保存当前的城市
     * @param city
     */
    public static void saveCity(String city){
        PreferenceUtil.set(Constants.Persistence.CITY, city);
    }

    /**
     * 获取当前的城市
     * @return
     */
    public static String getCity(){
        return PreferenceUtil.getString(Constants.Persistence.CITY,AppConfig.DEFAULT_CITY);
    }
}
