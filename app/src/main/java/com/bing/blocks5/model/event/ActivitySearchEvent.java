package com.bing.blocks5.model.event;

import com.bing.blocks5.model.Activity;

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
