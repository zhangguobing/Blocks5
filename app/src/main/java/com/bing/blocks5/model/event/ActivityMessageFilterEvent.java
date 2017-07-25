package com.bing.blocks5.model.event;

/**
 * author：zhangguobing on 2017/7/25 17:32
 * email：bing901222@qq.com
 */

public class ActivityMessageFilterEvent {
    public boolean is_activity_creator;

    public ActivityMessageFilterEvent(boolean is_activity_creator) {
        this.is_activity_creator = is_activity_creator;
    }
}
