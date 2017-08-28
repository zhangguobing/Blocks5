package com.bing.blocks5.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bing.blocks5.AppCookie;
import com.bing.blocks5.model.Activity;
import com.bing.blocks5.model.ActivityUser;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.NormalDialog;
import com.bing.blocks5.R;
import com.bing.blocks5.base.BaseController;
import com.bing.blocks5.base.BasePresenterActivity;
import com.bing.blocks5.base.ContentView;
import com.bing.blocks5.model.event.ActivityUserFilterEvent;
import com.bing.blocks5.controller.ActivityController;
import com.bing.blocks5.ui.activity.fragment.ActivityUserFragment;
import com.bing.blocks5.ui.main.adapter.FragmentAdapter;
import com.bing.blocks5.util.EventUtil;
import com.bing.blocks5.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * author：zhangguobing on 2017/7/10 10:28
 * email：bing901222@qq.com
 */
@ContentView(R.layout.activity_signup_list)
public class SignUpListActivity extends BasePresenterActivity<ActivityController.ActivityUiCallbacks>
   implements ActivityController.SignUpUi{

    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    @Bind(R.id.tv_confirm)
    TextView mConfirmTv;

    private Activity mActivity;

    private static final String EXTRA_ACTIVITY = "extra_activity";

    private String[] mSignUpOptions = new String[]{"全部","男生","女生"};
    private String[] mJoinOptions = new String[]{"全部","男生","女生"};

    public static void create(Context context,Activity activity){
        Intent intent = new Intent(context, SignUpListActivity.class);
        intent.putExtra(EXTRA_ACTIVITY, activity);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mActivity = getIntent().getParcelableExtra(EXTRA_ACTIVITY);

        List<String> titles = new ArrayList<>();
        titles.add("报名");
        titles.add("已加入");
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ActivityUserFragment.newInstance(0,mActivity));
        fragments.add(ActivityUserFragment.newInstance(1,mActivity));
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragments, titles));
        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mConfirmTv.setVisibility(position == 1 && mActivity.getState() == 1
                        && AppCookie.getUserInfo().getId() == mActivity.getCreator().getId() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.iv_back,R.id.iv_filter,R.id.tv_confirm})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_filter:
                showFilterDialog();
                break;
            case R.id.tv_confirm:
                showConfirmDialog();
                break;
        }
    }

    private void showFilterDialog() {
        final String[] contentOptions = mViewPager.getCurrentItem() == 0 ? mSignUpOptions : mJoinOptions;
        final ActionSheetDialog dialog = new ActionSheetDialog(this, contentOptions, null);
        dialog.isTitleShow(false).show();
        dialog.setOnOperItemClickL((parent, view, position, id) -> {
            dialog.dismiss();
            String sex = "";
            if(position == 1){
                sex = "男";
            }else if(position == 2){
                sex = "女";
            }
            EventUtil.sendEvent(new ActivityUserFilterEvent(sex));
        });
    }


    private void showConfirmDialog(){
        final NormalDialog dialog = new NormalDialog(this);
        int color = ContextCompat.getColor(this,R.color.primary_text);
        dialog.setCanceledOnTouchOutside(false);
        dialog.isTitleShow(false)
                .cornerRadius(5)
                .content("活动开始之后无法再更改成员，你确定要开始吗?")
                .contentGravity(Gravity.CENTER)
                .contentTextColor(color)
                .dividerColor(R.color.divider)
                .btnTextSize(15.5f, 15.5f)
                .btnTextColor(color,color)
                .widthScale(0.75f)
                .show();
        dialog.setOnBtnClickL(() -> dialog.dismiss(), () -> {
            dialog.dismiss();
            showLoading(R.string.label_being_something);
            getCallbacks().start(mActivity.getId());
        });
    }

    @Override
    protected BaseController getPresenter() {
        return new ActivityController();
    }

    @Override
    public void startActivitySuccess() {
        cancelLoading();
        ToastUtil.showText("活动开始成功");
    }

    public void onUserLoaded(List<ActivityUser> users, int is_sign){
        int male_count = 0;
        int female_count = 0;
        for (ActivityUser user : users){
            if("男".equals(user.getCreator().getSex())){
                male_count++;
            }else{
                female_count++;
            }
        }
        if(is_sign == 0){
            mSignUpOptions[0] = "全部 "+ users.size() + "人";
            mSignUpOptions[1] = "男生 "+ male_count + "人";
            mSignUpOptions[2] = "女生 "+ female_count + "人";
        }else{
            mJoinOptions[0] = "全部 "+ users.size() + "人";
            mJoinOptions[1] = "男生 "+ male_count + "人";
            mJoinOptions[2] = "女生 "+ female_count + "人";
        }
    }
}
