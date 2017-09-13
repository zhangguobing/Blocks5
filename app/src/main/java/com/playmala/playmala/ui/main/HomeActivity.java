package com.playmala.playmala.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.playmala.playmala.AppCookie;
import com.playmala.playmala.R;
import com.playmala.playmala.base.BaseController;
import com.playmala.playmala.base.BasePresenterActivity;
import com.playmala.playmala.base.ContentView;
import com.playmala.playmala.controller.LoginAuthController;
import com.playmala.playmala.model.Config;
import com.playmala.playmala.model.User;
import com.playmala.playmala.model.event.UserInfoRefreshEvent;
import com.playmala.playmala.ui.activity.AddActivityActivity;
import com.playmala.playmala.ui.main.adapter.CategoryAdapter;
import com.playmala.playmala.ui.main.fragments.DrawerMenuFragment;
import com.playmala.playmala.ui.search.SearchActivity;
import com.playmala.playmala.ui.user.ProfileActivity;
import com.playmala.playmala.util.AMapLocationUtil;
import com.playmala.playmala.util.AppUtil;
import com.playmala.playmala.util.EventUtil;
import com.playmala.playmala.util.ToastUtil;
import com.playmala.playmala.widget.BottomSpaceItemDecoration;
import com.playmala.playmala.widget.HomeBanner;
import com.playmala.playmala.widget.TitleBar;
import com.playmala.playmala.widget.slidingmenu.SlidingMenu;
import com.playmala.playmala.widget.slidingmenu.app.SlidingActivityBase;
import com.playmala.playmala.widget.slidingmenu.app.SlidingActivityHelper;
import com.bumptech.glide.Glide;
import com.flyco.dialog.widget.NormalDialog;
import com.lcodecore.tkrefreshlayout.utils.DensityUtil;
import com.orhanobut.logger.Logger;
import com.youth.banner.loader.ImageLoader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.campusapp.router.Router;

/**
 * Created by tian on 2017/9/6.
 * 新的主页面
 */
@ContentView(R.layout.activity_home)
public class HomeActivity extends BasePresenterActivity<LoginAuthController.LoginAuthUiCallbacks>
        implements LoginAuthController.HomeUi,SlidingActivityBase,View.OnClickListener{

    @Bind(R.id.title_bar)
    TitleBar mTitleBar;
    @Bind(R.id.banner)
    HomeBanner mBanner;
    @Bind(R.id.category_list)
    RecyclerView mCategoryList;

    private SlidingActivityHelper mHelper;
    private SlidingMenu mSlidingMenu;

    private ArrayList<Config.ActivityAreasBean> ActivityAreasList;
    private ArrayList<Config.ActivityTypesBean> activityTypesList;
    private List<String> areaOptions = new ArrayList<>();

    //是否已经获取到全局配置
    private boolean isRecievedConfig = false;

    //系统当前的城市
    private String mCurrentCity = AppCookie.getCity();

    private CategoryAdapter mCategoryAdapter = new CategoryAdapter();

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
            mBanner.setScrollable(false);
        });
        mSlidingMenu.setOnOpenListener(() -> {
            if(isRecievedConfig) {
                getCallbacks().getUserById(AppCookie.getUserInfo().getId());
            }
            mBanner.setScrollable(true);
        });
        mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        mSlidingMenu.setFadeDegree(0.35f);
        mSlidingMenu.setMode(SlidingMenu.LEFT);
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
        mTitleBar.setLeftImageResource(R.mipmap.ic_sliding_menu);
        mTitleBar.setLeftClickListener(view -> mSlidingMenu.toggle());
        mTitleBar.addAction(new TitleBar.Action() {
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
        mTitleBar.addAction(new TitleBar.Action() {
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

            }
        });

        mCategoryAdapter.setViewEventListener((item, position, view) -> {
            CategoryActivity.create(this,item.getName(),item.getImage_url(),ActivityAreasList,findActivityTypesByParentId(item.getId()));
        });
        mCategoryList.setNestedScrollingEnabled(false);
        mCategoryList.setLayoutManager(new LinearLayoutManager(this));
        mCategoryList.addItemDecoration(new BottomSpaceItemDecoration(DensityUtil.dp2px(this,20)));
        mCategoryList.setAdapter(mCategoryAdapter);

        HomeActivity.AMapLocationListener locationListener = new HomeActivity.AMapLocationListener(this);
        AMapLocationUtil.getInstance().setAMapLocationUtilListener(locationListener).startLocation();
    }


    private ArrayList<Config.ActivityTypesBean> findActivityTypesByParentId(int parentId){
        ArrayList<Config.ActivityTypesBean> list = new ArrayList<>();
        if(activityTypesList != null && activityTypesList.size() > 0){
            for (Config.ActivityTypesBean activityType : activityTypesList){
                if(activityType.getParent_id() == parentId){
                    list.add(activityType);
                }
            }
        }
        return list;
    }


    @Override
    protected void loadUiData() {
        showLoading(R.string.label_being_loading);
        getCallbacks().fetchConfig();
        getCallbacks().getUploadToken();
    }

    public static void create(Context context){
        Intent intent = new Intent(context,HomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected BaseController getPresenter() {
        return new LoginAuthController();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mHelper.onSaveInstanceState(outState);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AMapLocationUtil.getInstance().onDestory();
    }

    private void initBanner(List<Config.BannersBean> bannersBeans){
        List<String> imageUrls = new ArrayList<>();
        for (Config.BannersBean bannersBean : bannersBeans){
            imageUrls.add(bannersBean.getImgUrl());
        }
        mBanner.setImages(imageUrls).setOnBannerListener(position -> {
            Config.BannersBean bannersBean = bannersBeans.get(position);
            Router.open(HomeActivity.this,bannersBean.getLinkUrl());
        }).setImageLoader(new HomeActivity.GlideImageLoader()).start();
    }


    private void initCategoryList(List<Config.ActivityTypesBean> typesBeans){
        List<Config.ActivityTypesBean> bigTypes = new ArrayList<>();
        for (Config.ActivityTypesBean typeBean : typesBeans){
            if(typeBean.getParent_id() == 0){
                bigTypes.add(typeBean);
            }
        }
        mCategoryAdapter.addItems(bigTypes);
    }


    @OnClick({R.id.ib_scan,R.id.ib_add_activity,R.id.iv_profile})
    public void onClick(View view) {
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

    private class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).placeholder(R.drawable.bg_img_place_holder).error(R.mipmap.img_error).into(imageView);
        }
    }

    @Override
    public void receiveConfig(Config config) {
        cancelLoading();
        initBanner(config.getBanners());
        initCategoryList(config.getActivity_types());
//        initViewPager(config.getActivity_types());
        ActivityAreasList = config.getActivity_areas();
        activityTypesList = config.getActivity_types();
        isRecievedConfig = true;
        changeActivityAreaByCity(mCurrentCity);
        checkVersion(config.getAndroid_update());
    }


    private void changeActivityAreaByCity(String city) {
        if(isRecievedConfig){
            areaOptions.clear();
            areaOptions.add("所有地区");
            for (Config.ActivityAreasBean activityArea: ActivityAreasList){
                if(activityArea.getCity().equals(city)){
                    areaOptions.addAll(activityArea.getAreas());
                    break;
                }
            }
//            addChildOptionToRadioGroup(mAreaRadioGroup, areaOptions,0);
        }
    }


    private void checkVersion(Config.AndroidUpdateBean android_update) {
        if(android_update != null && !TextUtils.isEmpty(android_update.getVersion())){
            int serverVersion = 0;
            if(android_update.getVersion().contains(".")){
                String[] arrays = android_update.getVersion().split("\\.");
                if(arrays.length == 2){
                    serverVersion = Integer.parseInt(arrays[0]) * 100 + Integer.parseInt(arrays[1]) * 10;
                }else if(arrays.length == 3){
                    serverVersion = Integer.parseInt(arrays[0]) * 100 + Integer.parseInt(arrays[1]) * 10 + Integer.parseInt(arrays[2]);
                }
            }
            int clientVersion = AppUtil.getVersionCode(this);
            if(serverVersion > clientVersion){
                if(android_update.getIs_force() == 0){
                    NormalDialog updateDialog = new NormalDialog(this);
                    int color = ContextCompat.getColor(this,R.color.primary_text);
                    int redColor = ContextCompat.getColor(this,R.color.red);
                    updateDialog.setCanceledOnTouchOutside(false);
                    updateDialog.title("发现新版本")
                            .cornerRadius(5)
                            .content(android_update.getContent())
                            .contentGravity(Gravity.CENTER)
                            .contentTextColor(color)
                            .dividerColor(R.color.divider)
                            .btnTextSize(15.5f, 15.5f)
                            .btnTextColor(color,redColor)
                            .widthScale(0.75f)
                            .btnText("取消","去升级")
                            .show();
                    updateDialog.setOnBtnClickL(updateDialog::dismiss, () -> {
                        AppUtil.openAppInMarket(HomeActivity.this);
                        updateDialog.dismiss();
                    });
                }else{
                    NormalDialog updateDialog = new NormalDialog(this);
                    int color = ContextCompat.getColor(this,R.color.primary_text);
                    int redColor = ContextCompat.getColor(this,R.color.red);
                    updateDialog.setCanceledOnTouchOutside(false);
                    updateDialog.title("发现新版本")
                            .cornerRadius(5)
                            .content(android_update.getContent())
                            .contentGravity(Gravity.CENTER)
                            .contentTextColor(color)
                            .dividerColor(R.color.divider)
                            .btnTextSize(15.5f, 15.5f)
                            .btnTextColor(color,redColor)
                            .widthScale(0.75f)
                            .btnText("去升级")
                            .show();
                    updateDialog.setOnBtnClickL(() -> {
                        AppUtil.openAppInMarket(HomeActivity.this);
                        updateDialog.dismiss();
                    });
                }
            }
        }
    }

    @Override
    public void receiveUser(User user) {
        EventUtil.sendEvent(new UserInfoRefreshEvent(user));
    }


    private void setLocationSuccess(AMapLocation aMapLocation){
        if(!mCurrentCity.equals(aMapLocation.getCity())){
            showLocationChangeDialog(aMapLocation.getCity());
        }
        Logger.d("AMapLocation : ", aMapLocation);
    }

    private void setLocationFail(int errorCode, String errMsg){
        ToastUtil.showText("定位失败");
        Logger.d("AMapLocation : ", "errcode:"+ errorCode + ",errMsg:"+errMsg);
    }

    /**
     * 显示位置发生改变dialog
     * @param locationCity 定位到的城市
     */
    private void showLocationChangeDialog(String locationCity){
        int color = ContextCompat.getColor(this,R.color.primary_text);
        final NormalDialog dialog = new NormalDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.isTitleShow(false)
                .cornerRadius(5)
                .content("系统检测到你当前位置位于"+ locationCity +",是否将当前城市设为" + locationCity +"?")
                .contentGravity(Gravity.CENTER)
                .contentTextColor(color)
                .dividerColor(R.color.divider)
                .btnTextSize(15.5f, 15.5f)
                .btnTextColor(color,color)
                .btnText("否","好的")
                .widthScale(0.75f)
                .show();
        dialog.setOnBtnClickL(dialog::dismiss, () -> {
            dialog.dismiss();
            AppCookie.saveCity(locationCity);
            mCurrentCity = locationCity;
            changeActivityAreaByCity(mCurrentCity);
        });
    }

    private static class AMapLocationListener implements AMapLocationUtil.AMapLocationUtilListener {

        private final WeakReference<HomeActivity> mActivity;

        private AMapLocationListener(HomeActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(AMapLocation aMapLocation) {
            HomeActivity homeActivity = mActivity.get();
            if(homeActivity != null){
                homeActivity.setLocationSuccess(aMapLocation);
            }
            Logger.d("AMapLocation : ", aMapLocation);
        }

        @Override
        public void onFail(int errorCode, String errMsg) {
            HomeActivity homeActivity = mActivity.get();
            if(homeActivity != null){
                homeActivity.setLocationFail(errorCode, errMsg);
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

    @Override
    public boolean supportSlideBack() {
        return false;
    }
}
