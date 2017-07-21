package com.bing.blocks5.model;

/**
 * author：zhangguobing on 2017/7/20 17:47
 * email：bing901222@qq.com
 */

public class Comment {
    private int id;
    private int activity_id;
    private int user_id;
    private String content;
    private int is_team;
    private String created_at;
    private CreatorBean creator;
    private int send_state;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (id != comment.id) return false;
        if (activity_id != comment.activity_id) return false;
        if (user_id != comment.user_id) return false;
        if (is_team != comment.is_team) return false;
        if (content != null ? !content.equals(comment.content) : comment.content != null)
            return false;
        if (created_at != null ? !created_at.equals(comment.created_at) : comment.created_at != null)
            return false;
        return creator != null ? creator.equals(comment.creator) : comment.creator == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + activity_id;
        result = 31 * result + user_id;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + is_team;
        result = 31 * result + (created_at != null ? created_at.hashCode() : 0);
        result = 31 * result + (creator != null ? creator.hashCode() : 0);
        return result;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIs_team() {
        return is_team;
    }

    public void setIs_team(int is_team) {
        this.is_team = is_team;
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

    public int getSend_state() {
        return send_state;
    }

    public void setSend_state(int send_state) {
        this.send_state = send_state;
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

}
