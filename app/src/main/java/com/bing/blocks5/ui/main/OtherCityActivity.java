package com.bing.blocks5.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.bing.blocks5.R;
import com.bing.blocks5.base.BaseActivity;
import com.bing.blocks5.base.ContentView;
import com.bing.blocks5.util.AMapLocationUtil;
import com.orhanobut.logger.Logger;

import butterknife.Bind;

/**
 * author：zhangguobing on 2017/7/17 22:28
 * email：bing901222@qq.com
 */
@ContentView(R.layout.activity_other_city)
public class OtherCityActivity extends BaseActivity {

    @Bind(R.id.tv_location_city)
    TextView mLocationCityTv;

    public static void create(Context context){
        Intent intent = new Intent(context,OtherCityActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        AMapLocationUtil.getInstance().setAMapLocationUtilListener(new AMapLocationUtil.AMapLocationUtilListener() {
            @Override
            public void onSuccess(AMapLocation aMapLocation) {
                Logger.d("AMapLocation : ", aMapLocation);
                mLocationCityTv.setText(aMapLocation.getCity());
            }

            @Override
            public void onFail(int errorCode, String errMsg) {
                mLocationCityTv.setText("定位失败");
                Logger.d("AMapLocation : ", "errcode:"+ errorCode + ",errMsg:"+errMsg);
            }
        }).startLocation();
    }
}
