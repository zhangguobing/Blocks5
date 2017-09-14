package com.playmala.playmala.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.widget.TextView;

import com.flyco.dialog.widget.NormalDialog;
import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureFragment;
import com.google.zxing.client.result.ResultParser;
import com.playmala.playmala.R;
import com.playmala.playmala.base.BaseController;
import com.playmala.playmala.base.BasePresenterActivity;
import com.playmala.playmala.base.ContentView;
import com.playmala.playmala.controller.ActivityUserController;
import com.playmala.playmala.util.ToastUtil;

import butterknife.Bind;

/**
 * author：zhangguobing on 2017/6/25 13:50
 * email：bing901222@qq.com
 */
@ContentView(R.layout.activity_scan_barcode)
public class ScanBarCodeActivity extends BasePresenterActivity<ActivityUserController.ActivityUserUiCallbacks>
        implements CaptureFragment.OnFragmentInteractionListener,ActivityUserController.ScanBarcodeUi {

    @Bind(R.id.tv_tips)
    TextView mTipsTv;

    private static final int REQUEST_CAMERA_PERMISSION = 10001;

    private CaptureFragment mCaptureFragment;

    public static void create(Context context){
        Intent intent = new Intent(context,ScanBarCodeActivity.class);
        context.startActivity(intent);
    }

    public static void create(Context context, boolean isEditTips){
        Intent intent = new Intent(context,ScanBarCodeActivity.class);
        intent.putExtra("isEditTips", isEditTips);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        boolean isEditTips = getIntent().getBooleanExtra("isEditTips", false);
        if(isEditTips){
            mTipsTv.setText("1、在活动开始时间前半个小时到活动结束时间内，扫描楼主二维码，活动状态变为已完成，奖励相应信用。");
        }
        showCameraWithCheck();
    }

    private void showCameraWithCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.CAMERA,
                    getString(R.string.permission_rationale_camera),
                    REQUEST_CAMERA_PERMISSION);
        }else{
            showCamera();
        }
    }

    private void showCamera(){
        mCaptureFragment = (CaptureFragment) getSupportFragmentManager().findFragmentById(R.id.main_frame);
        if(mCaptureFragment ==null){
            mCaptureFragment =  CaptureFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame, mCaptureFragment).commitAllowingStateLoss();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CAMERA_PERMISSION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                showCamera();
            }else{
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void requestPermission(final String permission, String rationale, final int requestCode){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){
            final NormalDialog dialog = new NormalDialog(this);
            int color = ContextCompat.getColor(this,R.color.primary_text);
            dialog.setCanceledOnTouchOutside(false);
            dialog.isTitleShow(true)
                    .title(getString(R.string.mis_permission_dialog_title))
                    .cornerRadius(5)
                    .content(rationale)
                    .contentGravity(Gravity.CENTER)
                    .contentTextColor(color)
                    .dividerColor(R.color.divider)
                    .btnTextSize(15.5f, 15.5f)
                    .btnTextColor(color,color)
                    .widthScale(0.75f)
                    .btnText(getString(R.string.mis_permission_dialog_ok),getString(R.string.mis_permission_dialog_cancel))
                    .show();
            dialog.setOnBtnClickL(() -> {
                dialog.dismiss();
                ActivityCompat.requestPermissions(ScanBarCodeActivity.this, new String[]{permission}, requestCode);
            }, dialog::dismiss);
        }else{
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    private void restartPreview(long delayTime){
        if(mCaptureFragment !=null) mCaptureFragment.restartPreviewAfterDelay(delayTime);
    }

    @Override
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        String result = ResultParser.parseResult(rawResult).toString();

        if(result.startsWith("http://www.blocks5.com?activityId=")
                || result.startsWith("https://www.blocks5.com?activityId=")){
            Uri uri = Uri.parse(result);
            String activityId = uri.getQueryParameter("activityId");
            showLoading(R.string.label_being_something);
            getCallbacks().sign(Integer.parseInt(activityId));
        }else{
            ToastUtil.showText("不合法的二维码");
            restartPreview(2000);
        }
    }

    @Override
    protected BaseController getPresenter() {
        return new ActivityUserController();
    }

    @Override
    public void signSuccess(String msg) {
        cancelLoading();
        ToastUtil.showText("签到成功");
        finish();
    }

    @Override
    public void signFail(String msg) {
        cancelLoading();
        ToastUtil.showText(msg);
        restartPreview(2000);
    }
}
