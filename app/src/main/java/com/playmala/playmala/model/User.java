package com.playmala.playmala.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author：zhangguobing on 2017/7/2 15:08
 * email：bing901222@qq.com
 */

public class User implements Parcelable {


    /**
     * id : 1
     * phone : 13148839309
     * nick_name : 不小不大
     * sex : 男
     * age : 0
     * job :
     * addr :
     * avatar :
     * content :
     * credit : 100
     * follow_num : 0
     * followed_num : 0
     * identity_state : 0
     * identity_name :
     * identity_code :
     * logined_at : 2017-04-20 00:09:34
     * created_at : 2017-04-12 09:33:56
     * img_url_1 :
     * img_url_2 :
     * img_url_3 :
     * is_follow : 0
     */

    private int id;
    private String phone;
    private String nick_name;
    private String sex;
    private int age;
    private String job;
    private String addr;
    private String avatar;
    private String content;
    private int credit;
    private int follow_num;
    private int followed_num;
    private int identity_state;
    private String identity_name;
    private String identity_code;
    private String logined_at;
    private String created_at;
    private String img_url_1;
    private String img_url_2;
    private String img_url_3;
    private int is_follow;

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

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
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

    public int getFollow_num() {
        return follow_num;
    }

    public void setFollow_num(int follow_num) {
        this.follow_num = follow_num;
    }

    public int getFollowed_num() {
        return followed_num;
    }

    public void setFollowed_num(int followed_num) {
        this.followed_num = followed_num;
    }

    public int getIdentity_state() {
        return identity_state;
    }

    public void setIdentity_state(int identity_state) {
        this.identity_state = identity_state;
    }

    public String getIdentity_name() {
        return identity_name;
    }

    public void setIdentity_name(String identity_name) {
        this.identity_name = identity_name;
    }

    public String getIdentity_code() {
        return identity_code;
    }

    public void setIdentity_code(String identity_code) {
        this.identity_code = identity_code;
    }

    public String getLogined_at() {
        return logined_at;
    }

    public void setLogined_at(String logined_at) {
        this.logined_at = logined_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getImg_url_1() {
        return img_url_1;
    }

    public void setImg_url_1(String img_url_1) {
        this.img_url_1 = img_url_1;
    }

    public String getImg_url_2() {
        return img_url_2;
    }

    public void setImg_url_2(String img_url_2) {
        this.img_url_2 = img_url_2;
    }

    public String getImg_url_3() {
        return img_url_3;
    }

    public void setImg_url_3(String img_url_3) {
        this.img_url_3 = img_url_3;
    }

    public int getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(int is_follow) {
        this.is_follow = is_follow;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.phone);
        dest.writeString(this.nick_name);
        dest.writeString(this.sex);
        dest.writeInt(this.age);
        dest.writeString(this.job);
        dest.writeString(this.addr);
        dest.writeString(this.avatar);
        dest.writeString(this.content);
        dest.writeInt(this.credit);
        dest.writeInt(this.follow_num);
        dest.writeInt(this.followed_num);
        dest.writeInt(this.identity_state);
        dest.writeString(this.identity_name);
        dest.writeString(this.identity_code);
        dest.writeString(this.logined_at);
        dest.writeString(this.created_at);
        dest.writeString(this.img_url_1);
        dest.writeString(this.img_url_2);
        dest.writeString(this.img_url_3);
        dest.writeInt(this.is_follow);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.id = in.readInt();
        this.phone = in.readString();
        this.nick_name = in.readString();
        this.sex = in.readString();
        this.age = in.readInt();
        this.job = in.readString();
        this.addr = in.readString();
        this.avatar = in.readString();
        this.content = in.readString();
        this.credit = in.readInt();
        this.follow_num = in.readInt();
        this.followed_num = in.readInt();
        this.identity_state = in.readInt();
        this.identity_name = in.readString();
        this.identity_code = in.readString();
        this.logined_at = in.readString();
        this.created_at = in.readString();
        this.img_url_1 = in.readString();
        this.img_url_2 = in.readString();
        this.img_url_3 = in.readString();
        this.is_follow = in.readInt();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

}
