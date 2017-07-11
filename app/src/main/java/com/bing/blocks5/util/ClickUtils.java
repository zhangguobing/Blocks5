package com.bing.blocks5.util;

import android.os.SystemClock;

/**
 * author：ZhangGuoBing on 2017/6/2 13:48
 * email：bing901222@qq.com
 */
public class ClickUtils {

    private static long sLastClickTime = 0L;

    /**
     * 用于处理频繁点击问题, 如果两次点击小于500毫秒则不予以响应
     *
     *不能重复调用  一个onclick只能调用一次
     *
     * @return true:是连续的快速点击
     */
    public static boolean isFastDoubleClick() {
        long nowTime = SystemClock.elapsedRealtime();
        if ((nowTime - sLastClickTime) < 500) {
            return true;
        } else {
            sLastClickTime = nowTime;
            return false;
        }
    }
}
