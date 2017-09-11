package com.playmala.playmala.model.event;

import com.playmala.playmala.model.request.MainActivityListParams;

/**
 * author：zhangguobing on 2017/7/19 09:45
 * email：bing901222@qq.com
 */

public class MainActivityListFilterEvent {
    public MainActivityListParams params;

    public MainActivityListFilterEvent(MainActivityListParams params) {
        this.params = params;
    }
}
