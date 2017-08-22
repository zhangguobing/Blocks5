package com.bing.blocks5.api.service;

import com.bing.blocks5.api.ApiResponse;
import com.bing.blocks5.model.FeedBack;
import com.bing.blocks5.model.User;

import java.util.List;

import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * author：zhangguobing on 2017/6/20 17:38
 * email：bing901222@qq.com
 */

public interface UserService {
    @FormUrlEncoded
    @POST("user/register")
    Observable<ApiResponse> register(@Query("token") String token, @Field("nick_name") String nickName,
                                     @Field("sex") String sex,@Field("password") String password,
                                     @Field("avatar") String avatar);
    @FormUrlEncoded
    @POST("user/identity")
    Observable<ApiResponse> identity(@Query("token") String token,@Field("identity_name") String identity_name,@Field("identity_code") String identity_code);

    @GET("users/{id}")
    Observable<ApiResponse<User>> getUserById(@Path("id") int user_id, @Query("token") String token);

    /**
     * 关注某个用户
     * @param token
     * @param follow_id
     * @return
     */
    @FormUrlEncoded
    @POST("follows")
    Observable<ApiResponse> follow(@Query("token") String token, @Field("follow_id") String follow_id);

    /**
     * 取消关注某个用户
     * @param token
     * @param follow_id
     * @return
     */
    @DELETE("follows")
    Observable<ApiResponse> cancelFollow(@Query("token") String token, @Query("follow_id") String follow_id);

    @FormUrlEncoded
    @POST("user/base")
    Observable<ApiResponse<User>> updateUser(@Query("token") String token, @Field("age") int age, @Field("job") String job,
                                                       @Field("addr") String address, @Field("avatar") String avatar, @Field("content") String content,
                                                       @Field("img_url_1") String image_url_1, @Field("img_url_2") String image_url_2,
                                                       @Field("img_url_3") String image_url_3);

    @GET("feedbacks")
    Observable<ApiResponse<List<FeedBack>>> getFeedBack(@Query("token") String token, @Query("page_index") int page_index,@Query("page_size") String page_size);

    @FormUrlEncoded
    @POST("feedbacks")
    Observable<ApiResponse<FeedBack>> addFeedBack(@Query("token") String token,@Field("content") String content);
}
