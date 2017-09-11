package com.playmala.playmala.model.event;

import com.playmala.playmala.model.Activity;

import java.util.List;

/**
 * author：zhangguobing on 2017/7/2 14:05
 * email：bing901222@qq.com
 */

public class ActivitySearchEvent {
    public List<Activity> activities;

    public ActivitySearchEvent(List<Activity> activities) {
        this.activities = activities;
    }
}
