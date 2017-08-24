package com.bing.blocks5.model.event;

import com.bing.blocks5.model.User;

/**
 * Created by tian on 2017/8/24.
 */

public class UserInfoRefreshEvent {
    public User user;

    public UserInfoRefreshEvent(User user) {
        this.user = user;
    }
}
