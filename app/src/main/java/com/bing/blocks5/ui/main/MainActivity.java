package com.bing.blocks5.ui.main;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.bing.blocks5.ui.main.fragments.MainActivityListFragment;
import com.bing.blocks5.util.AMapLocationUtil;
import com.bing.blocks5.widget.DropDownView;
import com.bing.blocks5.widget.FlowRadioButton;
import com.bing.blocks5.widget.FlowRadioGroup;
import com.bumptech.glide.Glide;
import com.lcodecore.tkrefreshlayout.utils.DensityUtil;
import com.orhanobut.logger.Logger;
import com.youth.banner.loader.ImageLoader;
import com.bing.blocks5.R;
import com.bing.blocks5.base.BaseController;
import com.bing.blocks5.base.BasePresenterActivity;
import com.bing.blocks5.base.ContentView;
import com.bing.blocks5.model.Config;
import com.bing.blocks5.controller.LoginAuthController;
import com.bing.blocks5.ui.activity.AddActivityActivity;
import com.bing.blocks5.ui.main.adapter.FragmentAdapter;
import com.bing.blocks5.ui.main.fragments.DrawerMenuFragment;
import com.bing.blocks5.ui.search.SearchActivity;
import com.bing.blocks5.ui.user.ProfileActivity;
import com.bing.blocks5.widget.HomeBanner;
import com.bing.blocks5.widget.HomeTabViewPager;
import com.bing.blocks5.widget.slidingmenu.SlidingMenu;
import com.bing.blocks5.widget.slidingmenu.app.SlidingActivityBase;
import com.bing.blocks5.widget.slidingmenu.app.SlidingActivityHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.campusapp.router.Router;

/**
 * author：zhangguobing on 2017/6/19 20:29
 * email：bing901222@qq.com
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends BasePresenterActivity<LoginAuthController.LoginAuthUiCallbacks>
   implements LoginAuthController.HomeUi,SlidingActivityBase,View.OnClickListener {

    @Bind(R.id.banner)
    HomeBanner mBanner;
    @Bind(R.id.view_pager)
    HomeTabViewPager mViewPager;
    @Bind(R.id.app_bar_layout)
    public AppBarLayout mAppBarLayout;
    @Bind(R.id.drop_down_view)
    DropDownView mDropDownView;

    private SlidingActivityHelper mHelper;
    private SlidingMenu mSlidingMenu;


    public static final int NUM_OF_STANDS = 4;
    private ImageView arrowImg;
    private FlowRadioGroup mSortRadioGroup;
    private FlowRadioGroup mProprotyRadioGroup;
    private FlowRadioGroup mAreaRadioGroup;
    private FlowRadioGroup mTimeRadioGroup;
    private TextView mCityTextView;

    //是否已经定位成功
    private boolean isLocationSuccess = false;
    //是否已经获取到全局配置
    private boolean isRecievedConfig = false;

    private String mLocationCity;
    private List<Config.ActivityAreasBean> ActivityAreasList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mHelper = new SlidingActivityHelper(this);
        mHelper.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setBehindContentView(R.layout.menu_frame);

        DrawerMenuFragment drawerMenuFragment = new DrawerMenuFragment();
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.menu_frame, drawerMenuFragment);
        t.commit();

        mSlidingMenu = getSlidingMenu();
        mSlidingMenu.setOnClosedListener(() -> {
            mViewPager.setScrollable(false);
            mBanner.setScrollable(false);
        });
        mSlidingMenu.setOnOpenListener(() -> {
            mViewPager.setScrollable(true);
            mBanner.setScrollable(true);
        });
        mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        mSlidingMenu.setFadeDegree(0.35f);
        mSlidingMenu.setMode(SlidingMenu.LEFT);
//        mSlidingMenu.setBehindWidthRes(R.dimen.slidingmenu_width);
        mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        mSlidingMenu.setShadowDrawable(R.drawable.shadow);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate(savedInstanceState);
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v != null)
            return v;
        return mHelper.findViewById(id);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        View collapsedView = LayoutInflater.from(this).inflate(R.layout.layout_main_title_view, null, false);
        View expandedView = LayoutInflater.from(this).inflate(R.layout.view_my_drop_down_expanded, null, false);

        arrowImg = (ImageView) collapsedView.findViewById(R.id.iv_arrow);
        collapsedView.findViewById(R.id.main_sliding_menu).setOnClickListener(this);
        collapsedView.findViewById(R.id.main_search).setOnClickListener(this);
        expandedView.findViewById(R.id.tv_other_city).setOnClickListener(this);

        mSortRadioGroup = (FlowRadioGroup) expandedView.findViewById(R.id.rg_sort);
        mProprotyRadioGroup = (FlowRadioGroup) expandedView.findViewById(R.id.rg_property);
        mAreaRadioGroup = (FlowRadioGroup) expandedView.findViewById(R.id.rg_activity_area);
        mTimeRadioGroup = (FlowRadioGroup) expandedView.findViewById(R.id.rg_activity_time);

        mCityTextView = (TextView) expandedView.findViewById(R.id.tv_city);

        mDropDownView.setHeaderView(collapsedView);
        mDropDownView.setExpandedView(expandedView);
        mDropDownView.setDropDownListener(dropDownListener);

        AMapLocationUtil.getInstance().setAMapLocationUtilListener(new AMapLocationUtil.AMapLocationUtilListener() {
            @Override
            public void onSuccess(AMapLocation aMapLocation) {
                Logger.d("AMapLocation : ", aMapLocation);
                isLocationSuccess = true;
                mLocationCity = aMapLocation.getCity();
                mCityTextView.setText(mLocationCity);
                notifyActivityAreaChanged();
            }

            @Override
            public void onFail(int errorCode, String errMsg) {
                mCityTextView.setText("定位失败");
                Logger.d("AMapLocation : ", "errcode:"+ errorCode + ",errMsg:"+errMsg);
            }
        }).startLocation();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AMapLocationUtil.getInstance().onDestory();
    }

    private final DropDownView.DropDownListener dropDownListener = new DropDownView.DropDownListener() {
        @Override
        public void onExpandDropDown() {
            ObjectAnimator.ofFloat(arrowImg, View.ROTATION.getName(), 180).start();
        }

        @Override
        public void onCollapseDropDown() {
            ObjectAnimator.ofFloat(arrowImg, View.ROTATION.getName(), -180, 0).start();
        }
    };

    @Override
    protected void loadUiData() {
        showLoading(R.string.label_being_loading);
        getCallbacks().fetchConfig();
        getCallbacks().getUploadToken();
    }

    private void initBanner(List<Config.BannersBean> bannersBeans){
        List<String> imageUrls = new ArrayList<>();
        for (Config.BannersBean bannersBean : bannersBeans){
            imageUrls.add(bannersBean.getImgUrl());
        }
        mBanner.setImages(imageUrls).setOnBannerListener(position -> {
            Config.BannersBean bannersBean = bannersBeans.get(position);
            Router.open(MainActivity.this,bannersBean.getLinkUrl());
        }).setImageLoader(new GlideImageLoader()).start();
    }

    @Override
    protected BaseController getPresenter() {
        return new LoginAuthController();
    }

    @Override
    public void receiveConfig(Config config) {
        cancelLoading();
        initBanner(config.getBanners());
        initViewPager(config.getActivity_types());
        ActivityAreasList = config.getActivity_areas();
        isRecievedConfig = true;
        notifyActivityAreaChanged();
    }

    private void notifyActivityAreaChanged() {
        if(isRecievedConfig && isLocationSuccess){
            for (Config.ActivityAreasBean activityArea: ActivityAreasList){
//                mLocationCity = "深圳市";
                if(activityArea.getCity().equals(mLocationCity)){
                    addChildOptionToRadioGroup(mAreaRadioGroup, activityArea.getAreas());
                    break;
                }
            }
        }
    }

    @Override
    public void setBehindContentView(View v, ViewGroup.LayoutParams params) {
        mHelper.setBehindContentView(v, params);
    }

    @Override
    public void setBehindContentView(View v) {
        setBehindContentView(v, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void setBehindContentView(int id) {
        setBehindContentView(getLayoutInflater().inflate(id, null));
    }

    @Override
    public SlidingMenu getSlidingMenu() {
        return mHelper.getSlidingMenu();
    }

    @Override
    public void toggle() {
        mHelper.toggle();
    }

    @Override
    public void showContent() {
        mHelper.showContent();
    }

    @Override
    public void showMenu() {
        mHelper.showMenu();
    }

    @Override
    public void showSecondaryMenu() {
        mHelper.showSecondaryMenu();
    }

    @Override
    public void setSlidingActionBarEnabled(boolean b) {
        mHelper.setSlidingActionBarEnabled(b);
    }

    private class GlideImageLoader extends ImageLoader{
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).placeholder(R.drawable.bg_img_place_holder).error(R.mipmap.img_error).into(imageView);
        }
    }


    @Override
    public boolean supportSlideBack() {
        return false;
    }

    private void initViewPager(List<Config.ActivityTypesBean> activityTypesBeans) {
        List<String> titles = new ArrayList<>();
        for (Config.ActivityTypesBean activityType: activityTypesBeans){
            titles.add(activityType.getName());
        }

        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            fragments.add(MainActivityListFragment.newInstance(activityTypesBeans.get(i).getId()));
        }
        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragments, titles));
        TabLayout tablayout = (TabLayout) findViewById(R.id.tab_layout);
        tablayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBanner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
    }

    public static void create(Context context){
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.ib_scan,R.id.ib_add_activity,R.id.iv_profile})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ib_scan:
                ScanBarCodeActivity.create(this);
                break;
            case R.id.ib_add_activity:
                AddActivityActivity.create(this);
                break;
            case R.id.iv_profile:
                ProfileActivity.createWithAnimation(this);
                break;
            case R.id.main_sliding_menu:
                mSlidingMenu.toggle();
                break;
            case R.id.main_search:
                SearchActivity.create(this);
                break;
            case R.id.tv_other_city:
                OtherCityActivity.create(this);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mHelper.onSaveInstanceState(outState);
    }

    @Override
    public void setContentView(int id) {
        setContentView(getLayoutInflater().inflate(id, null));
    }

    @Override
    public void setContentView(View v) {
        setContentView(v, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void setContentView(View v, ViewGroup.LayoutParams params) {
        super.setContentView(v, params);
        mHelper.registerAboveContentView(v, params);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean b = mHelper.onKeyUp(keyCode, event);
        if (b) return b;
        return super.onKeyUp(keyCode, event);
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
            flowRadioButton.setPadding(padding,padding,padding,padding);
            flowRadioButton.setButtonDrawable(null);
            flowRadioButton.setText(options.get(i));
            flowRadioButton.setGravity(Gravity.CENTER);
            flowRadioButton.setBackgroundResource(R.drawable.bg_radio_button_3);
            flowRadioButton.setTextColor(ContextCompat.getColorStateList(this,R.color.select_color_white));
            flowRadioGroup.addView(flowRadioButton);
        }
    }
}
