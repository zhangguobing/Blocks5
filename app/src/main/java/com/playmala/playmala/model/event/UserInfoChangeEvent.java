package com.playmala.playmala.model.event;

import com.playmala.playmala.model.User;

/**
 * Created by tian on 2017/8/11.
 */

public class UserInfoChangeEvent {
    public User user;

    public UserInfoChangeEvent(User user) {
        this.user = user;
    }
}
