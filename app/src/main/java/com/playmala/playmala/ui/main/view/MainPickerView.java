package com.playmala.playmala.ui.main.view;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.utils.DensityUtil;
import com.playmala.playmala.AppCookie;
import com.playmala.playmala.R;
import com.playmala.playmala.model.Config;
import com.playmala.playmala.model.event.MainActivityListFilterEvent;
import com.playmala.playmala.model.request.MainActivityListParams;
import com.playmala.playmala.repository.ConfigManager;
import com.playmala.playmala.ui.main.OtherCityActivity;
import com.playmala.playmala.util.EventUtil;
import com.playmala.playmala.util.TimeUtil;
import com.playmala.playmala.widget.DropDownView;
import com.playmala.playmala.widget.FlowRadioButton;
import com.playmala.playmala.widget.FlowRadioGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tian on 2017/9/14.
 * 主页的筛选面板(单例)
 */

public class MainPickerView implements View.OnClickListener{
    
    private static MainPickerView mPickerView;
    
    private Activity mActivity;

//    private View mCollapsedView;
    private View mExpandedView;
    private DropDownView mDropDownView;

    private FlowRadioGroup mSortRadioGroup;
    private FlowRadioGroup mPropertyRadioGroup;
    private FlowRadioGroup mAreaRadioGroup;
    private FlowRadioGroup mTimeRadioGroup;
    private TextView mCityTextView;
    private TextView mNoCityTipTv;

    private MainActivityListParams params = MainActivityListParams.getDefault();

    private List<String> areaOptions = new ArrayList<>();

    public static final int REQUEST_CODE_SELECT_CITY = 1001;

    private ArrayList<Config.ActivityAreasBean> mActivityAreasList;

    //系统当前的城市
    private String mCurrentCity = AppCookie.getCity();

    public static MainPickerView getInstance(Activity activity) {
        if(mPickerView == null){
            synchronized (MainPickerView.class){
                if(mPickerView == null){
                    mPickerView = new MainPickerView(activity);
                }
            }
        }
        return mPickerView;
    }

    private MainPickerView(Activity activity) {
        mActivity = activity;
        init();
    }
    
    private void init(){
        mExpandedView = LayoutInflater.from(mActivity).inflate(R.layout.view_my_drop_down_expanded, null, false);
        mExpandedView.findViewById(R.id.tv_other_city).setOnClickListener(this);
        mExpandedView.findViewById(R.id.btn_ok).setOnClickListener(this);

        mSortRadioGroup = (FlowRadioGroup) mExpandedView.findViewById(R.id.rg_sort);
        mPropertyRadioGroup = (FlowRadioGroup) mExpandedView.findViewById(R.id.rg_property);
        mAreaRadioGroup = (FlowRadioGroup) mExpandedView.findViewById(R.id.rg_activity_area);
        mTimeRadioGroup = (FlowRadioGroup) mExpandedView.findViewById(R.id.rg_activity_time);
        mNoCityTipTv = (TextView) mExpandedView.findViewById(R.id.tv_city_no_support_tip);

        List<String> dates = TimeUtil.getFutureDate(8,"MM-dd");
        List<String> weeks = TimeUtil.getFutureWeeks(8);
        List<String> timeOptions = new ArrayList<>();
        timeOptions.add("一周内");
        for (int i = 0; i < dates.size(); i++) {
            timeOptions.add(weeks.get(i) + " " + dates.get(i));
        }
        addChildOptionToRadioGroup(mTimeRadioGroup,timeOptions,0);

        mCityTextView = (TextView) mExpandedView.findViewById(R.id.tv_city);
        mCityTextView.setText(mCurrentCity);

//        mCollapsedView.setOnClickListener(null);
    }


    public void bindViews(DropDownView dropDownView, View collapsedView){
        mDropDownView = dropDownView;
        ViewGroup parent = (ViewGroup) collapsedView.getParent();
        if (parent != null) {
            parent.removeView(collapsedView);
        }
        mDropDownView.setHeaderView(collapsedView);
        ViewGroup expandParent = (ViewGroup) mExpandedView.getParent();
        if (expandParent != null) {
            expandParent.removeView(mExpandedView);
        }
        mDropDownView.setExpandedView(mExpandedView);
    }


    public void bindData(ArrayList<Config.ActivityAreasBean> activityAreasBeen){
        mActivityAreasList = activityAreasBeen;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_other_city:
                OtherCityActivity.create(mActivity, REQUEST_CODE_SELECT_CITY);
                break;
            case R.id.btn_ok:
                mDropDownView.collapseDropDown();
                params.city = mCityTextView.getText().toString();
                AppCookie.saveCity(params.city);
                params.sort_type = mSortRadioGroup.getCheckedRadioButtonId() == R.id.rb_newest_time ? null : "credit";
                params.state = mPropertyRadioGroup.getCheckedRadioButtonId() == R.id.rb_concentration ? "1":"2";
                if(areaOptions != null && areaOptions.size() > 0){
                    params.area = areaOptions.get(mAreaRadioGroup.getCheckedRadioButtonId());
                }
                if(mTimeRadioGroup.getCheckedRadioButtonId() == 0){
                    params.end_at = TimeUtil.getEndTime(7);
                    params.begin_at = TimeUtil.getStartTime(0);
                }else{
                    int offset = mTimeRadioGroup.getCheckedRadioButtonId()-1;
                    params.end_at = TimeUtil.getEndTime(offset);
                    params.begin_at = TimeUtil.getStartTime(offset);
                }
                EventUtil.sendEvent(new MainActivityListFilterEvent(params));
                break;
        }
    }


    /**
     * 添加子选项到RadioGroup中
     * @param flowRadioGroup
     * @param options
     */
    private void addChildOptionToRadioGroup(FlowRadioGroup flowRadioGroup,List<String> options, int defaultCheckedPosition){
        flowRadioGroup.removeAllViews();
        for (int i = 0; i < options.size(); i++) {
            FlowRadioButton flowRadioButton = new FlowRadioButton(mActivity);
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            int margin = DensityUtil.dp2px(mActivity,3);
            layoutParams.setMargins(margin,margin,margin,margin);
            flowRadioButton.setLayoutParams(layoutParams);
            int padding = DensityUtil.dp2px(mActivity,7);
            flowRadioButton.setId(i);
            flowRadioButton.setPadding(padding,padding,padding,padding);
            flowRadioButton.setButtonDrawable(null);
            flowRadioButton.setText(options.get(i));
            flowRadioButton.setGravity(Gravity.CENTER);
            flowRadioButton.setBackgroundResource(R.drawable.bg_radio_button_3);
            flowRadioButton.setTextColor(ContextCompat.getColorStateList(mActivity,R.color.select_color_white));
            flowRadioButton.setChecked(defaultCheckedPosition == i);
            flowRadioGroup.addView(flowRadioButton);
        }
    }


    public void changeActivityAreaByCity(String city) {
        mCityTextView.setText(city);
        if(mActivityAreasList != null){
            areaOptions.clear();
            areaOptions.add("所有地区");
            for (Config.ActivityAreasBean activityArea: mActivityAreasList){
                if(activityArea.getCity().equals(city)){
                    areaOptions.addAll(activityArea.getAreas());
                    break;
                }
            }
            addChildOptionToRadioGroup(mAreaRadioGroup, areaOptions,0);
        }
        List<Config.ActivityAreasBean> activityAreas = ConfigManager.getInstance().getConfig().getActivity_areas();
        boolean isHasThisCity = false;
        for (int i = 0; i < activityAreas.size(); i++) {
            Config.ActivityAreasBean area = activityAreas.get(i);
            if(city.equals(area.getCity())){
                isHasThisCity = true;
                break;
            }
        }
        mNoCityTipTv.setVisibility(isHasThisCity ? View.GONE : View.VISIBLE);
    }
}
