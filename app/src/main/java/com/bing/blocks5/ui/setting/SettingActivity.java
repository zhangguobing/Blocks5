package com.bing.blocks5.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bing.blocks5.base.BaseController;
import com.flyco.dialog.widget.NormalDialog;
import com.bing.blocks5.AppCookie;
import com.bing.blocks5.R;
import com.bing.blocks5.SplashActivity;
import com.bing.blocks5.api.ApiClient;
import com.bing.blocks5.base.BasePresenterActivity;
import com.bing.blocks5.base.ContentView;
import com.bing.blocks5.model.User;
import com.bing.blocks5.controller.UserController;
import com.bing.blocks5.util.ActivityDataConvert;
import com.bing.blocks5.util.ActivityStack;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * author：zhangguobing on 2017/6/29 17:53
 * email：bing901222@qq.com
 */
@ContentView(R.layout.activity_setting)
public class SettingActivity extends BasePresenterActivity<UserController.UserUiCallbacks>
    implements UserController.SettingUi{

    @Bind(R.id.tv_certification)
    TextView mCertTv;

    private User mUser;

    public static void create(Context context){
        Intent intent = new Intent(context,SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showLoading(R.string.label_being_loading);
        getCallbacks().getUserById(AppCookie.getUserInfo().getId());
    }

    @OnClick({R.id.ll_certification,R.id.ll_feedback,R.id.ll_vote,R.id.ll_about_us,R.id.btn_exit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_certification:
                IdentityActivity.create(this,mUser);
                break;
            case R.id.btn_exit:
                showExitDialog();
                break;
        }
    }

    private void showExitDialog(){
        final NormalDialog dialog = new NormalDialog(this);
        int color = ContextCompat.getColor(this,R.color.primary_text);
        dialog.setCanceledOnTouchOutside(false);
        dialog.isTitleShow(false)
                .cornerRadius(5)
                .content("你确认退出?")
                .contentGravity(Gravity.CENTER)
                .contentTextColor(color)
                .dividerColor(R.color.divider)
                .btnTextSize(15.5f, 15.5f)
                .btnTextColor(color,color)
                .widthScale(0.75f)
                .show();
        dialog.setOnBtnClickL(dialog::dismiss, () -> {
            dialog.dismiss();
            AppCookie.saveUserInfo(null);
            AppCookie.saveToken(null);
            ApiClient.getInstance().setToken(null);
            SplashActivity.create(SettingActivity.this);
            ActivityStack.create().finishAll();
        });
    }

    @Override
    protected BaseController getPresenter() {
        return new UserController();
    }

    @Override
    public void loadUserCallback(User user) {
        cancelLoading();
        mUser = user;
        String identityState = ActivityDataConvert.getIdentityStateById(user.getIdentity_state()+"");
        mCertTv.setText("实名认证（"+ identityState +")");
    }
}
