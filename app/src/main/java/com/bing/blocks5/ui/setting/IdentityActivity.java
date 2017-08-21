package com.bing.blocks5.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bing.blocks5.base.BaseController;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.bing.blocks5.R;
import com.bing.blocks5.base.BasePresenterActivity;
import com.bing.blocks5.base.ContentView;
import com.bing.blocks5.model.User;
import com.bing.blocks5.controller.UserController;
import com.bing.blocks5.util.IdCardCheckUtil;
import com.bing.blocks5.util.ToastUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * author：zhangguobing on 2017/7/1 20:38
 * email：bing901222@qq.com
 */
@ContentView(R.layout.activity_identity)
public class IdentityActivity extends BasePresenterActivity<UserController.UserUiCallbacks>
 implements Validator.ValidationListener ,UserController.IdentityUi{
    @NotEmpty(message = "请输入姓名")
    @Bind(R.id.et_identity_name)
    EditText mIdentityNameEt;
    @NotEmpty(message = "请输入身份证号")
    @Bind(R.id.et_identity_code)
    EditText mIdentityCodeEt;
    @Bind(R.id.tv_identity_notice)
    TextView mIdentityNoticeTv;
    @Bind(R.id.btn_identity)
    Button mIdentityBtn;

    private Validator mValidator;

    private static final String EXTRA_USER = "extra.user";

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        User user = getIntent().getParcelableExtra(EXTRA_USER);
        if(user != null && (user.getIdentity_state() == 3 || user.getIdentity_state() == 1)){
            mIdentityNameEt.setText(user.getIdentity_name());
            mIdentityCodeEt.setText(user.getIdentity_code());
            mIdentityBtn.setText(user.getIdentity_state() == 3 ? "已认证" : "待审核");
            mIdentityNameEt.setEnabled(false);
            mIdentityCodeEt.setEnabled(false);
            mIdentityBtn.setEnabled(false);
        }
        mIdentityNoticeTv.setText(R.string.label_identity_notice);

//        mIdentityCodeEt.setKeyListener(new DigitsKeyListener() {
//            @Override
//            protected char[] getAcceptedChars() {
//                return getResources().getString(R.string.id_can_input).toCharArray();
//            }
//        });

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
    }

    public static void create(Context context,User user){
        Intent intent = new Intent(context,IdentityActivity.class);
        intent.putExtra(EXTRA_USER,user);
        context.startActivity(intent);
    }

    @OnClick(R.id.btn_identity)
    public void onClick(View view){
        mValidator.validate();
    }

    @Override
    protected BaseController getPresenter() {
        return new UserController();
    }

    @Override
    public void onValidationSucceeded() {
        String result = IdCardCheckUtil.IDCardValidate(mIdentityCodeEt.getText().toString().trim());
        if(!TextUtils.isEmpty(result)){
            ToastUtil.showText(result);
            return;
        }
        showLoading(R.string.label_being_something);
        String identityName = mIdentityNameEt.getText().toString().trim();
        String identityCode = mIdentityCodeEt.getText().toString().trim();
        getCallbacks().identity(identityName,identityCode);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            String message = error.getCollatedErrorMessage(this);
            ToastUtil.showText(message);
        }
    }

    @Override
    public void identitySuccess(String msg) {
        finish();
    }
}
