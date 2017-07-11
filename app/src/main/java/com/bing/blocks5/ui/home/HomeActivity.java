package com.bing.blocks5.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;
import com.bing.blocks5.AppCookie;
import com.bing.blocks5.Blocks5App;
import com.bing.blocks5.R;
import com.bing.blocks5.base.BasePresenter;
import com.bing.blocks5.base.BasePresenterActivity;
import com.bing.blocks5.base.ContentView;
import com.bing.blocks5.model.Config;
import com.bing.blocks5.presenter.LoginAuthPresenter;
import com.bing.blocks5.ui.activity.AddActivityActivity;
import com.bing.blocks5.ui.home.adapter.FragmentAdapter;
import com.bing.blocks5.ui.home.fragments.HomeActivityListFragment;
import com.bing.blocks5.ui.home.fragments.DrawerMenuFragment;
import com.bing.blocks5.ui.search.SearchActivity;
import com.bing.blocks5.ui.user.ProfileActivity;
import com.bing.blocks5.widget.HomeBanner;
import com.bing.blocks5.widget.HomeTabViewPager;
import com.bing.blocks5.widget.TitleBar;
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
@ContentView(R.layout.activity_home)
public class HomeActivity extends BasePresenterActivity<LoginAuthPresenter.LoginAuthUiCallbacks>
   implements LoginAuthPresenter.HomeUi,SlidingActivityBase {

    @Bind(R.id.banner)
    HomeBanner mBanner;
    @Bind(R.id.view_pager)
    HomeTabViewPager mViewPager;

    private SlidingActivityHelper mHelper;
    private SlidingMenu mSlidingMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mHelper = new SlidingActivityHelper(this);
        mHelper.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setBehindContentView(R.layout.menu_frame);

        Blocks5App.getContext().mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);

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
        super.initView(savedInstanceState);
        initView();
    }

    private void initView() {
        setTitleBar();
//        initBanner();
//        initViewPager();
    }

    @Override
    protected void loadUiData() {
        showLoading(R.string.label_being_loading);
        getCallbacks().fetchConfig(AppCookie.getToken());
    }

    private void initBanner(List<Config.BannersBean> bannersBeans){
        List<String> imageUrls = new ArrayList<>();
        for (Config.BannersBean bannersBean : bannersBeans){
            imageUrls.add(bannersBean.getImgUrl());
        }
        mBanner.setImages(imageUrls).setDelayTime(5000).setOnBannerListener(position -> {
            Config.BannersBean bannersBean = bannersBeans.get(position);
            Router.open(HomeActivity.this,bannersBean.getLinkUrl());
        }).setImageLoader(new GlideImageLoader()).start();
    }

    @Override
    protected BasePresenter getPresenter() {
        return new LoginAuthPresenter();
    }

    @Override
    public void receiveConfig(Config config) {
        cancelLoading();

        initBanner(config.getBanners());

        initViewPager(config.getActivity_types());
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
            Glide.with(context).load(path).into(imageView);
        }
    }

    private void setTitleBar() {
        TitleBar titleBar = getTitleBar();
        if(titleBar != null){
            View titleView = getLayoutInflater().inflate(R.layout.layout_main_title,null);
            titleBar.setCustomTitle(titleView);
            titleBar.setLeftImageResource(R.mipmap.ic_sliding_menu);
            titleBar.setLeftClickListener(v -> mSlidingMenu.toggle());
            titleBar.addAction(new TitleBar.Action() {
                @Override
                public String getText() {
                    return null;
                }

                @Override
                public int getDrawable() {
                    return R.mipmap.ic_search_white;
                }

                @Override
                public void performAction(View view) {
                    SearchActivity.create(HomeActivity.this);
                }
            });
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
            fragments.add(HomeActivityListFragment.newInstance(activityTypesBeans.get(i).getId()));
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
        Intent intent = new Intent(context,HomeActivity.class);
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
}
