package com.playmala.playmala;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.playmala.playmala.base.BasePermissionActivity;
import com.playmala.playmala.base.ContentView;
import com.playmala.playmala.ui.main.HomeActivity;
import com.playmala.playmala.ui.loginAuth.LoginActivity;
import com.playmala.playmala.ui.loginAuth.RegisterActivity;
import com.playmala.playmala.util.StatusBarUtil;

import butterknife.OnClick;

/**
 * author：zhangguobing on 2017/6/15 14:46
 * email：bing901222@qq.com
 */
@ContentView(R.layout.activity_splash)
public class SplashActivity extends BasePermissionActivity{

    @Override
    protected void onBeforeContentView() {
        super.onBeforeContentView();
        if(AppCookie.isLoggin()){
            HomeActivity.create(this);
            finish();
        }
    }

    public static void create(Context context){
        Intent intent = new Intent(context,SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
//        setImmersiveMode();
        StatusBarUtil.setStatusBarTranslucent(this,false);
    }

    @Override
    protected boolean isFixAndroidBug54971Workaround() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> requestPermission(new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}),2000);
    }

    /**
     * 设置全屏沉浸模式
     */
//    private void setImmersiveMode() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
//        }
//    }

    @OnClick({R.id.btn_register,R.id.btn_login})
    public void onClick(View view){
        if(view.getId() == R.id.btn_register){
            RegisterActivity.create(this);
        }else if(view.getId() == R.id.btn_login){
            LoginActivity.create(this);
        }
    }

}
