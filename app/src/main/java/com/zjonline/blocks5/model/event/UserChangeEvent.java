package com.zjonline.blocks5.model.event;

import com.zjonline.blocks5.model.LoginBean;

/**
 * author：zhangguobing on 2017/6/20 10:36
 * email：bing901222@qq.com
 */

public class UserChangeEvent {

    private LoginBean loginBean;

    public UserChangeEvent(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public LoginBean getLoginResult() {
        return loginBean;
    }
}
