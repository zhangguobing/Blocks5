package com.playmala.playmala.model.event;

import com.playmala.playmala.model.LoginBean;

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
