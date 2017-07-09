package com.zjonline.blocks5.model;

import com.google.gson.annotations.SerializedName;

/**
 * author：zhangguobing on 2017/6/19 13:21
 * email：bing901222@qq.com
 */

public class LoginBean {
    @SerializedName("token")
    private String token;
    @SerializedName("expire_at")
    private String expireAt;
    @SerializedName("refresh_expire_at")
    private String refreshExpiredAt;
    private User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(String expireAt) {
        this.expireAt = expireAt;
    }

    public String getRefreshExpiredAt() {
        return refreshExpiredAt;
    }

    public void setRefreshExpiredAt(String refreshExpiredAt) {
        this.refreshExpiredAt = refreshExpiredAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static class User {
        @SerializedName("id")
        public int id;
        @SerializedName("phone")
        public String phone;
        @SerializedName("nick_name")
        public String nickName;
        @SerializedName("sex")
        public String sex;
        @SerializedName("age")
        public int age;
        @SerializedName("job")
        public String job;
        @SerializedName("addr")
        public String addr;
        @SerializedName("avatar")
        public String avatar;
        @SerializedName("content")
        public String content;
        @SerializedName("credit")
        public int credit;
        @SerializedName("follow_num")
        public int followNum;
        @SerializedName("followed_num")
        public int followedNum;
        @SerializedName("identity_state")
        public int identityState;
        @SerializedName("identity_name")
        public String identityName;
        @SerializedName("identity_code")
        public String identityCode;
        @SerializedName("logined_at")
        public String loginedAt;
        @SerializedName("created_at")
        public String createdAt;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getCredit() {
            return credit;
        }

        public void setCredit(int credit) {
            this.credit = credit;
        }

        public int getFollowNum() {
            return followNum;
        }

        public void setFollowNum(int followNum) {
            this.followNum = followNum;
        }

        public int getFollowedNum() {
            return followedNum;
        }

        public void setFollowedNum(int followedNum) {
            this.followedNum = followedNum;
        }

        public int getIdentityState() {
            return identityState;
        }

        public void setIdentityState(int identityState) {
            this.identityState = identityState;
        }

        public String getIdentityName() {
            return identityName;
        }

        public void setIdentityName(String identityName) {
            this.identityName = identityName;
        }

        public String getIdentityCode() {
            return identityCode;
        }

        public void setIdentityCode(String identityCode) {
            this.identityCode = identityCode;
        }

        public String getLoginedAt() {
            return loginedAt;
        }

        public void setLoginedAt(String loginedAt) {
            this.loginedAt = loginedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }
    }


}
