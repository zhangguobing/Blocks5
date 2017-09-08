package com.bing.blocks5.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bing.blocks5.AppCookie;
import com.bing.blocks5.api.ResponseError;
import com.bing.blocks5.model.Comment;
import com.bing.blocks5.model.ShareInfo;
import com.bing.blocks5.ui.common.GalleryActivity;
import com.bing.blocks5.ui.setting.IdentityActivity;
import com.bing.blocks5.ui.setting.SettingActivity;
import com.bing.blocks5.util.ClickUtils;
import com.bing.blocks5.util.ShareUtil;
import com.bing.blocks5.util.TimeUtil;
import com.bing.blocks5.widget.opendanmaku.DanmakuItem;
import com.bing.blocks5.widget.opendanmaku.DanmakuView;
import com.bing.blocks5.widget.opendanmaku.IDanmakuItem;
import com.bumptech.glide.Glide;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.NormalDialog;
import com.lcodecore.tkrefreshlayout.utils.DensityUtil;
import com.bing.blocks5.R;
import com.bing.blocks5.base.BaseController;
import com.bing.blocks5.base.BasePresenterActivity;
import com.bing.blocks5.base.ContentView;
import com.bing.blocks5.model.Activity;
import com.bing.blocks5.model.Config;
import com.bing.blocks5.controller.ActivityController;
import com.bing.blocks5.repository.ConfigManager;
import com.bing.blocks5.ui.main.ScanBarCodeActivity;
import com.bing.blocks5.ui.user.UserDetailActivity;
import com.bing.blocks5.util.ActivityDataConvert;
import com.bing.blocks5.util.ImageLoadUtil;
import com.bing.blocks5.util.ToastUtil;
import com.bing.blocks5.widget.SwitchButton;
import com.bing.blocks5.widget.TitleBar;
import com.bing.blocks5.widget.bottomsharedialog.BottomShareDialog;
import com.bing.blocks5.widget.toprightmenu.MenuItem;
import com.bing.blocks5.widget.toprightmenu.TopRightMenu;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * author：zhangguobing on 2017/7/4 09:25
 * email：bing901222@qq.com
 * 活动详情页
 */

@ContentView(R.layout.activity_activity_detail)
public class ActivityDetailActivity extends BasePresenterActivity<ActivityController.ActivityUiCallbacks>
   implements ActivityController.ActivityDetailUi,View.OnClickListener{

    @Bind(R.id.rg_price_type)
    RadioGroup mPriceTypeRg;
    @Bind(R.id.iv_user_avatar)
    ImageView mAvatarImg;
    @Bind(R.id.tv_nickname_and_sex)
    TextView mNickNameSexTv;
    @Bind(R.id.tv_credit)
    TextView mCreditTv;
    @Bind(R.id.iv_radar)
    ImageView mRadarImg;
    @Bind(R.id.tv_activity_id)
    TextView mActivityIdTv;
    @Bind(R.id.tv_activity_type)
    TextView mActivityTypeTv;
    @Bind(R.id.tv_activity_name)
    TextView mActivityNameTv;
    @Bind(R.id.tv_activity_area)
    TextView mActivityAreaTv;
    @Bind(R.id.tv_activity_people_num)
    TextView mActivityPeopleNumTv;
    @Bind(R.id.tv_total_price)
    TextView mTotalPriceTv;
    @Bind(R.id.tv_price_content)
    TextView mPriceContentTv;
    @Bind(R.id.tv_activity_content)
    TextView mActivityContentTv;
    @Bind(R.id.sb_need_identity)
    SwitchButton mNeedIdentitySwitch;
    @Bind(R.id.tv_start_time)
    TextView mStartTimeTv;
    @Bind(R.id.tv_end_time)
    TextView mEndTimeTv;
    Button mJoinBtn;
    @Bind(R.id.banner)
    Banner mBanner;
    @Bind(R.id.danmakuView)
    DanmakuView mDanmakuView;
    @Bind(R.id.container)
    LinearLayout mContainer;

    private static final String EXTRA_ACTIVITY_ID = "activityId";

    private String mActivityId;
    private int mUserId;
    private Activity mActivity;

    private ArrayList<String> imageUrls;

    private boolean isRadarOpened = true;

    public static void create(Context context, String activity_id){
        Intent intent = new Intent(context,ActivityDetailActivity.class);
        intent.putExtra(EXTRA_ACTIVITY_ID,activity_id);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setTitleBar();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActivityId = getIntent().getStringExtra(EXTRA_ACTIVITY_ID);
        if(TextUtils.isEmpty(mActivityId) || "null".equals(mActivityId)){
            ToastUtil.showText("活动id为空");
            finish();
            return;
        }
        showLoading(R.string.label_being_loading);
        getCallbacks().getActivityById(Integer.parseInt(mActivityId));
    }

    private void initPriceTypeRadioButton(int activity_price_type_id){
        List<Config.ActivityPriceTypesBean> priceTypesBeanList = ConfigManager.getInstance().getConfig().getActivity_price_types();
        if(priceTypesBeanList != null && priceTypesBeanList.size() > 0){
            for (Config.ActivityPriceTypesBean priceTypesBean: priceTypesBeanList){
                if(String.valueOf(activity_price_type_id).equals(priceTypesBean.getId())){
                    mPriceTypeRg.removeAllViews();
                    RadioButton radioButton = new RadioButton(this);
                    RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.leftMargin = DensityUtil.dp2px(this,15);
                    radioButton.setLayoutParams(layoutParams);
                    int padding = DensityUtil.dp2px(this,8);
                    radioButton.setPadding(padding,padding,padding,padding);
                    radioButton.setTextColor(getResources().getColorStateList(R.color.select_color_red));
                    radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                    radioButton.setButtonDrawable(null);
                    radioButton.setBackgroundResource(R.drawable.bg_radio_button_2);
                    radioButton.setText(priceTypesBean.getName());
                    radioButton.setChecked(true);
                    mPriceTypeRg.addView(radioButton);
                    break;
                }
            }
        }
    }


    private void setTitleBar(){
        TitleBar titleBar = getTitleBar();
        if(titleBar != null){
            titleBar.addAction(new TitleBar.Action() {
                @Override
                public String getText() {
                    return null;
                }

                @Override
                public int getDrawable() {
                    return R.mipmap.ic_more;
                }

                @Override
                public void performAction(View view) {
                    if(mActivity == null) return;
                    if(mActivity.getCreator() != null &&
                            AppCookie.getUserInfo().getId() == mActivity.getCreator().getId()){
                        showUserSelfMenu(view);
                    }else{
                        showOthersMenu(view);
                    }
                }
            });
        }
    }



    /**
     * 显示用户自己的菜单选项
     */
    private void showUserSelfMenu(View view){
        TopRightMenu topRightMenu = new TopRightMenu(this);
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(R.drawable.ic_filter_2, "筛选"));
        menuItems.add(new MenuItem(R.mipmap.ic_message_6dp, "留言"));
        menuItems.add(new MenuItem(R.mipmap.ic_share_6dp, "分享"));
        menuItems.add(new MenuItem(R.mipmap.ic_sign_list, "签到列表"));
        menuItems.add(new MenuItem(R.mipmap.ic_join_6dp, "取消活动"));
        topRightMenu
                .setHeight(RecyclerView.LayoutParams.WRAP_CONTENT)
                .setWidth(320)
                .showIcon(true)
                .dimBackground(true)
                .needAnimationStyle(true)
                .setAnimationStyle(R.style.TRM_ANIM_STYLE)
                .addMenuList(menuItems)
                .setOnMenuItemClickListener(position -> {
                    if(position == 0){
                          //筛选
                        SignUpListActivity.create(this,mActivity);
                    }else if(position == 1){
                         //留言
                        ActivityMessageActivity.create(this,mActivity);
                    }else if(position == 2){
                        //分享
                        showShareDialog();
                    }else if(position == 3){
                        //签到列表
                        SignInActivity.create(this,mActivity);
                    }else if(position == 4){
                        //取消活动
                        showLoading(R.string.label_being_something);
                        getCallbacks().cancelActivity(mActivityId);
                    }
                })
                .showAsDropDown(view, -205, 0);

    }


    /**
     * 显示其他人的菜单选项
     */
    private void showOthersMenu(View view){
        TopRightMenu topRightMenu = new TopRightMenu(this);
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(mActivity.getIs_collect() == 0 ? R.mipmap.ic_favourite_6dp : R.mipmap.ic_favorite_2_6dp,
                mActivity.getIs_collect() == 0 ? "收藏" : "取消收藏"));
        menuItems.add(new MenuItem(R.mipmap.ic_baoming_list_6dp, "报名列表"));
        menuItems.add(new MenuItem(R.mipmap.ic_scan_6dp, "扫一扫"));
        menuItems.add(new MenuItem(R.mipmap.ic_share_6dp, "分享"));
        menuItems.add(new MenuItem(R.mipmap.ic_join_6dp, mActivity.getIs_join() == 0 ? "加入活动" : "取消活动"));
        menuItems.add(new MenuItem(R.mipmap.ic_report_6dp, "举报活动"));
        topRightMenu
                .setHeight(RecyclerView.LayoutParams.WRAP_CONTENT)
                .setWidth(320)
                .showIcon(true)
                .dimBackground(true)
                .needAnimationStyle(true)
                .setAnimationStyle(R.style.TRM_ANIM_STYLE)
                .addMenuList(menuItems)
                .setOnMenuItemClickListener(position -> {
                    if(position == 0){
                        //收藏
                        showLoading(R.string.label_being_something);
                        if(mActivity.getIs_collect() == 0){
                            getCallbacks().collectActivity(Integer.valueOf(mActivityId));
                        }else{
                            getCallbacks().cancelCollectActivity(Integer.valueOf(mActivityId));
                        }
                    }else if(position == 1){
                        SignUpListActivity.create(this,mActivity);
                    }else if(position == 2){
                        ScanBarCodeActivity.create(this);
                    }else if(position == 3){
                        showShareDialog();
                    }else if(position == 4){
//                        if("1".equals(mActivity.getNeed_identity())
//                                && AppCookie.getUserInfo().getIdentity_state() == 0){
//                            showNeedIdentityDialog();
//                            return;
//                        }
                        showLoading(R.string.label_being_something);
                        if(mActivity.getIs_join() == 0){
                            getCallbacks().join(Integer.valueOf(mActivityId));
                        }else{
                            getCallbacks().cancelJoin(Integer.valueOf(mActivityId));
                        }

                    }else if(position == 5){
                        showSelectReportContentDialog();
                    }
                })
                .showAsDropDown(view, -205, 0);
    }

    private void showNeedIdentityDialog(){
        NormalDialog dialog = new NormalDialog(this);
        int color = ContextCompat.getColor(this,R.color.primary_text);
        int redColor = ContextCompat.getColor(this,R.color.red);
        dialog.setCanceledOnTouchOutside(false);
        dialog.isTitleShow(true)
                .cornerRadius(5)
                .content("本活动需要实名用户才能参加")
                .contentGravity(Gravity.CENTER)
                .contentTextColor(color)
                .dividerColor(R.color.divider)
                .btnTextSize(15.5f, 15.5f)
                .btnTextColor(color,redColor)
                .widthScale(0.75f)
                .btnText("取消","去认证")
                .show();
        dialog.setOnBtnClickL(dialog::dismiss, () -> {
            IdentityActivity.create(this,null);
            dialog.dismiss();
        });
    }

    private void showShareDialog(){
        new BottomShareDialog(this)
                .layout(BottomShareDialog.GRID)
                .orientation(BottomShareDialog.VERTICAL)
                .inflateMenu(R.menu.share, null)
                .itemClick(item -> {
                     ShareInfo info = new ShareInfo();
                     info.setTitle(mActivity.getTitle());
                     info.setImage_url(mActivity.getCover_url());
                     info.setText(mActivity.getContent());
                     info.setUrl("http://www.baidu.com");
                     if(item.getId() == R.id.wechat){
                         ShareUtil.shareWechat(info);
                     }else if(item.getId() == R.id.moments){
                         ShareUtil.shareWechatMoments(info);
                     }else if(item.getId() == R.id.qq){
                         ShareUtil.shareQQ(info);
                     }else if(item.getId() == R.id.qzone){
                         ShareUtil.shareQzone(info);
                     }
                }).show();
    }

    @Override
    protected BaseController getPresenter() {
        return new ActivityController();
    }


    private void showSelectReportContentDialog() {
        final String[] contentOptions = {"泄露隐私", "人身攻击", "淫秽色情", "垃圾广告", "敏感信息"};
        final ActionSheetDialog dialog = new ActionSheetDialog(this, contentOptions, null);
        dialog.isTitleShow(false).show();
        dialog.setOnOperItemClickL((parent, view, position, id) -> {
            dialog.dismiss();
            String content = contentOptions[position];
            showLoading(R.string.label_being_something);
            getCallbacks().report(0,Integer.valueOf(mActivityId),content);
        });
    }

    @Override
    public void getActivitySuccess(Activity activity) {
        cancelLoading();
        setActivityUi(activity);
        mActivity = activity;
    }


    private void setActivityUi(Activity activity){
        getTitleBar().setTitle(ActivityDataConvert.getActivityStateById(activity.getState()+""));
        ImageLoadUtil.loadAvatar(mAvatarImg,activity.getCreator().getAvatar(),this);
        mNickNameSexTv.setText(activity.getCreator().getNick_name());
        int sexDrawableId = "男".equals(activity.getCreator().getSex()) ? R.mipmap.ic_male : R.mipmap.ic_female;
        Drawable sexDrawable = ContextCompat.getDrawable(this,sexDrawableId);
        sexDrawable.setBounds(0,0,sexDrawable.getIntrinsicWidth(),sexDrawable.getIntrinsicHeight());
        mNickNameSexTv.setCompoundDrawables(null,null,sexDrawable,null);
        mCreditTv.setText("信用:" + activity.getCreator().getCredit());
        mActivityIdTv.setText(activity.getId()+"");
        mActivityTypeTv.setText(ActivityDataConvert.getActivityTypeNameById(activity.getActivity_type_id()));
        mActivityNameTv.setText(activity.getTitle());
        mActivityAreaTv.setText(activity.getCity() + "-" + activity.getArea());
        mStartTimeTv.setText(TimeUtil.getTimeWithoutSec(activity.getBegin_at()));
        mEndTimeTv.setText(TimeUtil.getTimeWithoutSec(activity.getEnd_at()));
        initPriceTypeRadioButton(activity.getPrice_type());
        String peopleNumStr = activity.getMan_num()+"男"+activity.getWoman_num()+"女";
        if(activity.getMan_left() == 0 && activity.getWoman_left() == 0){
            peopleNumStr += "(已报满)";
        }else{
            peopleNumStr += "<font color='#E24D3E'>(剩余男" + activity.getMan_left() + "人，女" + activity.getWoman_left() + "人)</font>";
        }
        mActivityPeopleNumTv.setText(Html.fromHtml(peopleNumStr));
        mTotalPriceTv.setText(activity.getPrice_total()+ "");
        mPriceContentTv.setText(TextUtils.isEmpty(activity.getPrice_content()) ? "无" : activity.getPrice_content());
        mActivityContentTv.setText(activity.getContent());
        mNeedIdentitySwitch.setChecked("1".equals(activity.getNeed_identity()));
        mUserId = activity.getUser_id();
        initBanner(activity);
        initDanmakuView(activity.getComments());
        View footView;
        if(AppCookie.getUserInfo().getId() == activity.getCreator().getId()){
            footView = LayoutInflater.from(this).inflate(R.layout.layout_activity_detail_bottom_user,mContainer,false);
            footView.findViewById(R.id.rl_filter).setOnClickListener(this);
            footView.findViewById(R.id.rl_message).setOnClickListener(this);
        }else{
            footView = View.inflate(this, R.layout.layout_activity_detail_bottom_others, null);
            footView.findViewById(R.id.rl_message).setOnClickListener(this);
            mJoinBtn = (Button) footView.findViewById(R.id.btn_join);
            mJoinBtn.setOnClickListener(this);
            mJoinBtn.setEnabled(activity.getState() == 1);
            mJoinBtn.setText(activity.getIs_join() == 0 ? "加入活动" : "退出活动");
        }
        mContainer.addView(footView);
    }


    private void initBanner(Activity activity) {
        if(activity == null) return;
        imageUrls = new ArrayList<>();
        if(!TextUtils.isEmpty(activity.getImg_url_1())){
            imageUrls.add(activity.getImg_url_1());
        }
        if(!TextUtils.isEmpty(activity.getImg_url_2())){
            imageUrls.add(activity.getImg_url_2());
        }
        if(!TextUtils.isEmpty(activity.getImg_url_3())){
            imageUrls.add(activity.getImg_url_3());
        }
        mBanner.setImages(imageUrls).setImageLoader(new GlideImageLoader())
                .setOnBannerListener(position -> showGallery(mBanner,position)).start();
    }


    private void initDanmakuView(List<Comment> comments){
        if(comments == null || comments.size() == 0){
            mDanmakuView.setVisibility(View.GONE);
            return;
        }
        List<IDanmakuItem> list = new ArrayList<>();
        for (int i = 0; i < comments.size(); i++) {
            list.add(new DanmakuItem(this,comments.get(i).getContent(),mDanmakuView.getWidth()));
        }
        mDanmakuView.addItem(list,true);
        mDanmakuView.show();

        mDanmakuView.setDanmakuListener(() ->{
            List<IDanmakuItem> newList = new ArrayList<>();
            for (int i = 0; i < comments.size(); i++) {
                newList.add(new DanmakuItem(this,comments.get(i).getContent(),mDanmakuView.getWidth()));
            }
            mDanmakuView.addItem(newList,true);
        });
    }

    private class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).placeholder(R.drawable.bg_img_place_holder).error(R.mipmap.img_error).into(imageView);
        }
    }

    @Override
    public void onCollectSuccess(String msg) {
        cancelLoading();
        ToastUtil.showText("收藏成功");
        mActivity.setIs_collect(1);
    }

    @Override
    public void cancelCollectSuccess(String msg) {
        cancelLoading();
        ToastUtil.showText("取消收藏成功");
        mActivity.setIs_collect(0);
    }

    @Override
    public void joinSuccess() {
        cancelLoading();
        ToastUtil.showText("报名成功");
        mJoinBtn.setText("退出活动");
        mActivity.setIs_join(1);
    }

    @Override
    public void joinFail(ResponseError error) {
        cancelLoading();
        if(error.getCode() == 5){
            showNeedIdentityDialog();
        }else{
            ToastUtil.showText(error.getMessage());
        }
    }

    @Override
    public void cancelJoinSuccess() {
        cancelLoading();
        ToastUtil.showText("取消成功");
        mJoinBtn.setText("加入活动");
        mActivity.setIs_join(0);
    }

    @Override
    public void reportSuccess() {
        cancelLoading();
        ToastUtil.showText("举报成功");
    }

    @Override
    public void cancelActivitySuccess(Activity activity) {
        cancelLoading();
        ToastUtil.showText("取消成功");
        setActivityUi(activity);
    }

    @OnClick({R.id.iv_barcode,R.id.iv_user_avatar,R.id.iv_radar})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rl_message:
                ActivityMessageActivity.create(this,mActivity);
                break;
            case R.id.btn_join:
                showLoading(R.string.label_being_something);
                if(mActivity.getIs_join() == 0){
                    getCallbacks().join(Integer.valueOf(mActivityId));
                }else{
                    getCallbacks().cancelJoin(Integer.valueOf(mActivityId));
                }
                break;
            case R.id.iv_barcode:
                HowSignInActivity.create(this,mActivity, HowSignInActivity.PAGE_FROM_ACTIVITY_DETAIL);
                break;
            case R.id.iv_user_avatar:
                UserDetailActivity.create(this,mUserId);
                break;
            case R.id.iv_radar:
                if(ClickUtils.isFastDoubleClick()) return;
                if(isRadarOpened){
                    mRadarImg.setImageResource(R.mipmap.ic_radar_normal);
                    mDanmakuView.hide();
                }else{
                    mRadarImg.setImageResource(R.mipmap.ic_radar_selected);
                    mDanmakuView.show();
                }
                isRadarOpened = !isRadarOpened;
                break;
            case R.id.rl_filter:
                SignUpListActivity.create(this,mActivity);
                break;
        }
    }

    private void showGallery(View v,int position) {
        if(imageUrls == null || imageUrls.size() == 0) return;
        int[] location = new int[2];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Rect frame = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;
            v.getLocationOnScreen(location);
            location[1] += statusBarHeight;
        } else {
            v.getLocationOnScreen(location);
        }
        v.invalidate();
        int width = v.getWidth();
        int height = v.getHeight();

        Intent intent = new Intent(this, GalleryActivity.class);
        Bundle b = new Bundle();
        b.putStringArrayList(GalleryActivity.PHOTO_SOURCE_ID, imageUrls);
        intent.putExtras(b);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_POSITION, position);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_X_TAG, location[0]);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_Y_TAG, location[1]);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_W_TAG, width);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_H_TAG, height);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
