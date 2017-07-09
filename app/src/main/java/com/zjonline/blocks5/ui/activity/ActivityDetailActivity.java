package com.zjonline.blocks5.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.utils.DensityUtil;
import com.zjonline.blocks5.R;
import com.zjonline.blocks5.base.BasePresenter;
import com.zjonline.blocks5.base.BasePresenterActivity;
import com.zjonline.blocks5.base.ContentView;
import com.zjonline.blocks5.model.Activity;
import com.zjonline.blocks5.model.Config;
import com.zjonline.blocks5.presenter.ActivityPresenter;
import com.zjonline.blocks5.repository.ConfigManager;
import com.zjonline.blocks5.ui.user.UserDetailActivity;
import com.zjonline.blocks5.util.ActivityDataConvert;
import com.zjonline.blocks5.util.ImageLoadUtil;
import com.zjonline.blocks5.util.ToastUtil;
import com.zjonline.blocks5.widget.SwitchButton;
import com.zjonline.blocks5.widget.TitleBar;
import com.zjonline.blocks5.widget.toprightmenu.MenuItem;
import com.zjonline.blocks5.widget.toprightmenu.TopRightMenu;

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
public class ActivityDetailActivity extends BasePresenterActivity<ActivityPresenter.ActivityUiCallbacks>
   implements ActivityPresenter.ActivityDetailUi{

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
    @Bind(R.id.btn_join)
    Button mJoinBtn;

    private static final String EXTRA_ACTIVITY_ID = "activityId";

    private String mActivityId;
    private int mUserId;

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
                    radioButton.setTextColor(getResources().getColorStateList(R.color.textview));
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
                    showTopRightMenu(view);
                }
            });
        }
    }


    private void showTopRightMenu(View view){
        TopRightMenu topRightMenu = new TopRightMenu(this);
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(R.mipmap.ic_favourite_6dp, "收藏"));
        menuItems.add(new MenuItem(R.mipmap.ic_baoming_list_6dp, "报名列表"));
        menuItems.add(new MenuItem(R.mipmap.ic_scan_6dp, "扫一扫"));
        menuItems.add(new MenuItem(R.mipmap.ic_share_6dp, "分享"));
        menuItems.add(new MenuItem(R.mipmap.ic_join_6dp, "加入活动"));
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

                })
                .showAsDropDown(view, -205, 0);
    }

    @Override
    protected BasePresenter getPresenter() {
        return new ActivityPresenter();
    }


    @Override
    public void getActivitySuccess(Activity activity) {
        cancelLoading();
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
        mActivityAreaTv.setText(activity.getArea());
        mStartTimeTv.setText(activity.getBegin_at());
        mEndTimeTv.setText(activity.getEnd_at());
        initPriceTypeRadioButton(activity.getPrice_type());
        mActivityPeopleNumTv.setText(getString(R.string.format_male_female_people_num,activity.getMan_num()+"",activity.getWoman_num()+""));
        mTotalPriceTv.setText(activity.getPrice_total()+ "");
        mPriceContentTv.setText(activity.getPrice_content());
        mActivityContentTv.setText(activity.getContent());
        mNeedIdentitySwitch.setChecked("0".equals(activity.getNeed_identity()));
        mJoinBtn.setEnabled(activity.getState() == 1);
        mUserId = activity.getUser_id();
    }

    @OnClick({R.id.rl_message,R.id.btn_join,R.id.iv_barcode,R.id.iv_user_avatar})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rl_message:
                break;
            case R.id.btn_join:
                break;
            case R.id.iv_barcode:
                HowSignInActivity.create(this,mActivityId);
                break;
            case R.id.iv_user_avatar:
                UserDetailActivity.create(this,mUserId);
                break;
        }
    }
}
