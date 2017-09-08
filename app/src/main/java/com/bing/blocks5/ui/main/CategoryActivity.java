package com.bing.blocks5.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bing.blocks5.AppCookie;
import com.bing.blocks5.R;
import com.bing.blocks5.base.BaseActivity;
import com.bing.blocks5.base.ContentView;
import com.bing.blocks5.model.Config;
import com.bing.blocks5.model.event.MainActivityListFilterEvent;
import com.bing.blocks5.model.request.MainActivityListParams;
import com.bing.blocks5.ui.activity.AddActivityActivity;
import com.bing.blocks5.ui.main.adapter.FragmentAdapter;
import com.bing.blocks5.ui.main.fragments.MainActivityListFragment;
import com.bing.blocks5.util.EventUtil;
import com.bing.blocks5.util.ImageLoadUtil;
import com.bing.blocks5.util.TimeUtil;
import com.bing.blocks5.widget.DropDownView;
import com.bing.blocks5.widget.FlowRadioButton;
import com.bing.blocks5.widget.FlowRadioGroup;
import com.bing.blocks5.widget.TitleBar;
import com.lcodecore.tkrefreshlayout.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by tian on 2017/9/6.
 * 活动分类页面
 */
@ContentView(R.layout.activity_category)
public class CategoryActivity extends BaseActivity
       implements View.OnClickListener{

    private static final int REQUEST_CODE_SELECT_CITY = 1001;
    public static final String EXTRA_CATEGORY_NAME = "category_name";
    public static final String EXTRA_CATEGORY_URL = "category_url";
    public static final String EXTRA_AREA_LIST = "area_list";
    public static final String EXTRA_TYPE_LIST = "type_list";

    @Bind(R.id.iv_activity_category)
    ImageView mActivityCategoryImg;
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    @Bind(R.id.drop_down_view)
    DropDownView mDropDownView;


    private FlowRadioGroup mSortRadioGroup;
    private FlowRadioGroup mProprotyRadioGroup;
    private FlowRadioGroup mAreaRadioGroup;
    private FlowRadioGroup mTimeRadioGroup;
    private TextView mCityTextView;

    //系统当前的城市
    private String mCurrentCity = AppCookie.getCity();

    private List<String> areaOptions = new ArrayList<>();
    private ArrayList<Config.ActivityAreasBean> activityAreasList;

    private MainActivityListParams params = MainActivityListParams.getDefault();

    public static void create(Context context,String category_name,String category_url,
                              ArrayList<Config.ActivityAreasBean> areasBeanList,ArrayList<Config.ActivityTypesBean> activityTypes){
        Intent intent = new Intent(context,CategoryActivity.class);
        intent.putExtra(EXTRA_CATEGORY_NAME, category_name);
        intent.putExtra(EXTRA_CATEGORY_URL, category_url);
        intent.putExtra(EXTRA_AREA_LIST, areasBeanList);
        intent.putExtra(EXTRA_TYPE_LIST, activityTypes);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();

        ImageLoadUtil.loadImage(mActivityCategoryImg,intent.getStringExtra(EXTRA_CATEGORY_URL),this);
        activityAreasList = intent.getParcelableArrayListExtra(EXTRA_AREA_LIST);
        initViewPager(intent.getParcelableArrayListExtra(EXTRA_TYPE_LIST));

        View collapsedView = LayoutInflater.from(this).inflate(R.layout.layout_title_bar, null, false);
        View expandedView = LayoutInflater.from(this).inflate(R.layout.view_my_drop_down_expanded, null, false);

        TitleBar titleBar = (TitleBar) collapsedView.findViewById(R.id.title_bar);
        titleBar.setLeftImageResource(R.mipmap.ic_navigate_back);
        titleBar.setLeftClickListener(view -> finish());
        titleBar.setActionTextColor(ContextCompat.getColor(this,R.color.white));
        titleBar.setTitleColor(ContextCompat.getColor(this,R.color.white));
        titleBar.setTitle(intent.getStringExtra(EXTRA_CATEGORY_NAME));
        titleBar.addAction(new TitleBar.Action() {
            @Override
            public String getText() {
                return "筛选";
            }

            @Override
            public int getDrawable() {
                return 0;
            }

            @Override
            public void performAction(View view) {
                if(mDropDownView.isExpanded()){
                    mDropDownView.collapseDropDown();
                }else{
                    mDropDownView.expandDropDown();
                }
            }
        });

        expandedView.findViewById(R.id.tv_other_city).setOnClickListener(this);
        expandedView.findViewById(R.id.btn_ok).setOnClickListener(this);

        mSortRadioGroup = (FlowRadioGroup) expandedView.findViewById(R.id.rg_sort);
        mProprotyRadioGroup = (FlowRadioGroup) expandedView.findViewById(R.id.rg_property);
        mAreaRadioGroup = (FlowRadioGroup) expandedView.findViewById(R.id.rg_activity_area);
        mTimeRadioGroup = (FlowRadioGroup) expandedView.findViewById(R.id.rg_activity_time);

        List<String> dates = TimeUtil.getFutureDate(8,"MM-dd");
        List<String> weeks = TimeUtil.getFutureWeeks(8);
        List<String> timeOptions = new ArrayList<>();
        timeOptions.add("一周内");
        for (int i = 0; i < dates.size(); i++) {
            timeOptions.add(weeks.get(i) + " " + dates.get(i));
        }
        addChildOptionToRadioGroup(mTimeRadioGroup,timeOptions,0);

        mCityTextView = (TextView) expandedView.findViewById(R.id.tv_city);
        mCityTextView.setText(mCurrentCity);

        mDropDownView.setHeaderView(collapsedView);
        mDropDownView.setExpandedView(expandedView);
        collapsedView.setOnClickListener(null);
//        mDropDownView.setDropDownListener(dropDownListener);
    }

    private void initViewPager(List<Config.ActivityTypesBean> activityTypesBeans) {
        if(activityTypesBeans.size() >= 5){
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }else{
            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
            mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        }

        List<String> titles = new ArrayList<>();
        for (Config.ActivityTypesBean activityType: activityTypesBeans){
            titles.add(activityType.getName());
        }

        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            params.activity_type_id =  activityTypesBeans.get(i).getId();
            fragments.add(MainActivityListFragment.newInstance(params.activity_type_id));
        }
        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragments, titles));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    @OnClick(R.id.rl_add_activity)
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_other_city:
                OtherCityActivity.create(this, REQUEST_CODE_SELECT_CITY);
                break;
            case R.id.btn_ok:
                mDropDownView.collapseDropDown();
                params.city = mCityTextView.getText().toString();
                AppCookie.saveCity(params.city);
                params.sort_type = mSortRadioGroup.getCheckedRadioButtonId() == R.id.rb_newest_time ? null : "credit";
                params.state = mProprotyRadioGroup.getCheckedRadioButtonId() == R.id.rb_concentration ? "1":"2";
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
            case R.id.rl_add_activity:
                AddActivityActivity.create(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_SELECT_CITY:
                    String selectCity = data.getStringExtra(OtherCityActivity.EXTRA_RESULT);
                    mCityTextView.setText(selectCity);
                    changeActivityAreaByCity(selectCity);
                    break;
            }
        }
    }

    private void changeActivityAreaByCity(String city) {
        areaOptions.clear();
        areaOptions.add("所有地区");
        if(activityAreasList != null && activityAreasList.size() > 0){
            for (Config.ActivityAreasBean activityArea: activityAreasList){
                if(activityArea.getCity().equals(city)){
                    areaOptions.addAll(activityArea.getAreas());
                    break;
                }
            }
        }
        addChildOptionToRadioGroup(mAreaRadioGroup, areaOptions,0);
    }

    /**
     * 添加子选项到RadioGroup中
     * @param flowRadioGroup
     * @param options
     */
    private void addChildOptionToRadioGroup(FlowRadioGroup flowRadioGroup,List<String> options, int defaultCheckedPosition){
        flowRadioGroup.removeAllViews();
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
            flowRadioButton.setText(options.get(i));
            flowRadioButton.setGravity(Gravity.CENTER);
            flowRadioButton.setBackgroundResource(R.drawable.bg_radio_button_3);
            flowRadioButton.setTextColor(ContextCompat.getColorStateList(this,R.color.select_color_white));
            flowRadioButton.setChecked(defaultCheckedPosition == i);
            flowRadioGroup.addView(flowRadioButton);
        }
    }
}
