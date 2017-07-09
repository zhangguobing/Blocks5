package com.zjonline.blocks5;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.multidex.MultiDex;

import com.aitangba.swipeback.ActivityLifecycleHelper;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.zjonline.blocks5.base.WebActivity;
import com.zjonline.blocks5.ui.activity.ActivityDetailActivity;
import com.zjonline.blocks5.util.GsonHelper;
import com.zjonline.blocks5.util.PreferenceUtil;
import com.zjonline.blocks5.util.ToastUtil;

import java.util.Map;

import cn.campusapp.router.Router;
import cn.campusapp.router.interceptor.Interceptor;
import cn.campusapp.router.router.IActivityRouteTableInitializer;

/**
 * author：ZhangGuoBing on 2017/5/27 11:14
 * email：bing901222@qq.com
 */

public class Blocks5App extends Application{
    private static Blocks5App mInstance;
    private RefWatcher mRefWatcher;
    public AppBarLayout mAppBarLayout;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        MultiDex.install(this);

        // 吐司初始化
        ToastUtil.init(this);

        // 本地存储工具类初始化
        PreferenceUtil.init(this, GsonHelper.builderGson());

        // 日志打印器初始化
        Logger.init(getPackageName()).setLogLevel(AppConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE);

        //swipeback
        registerActivityLifecycleCallbacks(ActivityLifecycleHelper.build());

        // LeakCanary
        mRefWatcher = LeakCanary.install(this);

        //初始化路由
        initRouter();
    }

    public static Blocks5App getContext(){
        return mInstance;
    }

    public RefWatcher getRefWatcher() {
        return mRefWatcher;
    }

    private void initRouter(){
        Router.initActivityRouter(getApplicationContext(),
                router -> router.put("blocksfive://activity/detail", ActivityDetailActivity.class),
                "blocksfive","http","https");
        Router.initBrowserRouter(getApplicationContext());
        Router.setDebugMode(true);
        Router.setInterceptor((context, url) -> {
            if(url.startsWith("http://") || url.startsWith("https://")){
                WebActivity.create(context,url);
                return true;
            }
            return false;
        });
    }

}
