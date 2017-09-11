package com.playmala.playmala.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.playmala.playmala.R;
import com.playmala.playmala.base.BaseActivity;
import com.playmala.playmala.base.ContentView;
import com.playmala.playmala.model.Config;
import com.playmala.playmala.repository.ConfigManager;
import com.playmala.playmala.util.AMapLocationUtil;
import com.playmala.playmala.widget.FlowRadioButton;
import com.playmala.playmala.widget.FlowRadioGroup;
import com.lcodecore.tkrefreshlayout.utils.DensityUtil;
import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * author：zhangguobing on 2017/7/17 22:28
 * email：bing901222@qq.com
 */
@ContentView(R.layout.activity_other_city)
public class OtherCityActivity extends BaseActivity implements View.OnClickListener{

    private static final String EXTRA_REQUEST_CODE = "request_code";
    public static final String EXTRA_RESULT = "result";

    @Bind(R.id.tv_location_city)
    TextView mLocationCityTv;
    @Bind(R.id.rg_city)
    FlowRadioGroup mCityRadioGroup;

    private List<Config.ActivityAreasBean> activityAreas;

    public static void create(Activity activity, int requestCode){
        Intent intent = new Intent(activity,OtherCityActivity.class);
        intent.putExtra(EXTRA_REQUEST_CODE, requestCode);
        activity.startActivityForResult(intent,requestCode);
        activity.overridePendingTransition(R.anim.slide_in_top, R.anim.activity_stay);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        activityAreas = ConfigManager.getInstance().getConfig().getActivity_areas();
        List<String> cityList = new ArrayList<>();
        for (int i = 0; i < activityAreas.size(); i++) {
            Config.ActivityAreasBean area = activityAreas.get(i);
            cityList.add(area.getCity());
        }
        addChildOptionToRadioGroup(mCityRadioGroup,cityList);

        AMapLocationListener mAMapLocationListener = new AMapLocationListener(this);
        AMapLocationUtil.getInstance().setAMapLocationUtilListener(mAMapLocationListener).startLocation();
    }

    /**
     * 添加子选项到RadioGroup中
     * @param flowRadioGroup
     * @param options
     */
    private void addChildOptionToRadioGroup(FlowRadioGroup flowRadioGroup,List<String> options){
        for (int i = 0; i < options.size(); i++) {
            FlowRadioButton flowRadioButton = new FlowRadioButton(this);
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            int margin = DensityUtil.dp2px(this,3);
            layoutParams.setMargins(margin,margin,margin,margin);
            flowRadioButton.setLayoutParams(layoutParams);
            int padding = DensityUtil.dp2px(this,7);
            flowRadioButton.setId(i);
            flowRadioButton.setPadding(padding,padding,padding,padding);
            flowRadioButton.setButtonDrawable(null);
            flowRadioButton.setOnClickListener(this);
            flowRadioButton.setText(options.get(i));
            flowRadioButton.setGravity(Gravity.CENTER);
            flowRadioButton.setBackgroundResource(R.drawable.bg_radio_button_3);
            flowRadioButton.setTextColor(ContextCompat.getColorStateList(this,R.color.select_color_white));
            flowRadioGroup.addView(flowRadioButton);
        }
    }

    private void setLocationSuccess(String text){
        mLocationCityTv.setText(text);
        mLocationCityTv.setOnClickListener(this);
        for (int i = 0; i < activityAreas.size(); i++) {
            Config.ActivityAreasBean area = activityAreas.get(i);
            if(area.getCity().equals(text)){
                mCityRadioGroup.check(i);
            }
        }
    }

    private void setLocationFail(int errcode ,String errmsg){
        mLocationCityTv.setText("定位失败");
    }

    @Override
    public void onClick(View v) {
        String city = "";
        if(v instanceof FlowRadioButton){
            Config.ActivityAreasBean selectArea = activityAreas.get(v.getId());
            city = selectArea.getCity();
        }else if(v.getId() == R.id.tv_location_city){
            city = mLocationCityTv.getText().toString();
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RESULT,city);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_stay,R.anim.slide_out_bottom);
    }

    private static class AMapLocationListener implements AMapLocationUtil.AMapLocationUtilListener {

        private final WeakReference<OtherCityActivity> mActivity;

        private AMapLocationListener(OtherCityActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(AMapLocation aMapLocation) {
            OtherCityActivity activity = mActivity.get();
            if(activity != null){
                activity.setLocationSuccess(aMapLocation.getCity());
            }
            Logger.d("AMapLocation : ", aMapLocation);
        }

        @Override
        public void onFail(int errorCode, String errMsg) {
            OtherCityActivity activity = mActivity.get();
            if(activity != null){
                activity.setLocationFail(errorCode,errMsg);
            }
            Logger.d("AMapLocation : ", "errcode:"+ errorCode + ",errMsg:"+errMsg);
        }
    }
}
