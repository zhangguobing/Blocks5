package com.bing.blocks5.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author：zhangguobing on 2017/7/20 17:47
 * email：bing901222@qq.com
 */

public class Comment implements Parcelable {
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

    public static class CreatorBean implements Parcelable {

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.nick_name);
            dest.writeString(this.sex);
            dest.writeString(this.avatar);
            dest.writeInt(this.credit);
            dest.writeInt(this.identity_state);
        }

        public CreatorBean() {
        }

        protected CreatorBean(Parcel in) {
            this.id = in.readInt();
            this.nick_name = in.readString();
            this.sex = in.readString();
            this.avatar = in.readString();
            this.credit = in.readInt();
            this.identity_state = in.readInt();
        }

        public static final Creator<CreatorBean> CREATOR = new Creator<CreatorBean>() {
            @Override
            public CreatorBean createFromParcel(Parcel source) {
                return new CreatorBean(source);
            }

            @Override
            public CreatorBean[] newArray(int size) {
                return new CreatorBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.activity_id);
        dest.writeInt(this.user_id);
        dest.writeString(this.content);
        dest.writeInt(this.is_team);
        dest.writeString(this.created_at);
        dest.writeParcelable(this.creator, flags);
        dest.writeInt(this.send_state);
    }

    public Comment() {
    }

    protected Comment(Parcel in) {
        this.id = in.readInt();
        this.activity_id = in.readInt();
        this.user_id = in.readInt();
        this.content = in.readString();
        this.is_team = in.readInt();
        this.created_at = in.readString();
        this.creator = in.readParcelable(CreatorBean.class.getClassLoader());
        this.send_state = in.readInt();
    }

    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
