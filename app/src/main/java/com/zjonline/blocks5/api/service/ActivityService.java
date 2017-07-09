package com.zjonline.blocks5.api.service;

import com.zjonline.blocks5.api.ApiResponse;
import com.zjonline.blocks5.model.Activity;
import com.zjonline.blocks5.ui.activity.request.CreateActivityParams;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * author：zhangguobing on 2017/7/1 16:21
 * email：bing901222@qq.com
 */

public interface ActivityService {
    @POST("activities")
    Observable<ApiResponse> createActivity(@Query("token") String token, @Body CreateActivityParams params);

    @GET("activities")
    Observable<ApiResponse<List<Activity>>> getActivityListByActivityId(@Query("token") String token, @Query("id") int activity_id);

    @GET("activities")
    Observable<ApiResponse<List<Activity>>> getActivityList(@Query("token") String token, @Query("id") int activity_type_id,@Query("page_index") int page_index);

    @GET("activities/{id}")
    Observable<ApiResponse<Activity>> getActivityById(@Path("id") int id, @Query("token") String token);

    @GET("activities")
    Observable<ApiResponse<List<Activity>>> getActivityList(@Query("token") String token, @Query("user_id") int user_id,
                                                             @Query("state") int state, @Query("page_index") int page_index);

    @GET("activities")
    Observable<ApiResponse<List<Activity>>> getActivityListByJoinIdAndState(@Query("token") String token, @Query("join_user_id") int join_user_id,
                                                                            @Query("state") int state, @Query("page_index") int page_index);

    @GET("activities")
    Observable<ApiResponse<List<Activity>>> getActivityListByUserId(@Query("token") String token, @Query("user_id") int user_id, @Query("page_index") int page_index);

    @GET("activities")
    Observable<ApiResponse<List<Activity>>> getActivityListByJoinId(@Query("token") String token, @Query("join_user_id") int join_user_id, @Query("page_index") int page_index);

}
