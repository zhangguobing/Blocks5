package com.playmala.playmala.ui.loginAuth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.playmala.playmala.AppCookie;
import com.playmala.playmala.R;
import com.playmala.playmala.album.Album;
import com.playmala.playmala.base.BaseController;
import com.playmala.playmala.base.BasePresenterActivity;
import com.playmala.playmala.base.ContentView;
import com.playmala.playmala.controller.UserController;
import com.playmala.playmala.ui.main.HomeActivity;
import com.playmala.playmala.util.ActivityStack;
import com.playmala.playmala.util.AsyncRun;
import com.playmala.playmala.util.ImageLoadUtil;
import com.playmala.playmala.util.QiniuUploadUtils;
import com.playmala.playmala.util.ToastUtil;
import com.playmala.playmala.widget.TitleBar;
import com.flyco.dialog.widget.NormalDialog;
import com.kaopiz.kprogresshud.KProgressHUD;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * author：zhangguobing on 2017/6/20 17:27
 * email：bing901222@qq.com
 * 完善信息页面
 */
@ContentView(R.layout.activity_login_next)
public class RegisterNextActivity extends BasePresenterActivity<UserController.UserUiCallbacks>
        implements UserController.LoginNextUi {

    private static final int ACTIVITY_REQUEST_SELECT_PHOTO = 100;
    private static final int ACTIVITY_REQUEST_TAKE_PICTURE = 101;

    @Bind(R.id.radio_group)
    RadioGroup mRadioGroup;
    @Bind(R.id.et_nick_name)
    EditText mNickNameEt;
    @Bind(R.id.et_pwd)
    EditText mPwdEt;
    @Bind(R.id.et_confirm_pwd)
    EditText mConfirmPwdEt;
    @Bind(R.id.iv_avatar)
    ImageView mAvatarImage;

    private KProgressHUD mUploadProgressDialog;

    private String mAvatarUrl = "";

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setTitleBar();
        mUploadProgressDialog = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.PIE_DETERMINATE)
                .setLabel(getString(R.string.lable_being_upload)).setMaxProgress(100);
    }

    private void setTitleBar() {
        TitleBar titleBar = getTitleBar();
        if(titleBar != null){
//            titleBar.setActionTextColor(ContextCompat.getColor(this,R.color.white));
            titleBar.addAction(new TitleBar.Action() {
                @Override
                public String getText() {
                    return "退出登录";
                }

                @Override
                public int getDrawable() {
                    return 0;
                }

                @Override
                public void performAction(View view) {
                    AppCookie.saveUserInfo(null);
                    AppCookie.saveToken(null);
                    finish();
                }
            });
        }
    }


    public static void create(Context context) {
        Intent intent = new Intent(context, RegisterNextActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected BaseController getPresenter() {
        return new UserController();
    }

    @Override
    public void registerSuccess(String message) {
        cancelLoading();
        ToastUtil.showText(message);
        HomeActivity.create(this);
        ActivityStack.create().appLogin();
//         LoginActivity.create(this);
    }

    @OnClick({R.id.btn_complete_register,R.id.iv_avatar})
    public void onClick(View view) {
        if(view.getId() == R.id.btn_complete_register){
            completeRegisterClick();
        }else if(view.getId() == R.id.iv_avatar){
            fromAlbum();
        }
    }

    /**
     * Select image from fromAlbum.
     */
    private void fromAlbum() {
        Album.album(this)
                .requestCode(ACTIVITY_REQUEST_SELECT_PHOTO)
                .toolBarColor(ContextCompat.getColor(this, R.color.red))
                .statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                .navigationBarColor(ActivityCompat.getColor(this, R.color.colorPrimaryDark))
                .selectCount(1)
                .columnCount(3)
                .title("相册")
                .start();
    }


//    private void previewImage(int position) {
//        Album.gallery(this)
//                .requestCode(ACTIVITY_REQUEST_PREVIEW_PHOTO)
//                .toolBarColor(ContextCompat.getColor(this, R.color.red))
//                .statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
//                .navigationBarColor(ActivityCompat.getColor(this, R.color.colorPrimaryDark))
//                .checkedList(mImageList) // Image list.
//                .currentPosition(position) // Preview first to show the first few.
//                .checkFunction(true) // Does the user have an anti-selection when previewing.
//                .start();
//
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTIVITY_REQUEST_SELECT_PHOTO: {
                if (resultCode == RESULT_OK) {
                    String filePath = Album.parseResult(data).get(0);
                    QiniuUploadUtils.getInstance().uploadImage(filePath, new QiniuUploadUtils.QiniuUploadUtilsListener() {
                        @Override
                        public void onStart() {
                            AsyncRun.runInMain(() -> mUploadProgressDialog.show());

                        }

                        @Override
                        public void onSuccess(String filePath,String destUrl) {
                            AsyncRun.runInMain(() -> {
                                mUploadProgressDialog.dismiss();
                                ImageLoadUtil.loadAvatar(mAvatarImage,filePath,RegisterNextActivity.this);
                                mAvatarUrl = destUrl;
                            });
                        }

                        @Override
                        public void onError(int errorCode, String msg) {
                            AsyncRun.runInMain(() -> {
                                mUploadProgressDialog.dismiss();
                                ToastUtil.showText("上传失败," + msg);
                            });

                        }

                        @Override
                        public void onProgress(int progress) {
                            AsyncRun.runInMain(() -> mUploadProgressDialog.setProgress(progress));
                        }
                    });
                }
                break;
            }
        }
    }

    private void completeRegisterClick(){
        if (mNickNameEt.getText().toString().trim().length() == 0) {
            ToastUtil.showText("请输入昵称");
            return;
        }
        if (mRadioGroup.getCheckedRadioButtonId() == -1) {
            ToastUtil.showText("请选择性别");
            return;
        }
        if (mPwdEt.getText().toString().trim().length() == 0) {
            ToastUtil.showText("请输入密码");
            return;
        }
        if (mConfirmPwdEt.getText().toString().trim().length() == 0) {
            ToastUtil.showText("请输入确认密码");
            return;
        }
        if (mPwdEt.getText().toString().trim().length() < 8) {
            ToastUtil.showText("请输入至少8位密码");
            return;
        }
        if (!mPwdEt.getText().toString().trim().equals(mConfirmPwdEt.getText().toString().trim())) {
            ToastUtil.showText("密码不一致");
            return;
        }

        if(TextUtils.isEmpty(mAvatarUrl)){
            ToastUtil.showText("请上传头像");
            return;
        }

        showCannotUpdateSexDialog();
    }

    private void showCannotUpdateSexDialog(){
        final NormalDialog dialog = new NormalDialog(this);
        int color = ContextCompat.getColor(this,R.color.primary_text);
        dialog.setCanceledOnTouchOutside(false);
        dialog.isTitleShow(true)
                .cornerRadius(5)
                .title("提示")
                .content("提交完成后将不能更改性别")
                .contentGravity(Gravity.CENTER)
                .contentTextColor(color)
                .dividerColor(R.color.divider)
                .btnTextSize(15.5f, 15.5f)
                .btnTextColor(color,color)
                .widthScale(0.75f)
                .show();
        dialog.setOnBtnClickL(dialog::dismiss, () -> {
            dialog.dismiss();
            showLoading(R.string.label_being_something);
            String sex = mRadioGroup.getCheckedRadioButtonId() == R.id.rb_male ? "男" : "女";
            getCallbacks().register(AppCookie.getToken(),mNickNameEt.getText().toString().trim(),
                    sex,mPwdEt.getText().toString().trim(),mAvatarUrl);
        });
    }
}
