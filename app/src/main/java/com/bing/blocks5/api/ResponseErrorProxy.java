package com.bing.blocks5.api;

import com.bing.blocks5.AppCookie;
import com.bing.blocks5.model.Token;
import com.google.gson.JsonParseException;
import com.orhanobut.logger.Logger;
import com.bing.blocks5.AppConfig;
import com.bing.blocks5.R;
import com.bing.blocks5.util.GsonHelper;
import com.bing.blocks5.util.StringFetcher;

import org.apache.http.conn.ConnectTimeoutException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.HttpException;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.bing.blocks5.Constants.HttpCode.HTTP_UNKNOWN_ERROR;
import static com.bing.blocks5.Constants.HttpCode.HTTP_NETWORK_ERROR;
import static com.bing.blocks5.Constants.HttpCode.HTTP_SERVER_ERROR;
import static com.bing.blocks5.Constants.HttpCode.HTTP_UNAUTHORIZED;


public class ResponseErrorProxy implements InvocationHandler {


    private Object mProxyObject;
    private ApiClient mApiClient;

    public ResponseErrorProxy(Object proxyObject, ApiClient restApiClient) {
        mProxyObject = proxyObject;
        mApiClient = restApiClient;
    }

    @Override
    public Object invoke(Object proxy, final Method method, final Object[] args) {
        return Observable.just(null)
                .flatMap((Func1<Object, Observable<?>>) o -> {
                    try {
                        return (Observable<?>) method.invoke(mProxyObject, args);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .retryWhen(observable -> observable.flatMap((Func1<Throwable, Observable<?>>) throwable -> {
                    ResponseError error;
                    if (throwable instanceof ConnectTimeoutException
                            || throwable instanceof SocketTimeoutException
                            || throwable instanceof UnknownHostException
                            || throwable instanceof ConnectException) {
                        error = new ResponseError(HTTP_NETWORK_ERROR,
                                StringFetcher.getString(R.string.toast_error_network));
                    } else if (throwable instanceof HttpException) {
                        HttpException exception = (HttpException) throwable;
                        try {
                            error = GsonHelper.builderGson().fromJson(
                                    exception.response().errorBody().string(), ResponseError.class);
                        } catch (Exception e) {
                            if (e instanceof JsonParseException) {
                                error = new ResponseError(HTTP_SERVER_ERROR,
                                        StringFetcher.getString(R.string.toast_error_server));
                            } else {
                                error = new ResponseError(HTTP_UNKNOWN_ERROR,
                                        StringFetcher.getString(R.string.toast_error_unknown));
                            }
                        }
                    } else if (throwable instanceof JsonParseException) {
                        error = new ResponseError(HTTP_SERVER_ERROR,
                                StringFetcher.getString(R.string.toast_error_server));
                    } else if(throwable instanceof ResponseError){
                        error = (ResponseError) throwable;
                    } else {
                        error = new ResponseError(HTTP_UNKNOWN_ERROR,
                                StringFetcher.getString(R.string.toast_error_unknown));
                    }

                    if (AppConfig.DEBUG) {
                        throwable.printStackTrace();
                        Logger.e("网络请求出现错误: " + error.toString());
                    }
                    if (error.getCode() == HTTP_UNAUTHORIZED) {
                        return refreshTokenWhenTokenInvalid();
                    } else {
                        return Observable.error(error);
                    }
                }));
    }

    private Observable<?> refreshTokenWhenTokenInvalid() {
        synchronized (ResponseErrorProxy.class) {
//            Map<String, String> params = new HashMap<>();
//            params.put(PARAM_CLIENT_ID, AppConfig.APP_KEY);
//            params.put(PARAM_CLIENT_SECRET, AppConfig.APP_SECRET);
//            params.put(PARAM_GRANT_TYPE, "refresh_token");
//            params.put(PARAM_REFRESH_TOKEN, AppCookie.getRefreshToken());
//            params.put("token", AppCookie.getUserInfo().getPhone());

            return mApiClient.tokenService()
                    .refreshToken(AppCookie.getUserInfo().getPhone())
                    .doOnNext(response -> {
//                            AppCookie.saveAccessToken(token.getAccessToken());
//                            AppCookie.saveRefreshToken(token.getRefreshToken());
                        if(response.data != null) AppCookie.saveToken(response.data.getToken());
//                            mRestApiClient.setToken(token.getAccessToken());
                    });
        }
    }
}
