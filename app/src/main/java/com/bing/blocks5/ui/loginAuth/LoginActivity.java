package com.bing.blocks5.ui.loginAuth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bing.blocks5.R;
import com.bing.blocks5.base.BaseController;
import com.bing.blocks5.base.BasePresenterActivity;
import com.bing.blocks5.base.ContentView;
import com.bing.blocks5.controller.LoginAuthController;
import com.bing.blocks5.model.LoginBean;
import com.bing.blocks5.ui.main.MainActivity;
import com.bing.blocks5.util.ActivityStack;
import com.bing.blocks5.util.AppUtil;
import com.bing.blocks5.util.CountDownTimerUtils;
import com.bing.blocks5.util.ToastUtil;
import com.bing.blocks5.util.ValidatorUtil;
import com.flyco.dialog.widget.NormalDialog;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * author：zhangguobing on 2017/6/14 15:02
 * email：bing901222@qq.com
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BasePresenterActivity<LoginAuthController.LoginAuthUiCallbacks>
   implements LoginAuthController.LoginUi{

    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    @Bind(R.id.tv_switch_mode)
    TextView mSwitchModeTextView;

    private EditText mCodeTelEditText;
    private EditText mCodeCodeEditText;
    private EditText mPwdTelEditText;
    private EditText mPwdPwdEdiText;
    private TextView  mFetchCodeTextView;

    private CountDownTimerUtils countDownTimerUtils;

    @Override
    protected BaseController getPresenter() {
        return new LoginAuthController();
    }

    public static void create(Context context){
        Intent intent = new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initViewPager(mViewPager);
    }

    private void initViewPager(ViewPager viewPager) {
        LayoutInflater inflater = getLayoutInflater();
        View codeView = inflater.inflate(R.layout.layout_code_login,null);
        View pwdView = inflater.inflate(R.layout.layout_pwd_login,null);
        mCodeTelEditText = (EditText) codeView.findViewById(R.id.et_phone);
        mCodeCodeEditText = (EditText) codeView.findViewById(R.id.et_code);
        mPwdTelEditText = (EditText) pwdView.findViewById(R.id.et_phone);
        mPwdPwdEdiText = (EditText) pwdView.findViewById(R.id.et_pwd);
        mFetchCodeTextView = (TextView) codeView.findViewById(R.id.btn_fetch_code);
        mFetchCodeTextView.setOnClickListener(this::fetchCode);
        ArrayList<View> viewList = new ArrayList<>();
        viewList.add(codeView);
        viewList.add(pwdView);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTitle(position == 0 ? "验证码登录":"密码登录");
                mSwitchModeTextView.setText(position == 0 ? "切换密码登录":"切换验证码登录");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }
        };
        viewPager.setAdapter(pagerAdapter);
    }

    public void fetchCode(View view){
        String code = mCodeTelEditText.getText().toString();
        if(code.length() == 0){
            ToastUtil.showText("请输入手机号码");
            return;
        }
        if(code.length() < 11 || !code.startsWith("1")){
            ToastUtil.showText("请输入正确的手机号码");
            return;
        }
        showLoading(R.string.label_being_something);
        getCallbacks().captcha(code,"login");
    }


    public void switchMode(View view){
        TextView textView = (TextView) view;
        if(mViewPager.getCurrentItem() == 0){
            textView.setText("切换验证码登录");
            mViewPager.setCurrentItem(1,true);
        }else{
            textView.setText("密码登录");
            mViewPager.setCurrentItem(0,true);
        }
    }

    @OnClick({R.id.tv_switch_mode,R.id.btn_login,R.id.tv_forget_pwd})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_switch_mode:
                switchMode(view);
                break;
            case R.id.btn_login:
                login(view);
                break;
            case R.id.tv_forget_pwd:
                forgetPwd(view);
                break;
        }
    }

    public void forgetPwd(View view){
        Intent intent = new Intent(this,ForgetPwdActivity.class);
        startActivity(intent);
    }

    public void login(View view){
        String phone;
        String captcha;
        if(mViewPager.getCurrentItem() == 0){
            int length = mCodeTelEditText.getText().length();
            if(mCodeTelEditText.getText().length() == 0){
                ToastUtil.showText("请输入手机号码");
                return;
            }
            if(length < 11 || !mCodeTelEditText.getText().toString().startsWith("1")){
                ToastUtil.showText("请输入正确的手机号码");
                return;
            }
            if(mCodeCodeEditText.getText().length() < 6){
                ToastUtil.showText("请输入6位验证码");
                return;
            }
            phone = mCodeTelEditText.getText().toString();
            captcha = mCodeCodeEditText.getText().toString();
        }else{
            String tel = mPwdTelEditText.getText().toString().trim();
            int length = tel.length();
            if(length == 0){
                ToastUtil.showText("请输入手机号码");
                return;
            }
            if(length < 11 || !tel.startsWith("1")){
                ToastUtil.showText("请输入正确的手机号码");
                return;
            }
            if(mPwdPwdEdiText.getText().toString().trim().length() < 8){
                ToastUtil.showText("请输入8位登录密码");
                return;
            }
            phone = mPwdTelEditText.getText().toString().trim();
            captcha = mPwdPwdEdiText.getText().toString().trim();
        }
        showLoading(R.string.label_being_login);
        getCallbacks().login(phone,captcha);
    }

    @Override
    public void loginFinish(LoginBean loginBean) {
       cancelLoading();
       if(TextUtils.isEmpty(loginBean.getUser().getNick_name())){
           NormalDialog dialog = new NormalDialog(this);
           int color = ContextCompat.getColor(this,R.color.primary_text);
           int redColor = ContextCompat.getColor(this,R.color.red);
           dialog.setCanceledOnTouchOutside(false);
           dialog.isTitleShow(false)
                   .cornerRadius(5)
                   .content("你还没有注册，是否去注册！")
                   .contentGravity(Gravity.CENTER)
                   .contentTextColor(color)
                   .dividerColor(R.color.divider)
                   .btnTextSize(15.5f, 15.5f)
                   .btnTextColor(color,redColor)
                   .widthScale(0.75f)
                   .btnText("取消","确定")
                   .show();
           dialog.setOnBtnClickL(dialog::dismiss, () -> {
               RegisterNextActivity.create(this);
               dialog.dismiss();
           });
       }else{
           MainActivity.create(this);
           ActivityStack.create().appLogin();
       }
    }

    @Override
    public void receiveCaptcha(String message) {
        cancelLoading();
        ToastUtil.showText(message);
        countDownTimerUtils =  new CountDownTimerUtils(mFetchCodeTextView,60000,1000);
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
