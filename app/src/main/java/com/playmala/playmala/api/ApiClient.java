package com.playmala.playmala.api;

import com.playmala.playmala.AppConfig;
import com.playmala.playmala.Blocks5App;
import com.playmala.playmala.Constants;
import com.playmala.playmala.api.service.ActivityService;
import com.playmala.playmala.api.service.ActivityUserService;
import com.playmala.playmala.api.service.LoginAuthService;
import com.playmala.playmala.api.service.TokenService;
import com.playmala.playmala.api.service.UserService;
import com.playmala.playmala.util.GsonHelper;

import java.io.File;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.CustomConverterFactory;

/**
 * author：zhangguobing on 2017/6/14 16:45
 * email：bing901222@qq.com
 */

public class ApiClient {
    private Retrofit mRetrofit;
    private String mToken;

    private static ApiClient mApiClient;

    private ApiClient() {
    }

    public static ApiClient getInstance(){
        if(mApiClient == null){
            synchronized (ApiClient.class){
                if(mApiClient == null){
                    mApiClient = new ApiClient();
                }
            }
        }
        return mApiClient;
    }

    public ApiClient setToken(String token) {
        mToken = token;
        mRetrofit = null;
        return this;
    }

    private OkHttpClient newRetrofitClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 设置缓存路径
        File cacheDir = new File(Blocks5App.getContext().getCacheDir(), UUID.randomUUID().toString());
        Cache cache = new Cache(cacheDir, 1024);
        builder.cache(cache);
        // 设置超时时间
        builder.connectTimeout(AppConfig.CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        builder.readTimeout(AppConfig.READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        // 添加请求签名拦截器
//        builder.addInterceptor(new RequestSignInterceptor());
        // 添加请求头
        builder.addInterceptor(chain -> {
            Request.Builder newRequest = chain.request().newBuilder();
            newRequest.addHeader(Constants.Header.HTTP_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
            if (mToken != null) {
                newRequest.addHeader(Constants.Header.TOKEN,mToken);
            }
            return chain.proceed(newRequest.build());
        });
        // 添加OkHttp日志打印器
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(AppConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        builder.addInterceptor(logging);

        return builder.build();
    }

    private Retrofit getRetrofit() {
        if(mRetrofit == null) {
            Retrofit.Builder builder = new Retrofit.Builder();
            builder.client(newRetrofitClient());
            builder.baseUrl(AppConfig.BASE_URL);
            builder.addConverterFactory(CustomConverterFactory.create(GsonHelper.builderGson()));
            builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
            mRetrofit = builder.build();
        }
        return mRetrofit;
    }

    private <T> T get(Class<T> clazz) {
        return getRetrofit().create(clazz);
    }

    @SuppressWarnings("unchecked")
    private <T> T getByProxy(Class<T> clazz) {
        T t = get(clazz);
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz},
                new ResponseErrorProxy(t, this));
    }

    public LoginAuthService loginAuthService(){
        return getByProxy(LoginAuthService.class);
    }

    public UserService userService(){
        return getByProxy(UserService.class);
    }

    public ActivityService activityService(){
        return getByProxy(ActivityService.class);
    }

    public ActivityUserService activityUserService(){
        return getByProxy(ActivityUserService.class);
    }

    public TokenService tokenService(){
        return getByProxy(TokenService.class);
    }
}
