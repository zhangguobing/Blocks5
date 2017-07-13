package com.bing.blocks5.api.service;

import com.bing.blocks5.api.ApiResponse;
import com.bing.blocks5.model.Token;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * author：zhangguobing on 2017/7/13 21:48
 * email：bing901222@qq.com
 */

public interface TokenService {
    @GET("refreshToken")
    Observable<ApiResponse<Token>> refreshToken(@Query("token") String token);
}
