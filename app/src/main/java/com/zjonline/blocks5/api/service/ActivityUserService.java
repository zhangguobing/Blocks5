package com.zjonline.blocks5.api.service;

import com.zjonline.blocks5.api.ApiResponse;

import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * author：zhangguobing on 2017/7/5 10:41
 * email：bing901222@qq.com
 */

public interface ActivityUserService {

    //当前登录用户签到某个活动
    @POST("activities/{activityId}/sign")
    Observable<ApiResponse> sign(@Path("activityId") int activity_id, @Query("token") String token);
}
