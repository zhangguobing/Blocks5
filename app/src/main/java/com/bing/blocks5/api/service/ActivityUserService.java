package com.bing.blocks5.api.service;

import com.bing.blocks5.api.ApiResponse;
import com.bing.blocks5.model.Activity;
import com.bing.blocks5.model.ActivityUser;
import com.bing.blocks5.model.User;

import java.util.List;

import retrofit2.http.GET;
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

    //获取当前活动报名的用户
    @GET("activities/{activityId}/users")
    Observable<ApiResponse<List<ActivityUser>>> getUsersByActivityId(@Path("activityId") int activityId, @Query("token") String token,
                                                                     @Query("state") int state, @Query("page_index") int page_index,
                                                                     @Query("is_sign") int is_sign, @Query("sex") String sex, @Query("page_size") String page_size);
    //报名参加某个活动
    @POST("activities/{activityId}/users")
    Observable<ApiResponse> joinActivity(@Path("activityId") int activity_id, @Query("token") String token);

    /**
     * 获取当前用户的浏览历史（收藏记录）
     * @param collect 是否收藏。0为历史，1为收藏。默认历史。
     * @param page_index 页码，从1开始，默认1
     * @param page_size 每页大小，默认10
     * @return
     */
    @GET("histories/activities")
    Observable<ApiResponse<List<Activity>>> getHistoryOrCollectActivity(@Query("token") String token, @Query("is_collect") int collect,
                                                                  @Query("page_index") int page_index, @Query("page_size") int page_size);

    /**
     * 获取某用户关注的（或被关注的）用户列表
     * @param token
     * @param follow_type 类型：0 为我关注，1为关注我的。默认0
     * @param page_index 页码，从1开始，默认1
     * @param page_size 每页数量，默认10
     * @param user_id 需要查看的用户，不填则默认为当前用户
     * @return
     */
    @GET("follows")
    Observable<ApiResponse<List<User>>> getFollowers(@Query("token") String token, @Query("follow_type") int follow_type,
                                             @Query("page_index") int page_index,@Query("page_size") int page_size,
                                             @Query("user_id") int user_id);
}
