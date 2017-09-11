package com.playmala.playmala.model;

/**
 * author：zhangguobing on 2017/7/10 11:00
 * email：bing901222@qq.com
 */

public class ActivityUser {

    private int id;
    private int activity_id;
    private int user_id;
    private int state;
    private String is_sign;
    private String signed_at;
    private String created_at;
    private CreatorBean creator;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getIs_sign() {
        return is_sign;
    }

    public void setIs_sign(String is_sign) {
        this.is_sign = is_sign;
    }

    public String getSigned_at() {
        return signed_at;
    }

    public void setSigned_at(String signed_at) {
        this.signed_at = signed_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public CreatorBean getCreator() {
        return creator;
    }

    public void setCreator(CreatorBean creator) {
        this.creator = creator;
    }

    public static class CreatorBean {

        private int id;
        private String nick_name;
        private String sex;
        private String avatar;
        private int credit;
        private int identity_state;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getCredit() {
            return credit;
        }

        public void setCredit(int credit) {
            this.credit = credit;
        }

        public int getIdentity_state() {
            return identity_state;
        }

        public void setIdentity_state(int identity_state) {
            this.identity_state = identity_state;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActivityUser that = (ActivityUser) o;

        if (id != that.id) return false;
        if (activity_id != that.activity_id) return false;
        if (user_id != that.user_id) return false;
        if (state != that.state) return false;
        if (is_sign != null ? !is_sign.equals(that.is_sign) : that.is_sign != null) return false;
        if (signed_at != null ? !signed_at.equals(that.signed_at) : that.signed_at != null)
            return false;
        if (created_at != null ? !created_at.equals(that.created_at) : that.created_at != null)
            return false;
        return creator != null ? creator.equals(that.creator) : that.creator == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + activity_id;
        result = 31 * result + user_id;
        result = 31 * result + state;
        result = 31 * result + (is_sign != null ? is_sign.hashCode() : 0);
        result = 31 * result + (signed_at != null ? signed_at.hashCode() : 0);
        result = 31 * result + (created_at != null ? created_at.hashCode() : 0);
        result = 31 * result + (creator != null ? creator.hashCode() : 0);
        return result;
    }
}
