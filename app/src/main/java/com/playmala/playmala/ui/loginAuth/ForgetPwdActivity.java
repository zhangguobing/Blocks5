package com.playmala.playmala.ui.loginAuth;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.playmala.playmala.R;
import com.playmala.playmala.base.BaseController;
import com.playmala.playmala.base.BasePresenterActivity;
import com.playmala.playmala.base.ContentView;
import com.playmala.playmala.controller.LoginAuthController;
import com.playmala.playmala.util.CountDownTimerUtils;
import com.playmala.playmala.util.ToastUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * author：zhangguobing on 2017/6/16 10:22
 * email：bing901222@qq.com
 */
@ContentView(R.layout.activity_forget_pwd)
public class ForgetPwdActivity extends BasePresenterActivity<LoginAuthController.LoginAuthUiCallbacks>
   implements LoginAuthController.ForgetPwdUi {

    @Bind(R.id.et_phone)
    EditText mPhoneEditText;
    @Bind(R.id.tv_fetch_code)
    TextView mFetchCodeTv;
    @Bind(R.id.et_code)
    EditText mCodeEditText;
    @Bind(R.id.et_pwd)
    EditText mPwdEditText;

    private CountDownTimerUtils mCountDownTimerUtils;

    @Override
    protected BaseController getPresenter() {
        return new LoginAuthController();
    }


    @OnClick({R.id.tv_fetch_code,R.id.btn_update})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_fetch_code:
                fetchCode(view);
                break;
            case R.id.btn_update:
                update();
                break;
        }
    }

    private void update() {
        String phone = mPhoneEditText.getText().toString();
        int length = phone.length();
        if(length == 0){
            ToastUtil.showText("请输入手机号码");
            return;
        }
        if(length < 11 || !phone.startsWith("1")){
            ToastUtil.showText("请输入正确的手机号码");
            return;
        }
        String captcha = mCodeEditText.getText().toString().trim();
        if(captcha.length() < 6){
            ToastUtil.showText("请输入6位验证码");
            return;
        }
        String pwd = mPwdEditText.getText().toString().trim();
        if(pwd.length() < 8){
            ToastUtil.showText("请输入至少8位验证码");
            return;
        }
        showLoading(R.string.label_being_something);
        getCallbacks().resetPassword(phone,captcha,pwd);
    }

    public void fetchCode(View view){
        String phone = mPhoneEditText.getText().toString();
        int length = phone.length();
        if(length == 0){
            ToastUtil.showText("请输入手机号码");
            return;
        }
        if(length < 11 || !phone.startsWith("1")){
            ToastUtil.showText("请输入正确的手机号码");
            return;
        }
        showLoading(R.string.label_being_something);
        getCallbacks().forget(phone);
    }

    @Override
    public void receiveCaptcha(String message) {
        cancelLoading();
        ToastUtil.showText(message);
        mCountDownTimerUtils = new CountDownTimerUtils(mFetchCodeTv,60000,1000);
        mCountDownTimerUtils.start();
    }

    @Override
    public void resetPasswordComplete(String message) {
        cancelLoading();
        ToastUtil.showText(message);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mCountDownTimerUtils != null){
            mCountDownTimerUtils.cancel();
        }
    }
}
