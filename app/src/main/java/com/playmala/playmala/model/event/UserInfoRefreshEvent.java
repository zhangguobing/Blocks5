package com.playmala.playmala.model.event;

import com.playmala.playmala.model.User;

/**
 * Created by tian on 2017/8/24.
 */

public class UserInfoRefreshEvent {
    public User user;

    public UserInfoRefreshEvent(User user) {
        this.user = user;
    }
}
