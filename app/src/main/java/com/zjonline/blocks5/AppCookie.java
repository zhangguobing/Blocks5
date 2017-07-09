package com.zjonline.blocks5;

import android.text.TextUtils;

import com.zjonline.blocks5.model.LoginBean;
import com.zjonline.blocks5.util.PreferenceUtil;

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
}
