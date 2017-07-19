package com.bing.blocks5.util;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.bing.blocks5.Blocks5App;

/**
 * author：zhangguobing on 2017/7/17 16:27
 * email：bing901222@qq.com
 * 高德定位服务工具类
 */

public class AMapLocationUtil {

    private static AMapLocationUtil aMapLocationUtil;

    private AMapLocationUtilListener aMapLocationUtilListener;

    private AMapLocationClient aMapLocationClient;

    private AMapLocationUtil(){
        init();
    }

    private void init() {
        aMapLocationClient = new AMapLocationClient(Blocks5App.getContext());
        aMapLocationClient.setLocationListener(aMapLocation -> {
            if(aMapLocation != null){
                if(aMapLocation.getErrorCode() == 0){
                    if(aMapLocationUtilListener != null){
                        aMapLocationUtilListener.onSuccess(aMapLocation);
                    }
                }else{
                    if(aMapLocationUtilListener != null){
                        aMapLocationUtilListener.onFail(aMapLocation.getErrorCode(),aMapLocation.getErrorInfo());
                    }
                }
            }
        });
        AMapLocationClientOption locationClientOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        locationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //只需一次定位
        locationClientOption.setOnceLocation(true);

        aMapLocationClient.setLocationOption(locationClientOption);

    }

    public AMapLocationUtilListener getAMapLocationUtilListener() {
        return aMapLocationUtilListener;
    }

    public AMapLocationUtil setAMapLocationUtilListener(AMapLocationUtilListener aMapLocationUtilListener) {
        this.aMapLocationUtilListener = aMapLocationUtilListener;
        return this;
    }

    public static AMapLocationUtil getInstance(){
        if(aMapLocationUtil == null){
            synchronized (AMapLocationUtil.class){
                if(aMapLocationUtil == null){
                    aMapLocationUtil = new AMapLocationUtil();
                }
            }
        }
        return aMapLocationUtil;
    }

    public interface AMapLocationUtilListener{
        void onSuccess(AMapLocation aMapLocation);
        void onFail(int errorCode, String errMsg);
    }

    public void stopLocation(){
        if(aMapLocationClient != null){
            aMapLocationClient.stopLocation();
        }
    }

    public void startLocation(){
        if(aMapLocationClient != null){
            aMapLocationClient.startLocation();
        }
    }

    public void onDestory(){
        if(aMapLocationClient != null){
            aMapLocationClient.onDestroy();
        }
    }
}
