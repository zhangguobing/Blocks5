package com.bing.blocks5.model.event;

/**
 * Created by tian on 2017/8/31.
 */

public class ActivityNoticeUpdateEvent {
    public String notice_type;
    public String notice_content;

    public ActivityNoticeUpdateEvent(String notice_type, String notice_content) {
        this.notice_type = notice_type;
        this.notice_content = notice_content;
    }
}
