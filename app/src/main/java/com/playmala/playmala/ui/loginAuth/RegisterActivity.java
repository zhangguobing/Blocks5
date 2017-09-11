package com.playmala.playmala.ui.loginAuth;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.playmala.playmala.R;
import com.playmala.playmala.base.BaseController;
import com.playmala.playmala.base.BasePresenterActivity;
import com.playmala.playmala.base.ContentView;
import com.playmala.playmala.controller.LoginAuthController;
import com.playmala.playmala.model.LoginBean;
import com.playmala.playmala.util.CountDownTimerUtils;
import com.playmala.playmala.util.ToastUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * author：zhangguobing on 2017/6/15 17:27
 * email：bing901222@qq.com
 */
@ContentView(R.layout.activity_register)
public class RegisterActivity extends BasePresenterActivity<LoginAuthController.LoginAuthUiCallbacks>
   implements LoginAuthController.LoginUi{

    @Bind(R.id.et_phone)
    EditText mPhoneEditText;
    @Bind(R.id.et_code)
    EditText mCodeEditText;
    @Bind(R.id.btn_fetch_code)
    Button mFetchCodeBtn;

    private CountDownTimerUtils countDownTimerUtils;

    @Override
    protected BaseController getPresenter() {
        return new LoginAuthController();
    }

    public static void create(Context context){
        Intent intent = new Intent(context,RegisterActivity.class);
        context.startActivity(intent);
    }

    public void fetchCode(View view){
        String code = mPhoneEditText.getText().toString();
        if(code.length() == 0){
            ToastUtil.showText("请输入手机号码");
            return;
        }
        if(code.length() < 11 || !code.startsWith("1")){
            ToastUtil.showText("请输入正确的手机号码");
            return;
        }
        showLoading(R.string.label_being_something);
        getCallbacks().captcha(code,"register");
    }

    @OnClick({R.id.btn_next,R.id.btn_fetch_code})
    public void onClick(View view){
        if(view.getId() == R.id.btn_fetch_code){
            fetchCode(view);
        }else if(view.getId() == R.id.btn_next){
            next();
        }
    }

    private void next() {
        String phone = mPhoneEditText.getText().toString().trim();
        if(phone.length() == 0){
            ToastUtil.showText("请输入手机号码");
            return;
        }
        if(phone.length() < 11 || !phone.startsWith("1")){
            ToastUtil.showText("请输入正确的手机号码");
            return;
        }
        String captcha = mCodeEditText.getText().toString().trim();
        if(mCodeEditText.getText().length() < 6){
            ToastUtil.showText("请输入6位验证码");
            return;
        }
        showLoading(R.string.label_being_login);
        getCallbacks().login(phone,captcha);
    }

    @Override
    public void loginFinish(LoginBean loginBean) {
        cancelLoading();
        RegisterNextActivity.create(this);
    }

    @Override
    public void receiveCaptcha(String message) {
        cancelLoading();
        ToastUtil.showText(message);
        countDownTimerUtils = new CountDownTimerUtils(mFetchCodeBtn,60000,1000);
        countDownTimerUtils.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimerUtils != null){
            countDownTimerUtils.cancel();
        }
    }
}
