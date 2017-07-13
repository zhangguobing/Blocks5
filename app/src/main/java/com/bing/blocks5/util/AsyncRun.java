package com.bing.blocks5.util;

import android.os.Handler;
import android.os.Looper;

/**
 * author：zhangguobing on 2017/7/13 16:53
 * email：bing901222@qq.com
 */

public final class AsyncRun {
    public static void runInMain(Runnable r) {
        Handler h = new Handler(Looper.getMainLooper());
        h.post(r);
    }

    public static void runInBack(Runnable r) {

    }
}