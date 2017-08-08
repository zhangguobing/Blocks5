package com.bing.blocks5.model;

/**
 * author：zhangguobing on 2017/7/20 22:39
 * email：bing901222@qq.com
 */

public class FeedBack {

    /**
     * id : 1
     * user_id : 1
     * is_reply :
     * content : 反馈反馈
     * created_at : 2017-04-12 20:17:21
     */

    private int id;
    private int user_id;
    private int is_reply;
    private String content;
    private String created_at;
    private int send_state;

    public int getSend_state() {
        return send_state;
    }

    public void setSend_state(int send_state) {
        this.send_state = send_state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getIs_reply() {
        return is_reply;
    }

    public void setIs_reply(int is_reply) {
        this.is_reply = is_reply;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
