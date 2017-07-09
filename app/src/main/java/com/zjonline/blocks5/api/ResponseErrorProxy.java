package com.zjonline.blocks5.api;

import com.google.gson.JsonParseException;
import com.orhanobut.logger.Logger;
import com.zjonline.blocks5.AppConfig;
import com.zjonline.blocks5.R;
import com.zjonline.blocks5.util.GsonHelper;
import com.zjonline.blocks5.util.StringFetcher;

import org.apache.http.conn.ConnectTimeoutException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;
import rx.Observable;
import rx.functions.Func1;

import static com.zjonline.blocks5.Constants.HttpCode.HTTP_UNKNOWN_ERROR;
import static com.zjonline.blocks5.Constants.HttpCode.HTTP_NETWORK_ERROR;
import static com.zjonline.blocks5.Constants.HttpCode.HTTP_SERVER_ERROR;


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
                        error.printStackTrace();
                        Logger.e("网络请求出现错误: " + error.toString());
                    }

                    return Observable.error(error);

//                                if (error.getCode() == HTTP_UNAUTHORIZED) {
//                                    return refreshTokenWhenTokenInvalid();
//                                } else {
//                                    return Observable.error(error);
//                                }
                }));
    }

//    private Observable<?> refreshTokenWhenTokenInvalid() {
//        synchronized (ResponseErrorProxy.class) {
//            Map<String, String> params = new HashMap<>();
//            params.put(PARAM_CLIENT_ID, AppConfig.APP_KEY);
//            params.put(PARAM_CLIENT_SECRET, AppConfig.APP_SECRET);
//            params.put(PARAM_GRANT_TYPE, "refresh_token");
//            params.put(PARAM_REFRESH_TOKEN, AppCookie.getRefreshToken());
//
//            return mRestApiClient.tokenService()
//                    .refreshToken(params)
//                    .doOnNext(new Action1<Token>() {
//                        @Override
//                        public void call(Token token) {
//                            AppCookie.saveAccessToken(token.getAccessToken());
//                            AppCookie.saveRefreshToken(token.getRefreshToken());
//                            mRestApiClient.setToken(token.getAccessToken());
//                        }
//                    });
//        }
//    }
}
