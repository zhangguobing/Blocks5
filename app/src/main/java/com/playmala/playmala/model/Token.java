package com.playmala.playmala.model;

/**
 * author：zhangguobing on 2017/7/13 21:46
 * email：bing901222@qq.com
 */

public class Token {
    private String token;
    private String expired_at;
    private String refresh_expired_at;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpired_at() {
        return expired_at;
    }

    public void setExpired_at(String expired_at) {
        this.expired_at = expired_at;
    }

    public String getRefresh_expired_at() {
        return refresh_expired_at;
    }

    public void setRefresh_expired_at(String refresh_expired_at) {
        this.refresh_expired_at = refresh_expired_at;
    }
}
