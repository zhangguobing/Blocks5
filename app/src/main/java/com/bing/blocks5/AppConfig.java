package com.bing.blocks5;

/**
 * author：ZhangGuoBing on 2017/5/27 11:14
 * email：bing901222@qq.com
 */


public class AppConfig {

    /**
     * 是否是debug模式
     */
    public static final boolean DEBUG = BuildConfig.DEBUG;
    /**
     * 服务器地址
     */
    public static final String BASE_URL = "http://112.74.18.189/api-t/";

    /**
     * 数据库名称
     */
    public static final String DB_NAME = "blocks5.db";

    /**
     * 连接超时时间
     */
    public static final int CONNECT_TIMEOUT_MILLIS = 15 * 1000; // 15s

    /**
     * 响应超时时间
     */
    public static final int READ_TIMEOUT_MILLIS = 20 * 1000; // 20s


    /**
     * 应用名称
     */
    public static String APP_NAME = Blocks5App.getContext().getString(R.string.app_name);
}
