package com.bing.blocks5.api.service;

import com.bing.blocks5.api.ApiResponse;
import com.bing.blocks5.model.Activity;
import com.bing.blocks5.model.Comment;
import com.bing.blocks5.ui.activity.request.CreateActivityParams;

import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
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

    @FormUrlEncoded
    @POST("histories/activities")
    Observable<ApiResponse> collectActivity(@Field("activity_id") int activity_id,@Query("token") String token);

    //举报活动 rpt_type = 0, rpt_id 活动id
    @FormUrlEncoded
    @POST("reports")
    Observable<ApiResponse> reportActivity(@Query("token") String token,@Field("rpt_type") int rpt_type,
                                           @Field("rpt_id") int rpt_id, @Field("content") String content);

    //开始某个活动
    @GET("activities/{id}/start")
    Observable<ApiResponse> startActivity(@Path("id") int activity_id,@Query("token") String token);

    //活动筛选  sort_type= credit 按信用程度
    @GET("activities")
    Observable<ApiResponse<List<Activity>>> getActivityList(@QueryMap Map<String,String> map);

   //获取活动评论列表
    @GET("activities/{activityId}/comments")
    Observable<ApiResponse<List<Comment>>> getActivityComments(@Path("activityId") String activityId,@QueryMap Map<String,String> map);

    //新增活动评论
    @FormUrlEncoded
    @POST("activities/{activityId}/comments")
    Observable<ApiResponse<Comment>> addActivityComment(@Path("activityId") String activityId,@Query("token") String token, @Field("content") String content, @Field("is_team") int is_team);
}
