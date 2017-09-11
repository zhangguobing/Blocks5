package com.playmala.playmala.model.event;

import com.playmala.playmala.model.User;

/**
 * author：zhangguobing on 2017/7/2 14:05
 * email：bing901222@qq.com
 */

public class UserSearchEvent {
    public User user;

    public UserSearchEvent(User user) {
        this.user = user;
    }
}
