package com.bing.blocks5.ui.loginAuth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.bing.blocks5.AppCookie;
import com.bing.blocks5.R;
import com.bing.blocks5.album.Album;
import com.bing.blocks5.base.BaseController;
import com.bing.blocks5.base.BasePresenterActivity;
import com.bing.blocks5.base.ContentView;
import com.bing.blocks5.controller.UserController;
import com.bing.blocks5.ui.main.MainActivity;
import com.bing.blocks5.util.ActivityStack;
import com.bing.blocks5.util.ImageLoadUtil;
import com.bing.blocks5.util.QiniuUploadUtils;
import com.bing.blocks5.util.ToastUtil;
import com.bing.blocks5.widget.TitleBar;
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
    private static final int ACTIVITY_REQUEST_PREVIEW_PHOTO = 102;

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
            titleBar.setActionTextColor(ContextCompat.getColor(this,R.color.white));
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
        MainActivity.create(this);
        ActivityStack.create().appLogin();
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

    /**
     * Take a picture from fromCamera.
     */
    private void fromCamera() {
        Album.camera(this)
                .requestCode(ACTIVITY_REQUEST_TAKE_PICTURE)
//                .imagePath() // Specify the image path, optional.
                .start();
    }

    /**
     * Preview image.
     *
     * @param position current position.
     */
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
                            mUploadProgressDialog.show();
                        }

                        @Override
                        public void onSuccess(String fileUrl) {
                            mUploadProgressDialog.dismiss();
                            ImageLoadUtil.loadAvatar(mAvatarImage,fileUrl,RegisterNextActivity.this);
                            mAvatarUrl = fileUrl;
                        }

                        @Override
                        public void onError(int errorCode, String msg) {
                            mUploadProgressDialog.dismiss();
                            ToastUtil.showText("上传异常");
                        }

                        @Override
                        public void onProgress(int progress) {
                            mUploadProgressDialog.setProgress(progress);
                        }
                    });
                }
                break;
            }
//            case ACTIVITY_REQUEST_TAKE_PICTURE: {
//                if (resultCode == RESULT_OK) {
//                    List<String> imageList = Album.parseResult(data);
//                    mImageList.addAll(imageList);
//                    refreshImage();
//                }
//                break;
//            }
//            case ACTIVITY_REQUEST_PREVIEW_PHOTO: {
//                if (resultCode == RESULT_OK) {
//                    mImageList = Album.parseResult(data);
//                    refreshImage();
//                }
//                break;
//            }
        }
    }

    /**
     * Process selection results.
     */
//    private void refreshImage() {
//        if (mImageList == null || mImageList.size() == 0) {
//            mAvatarImage.setImageResource(R.mipmap.ic_user_avatar_white);
//        } else {
//            Album.getAlbumConfig().getImageLoader().loadImage(mAvatarImage, mImageList.get(0), mAvatarImage.getMeasuredWidth(), mAvatarImage.getMeasuredHeight());
//        }
//    }

    private void completeRegisterClick(){
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
            showLoading(R.string.label_being_something);
            String sex = mRadioGroup.getCheckedRadioButtonId() == R.id.rb_male ? "男" : "女";
            getCallbacks().register(AppCookie.getToken(),mNickNameEt.getText().toString().trim(),
                    sex,mPwdEt.getText().toString().trim(),mAvatarUrl);
        });
    }
}
