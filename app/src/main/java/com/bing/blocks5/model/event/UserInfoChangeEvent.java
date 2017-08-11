package com.bing.blocks5.model.event;

import com.bing.blocks5.model.User;

/**
 * Created by tian on 2017/8/11.
 */

public class UserInfoChangeEvent {
    public User user;

    public UserInfoChangeEvent(User user) {
        this.user = user;
    }
}
