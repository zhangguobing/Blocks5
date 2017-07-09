package com.zjonline.blocks5.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureFragment;
import com.google.zxing.client.result.ResultParser;
import com.zjonline.blocks5.R;
import com.zjonline.blocks5.base.BaseActivity;
import com.zjonline.blocks5.base.BasePresenter;
import com.zjonline.blocks5.base.BasePresenterActivity;
import com.zjonline.blocks5.base.ContentView;
import com.zjonline.blocks5.presenter.ActivityUserPresenter;
import com.zjonline.blocks5.util.ToastUtil;

/**
 * author：zhangguobing on 2017/6/25 13:50
 * email：bing901222@qq.com
 */
@ContentView(R.layout.activity_scan_barcode)
public class ScanBarCodeActivity extends BasePresenterActivity<ActivityUserPresenter.ActivityUserUiCallbacks>
        implements CaptureFragment.OnFragmentInteractionListener,ActivityUserPresenter.ScanBarcodeUi {

    private static final int REQUEST_CAMERA_PERMISSION = 10001;

    private CaptureFragment mCaptureFragment;

    public static void create(Context context){
        Intent intent = new Intent(context,ScanBarCodeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
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
            new AlertDialog.Builder(this)
                    .setTitle(R.string.mis_permission_dialog_title)
                    .setMessage(rationale)
                    .setPositiveButton(R.string.mis_permission_dialog_ok, (dialog, which) ->
                            ActivityCompat.requestPermissions(ScanBarCodeActivity.this, new String[]{permission}, requestCode))
                    .setNegativeButton(R.string.mis_permission_dialog_cancel, null)
                    .create().show();
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

        if(result.startsWith("http://www.blocks5.com?activityId=")){
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
    protected BasePresenter getPresenter() {
        return new ActivityUserPresenter();
    }

    @Override
    public void signSuccess(String msg) {
        cancelLoading();
        ToastUtil.showText(msg);
        finish();
    }

    @Override
    public void signFail(String msg) {
        cancelLoading();
        ToastUtil.showText(msg);
        restartPreview(2000);
    }
}
