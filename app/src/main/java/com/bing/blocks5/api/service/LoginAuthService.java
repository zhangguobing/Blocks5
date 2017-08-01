package com.bing.blocks5.api.service;

import com.bing.blocks5.api.ApiResponse;
import com.bing.blocks5.model.Config;
import com.bing.blocks5.model.LoginBean;
import com.bing.blocks5.model.UploadToken;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * author：zhangguobing on 2017/6/19 16:35
 * email：bing901222@qq.com
 */

public interface LoginAuthService {
    @FormUrlEncoded
    @POST("login")
    Observable<ApiResponse<LoginBean>> login(@Field("phone") String phone, @Field("captcha") String captcha);
    @GET("captcha")
    Observable<ApiResponse> captcha(@Query("phone") String phone,@Query("type") String type);
    @GET("forget")
    Observable<ApiResponse> forget(@Query("phone") String phone);
    @FormUrlEncoded
    @POST("resetPassword")
    Observable<ApiResponse> resetPassword(@Field("phone") String phone, @Field("captcha") String captcha, @Field("password") String password);
    @GET("configure")
    Observable<ApiResponse<Config>> config(@Query("token") String token);
    @GET("getUploadToken")
    Observable<ApiResponse<UploadToken>> getUploadToken(@Query("token") String token);
}
