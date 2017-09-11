package com.playmala.playmala.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.playmala.playmala.model.Activity;
import com.playmala.playmala.ui.main.ScanBarCodeActivity;
import com.playmala.playmala.widget.TitleBar;
import com.google.zxing.client.android.encode.QREncode;
import com.playmala.playmala.R;
import com.playmala.playmala.base.BaseActivity;
import com.playmala.playmala.base.ContentView;

import butterknife.Bind;

/**
 * author：zhangguobing on 2017/7/4 17:09
 * email：bing901222@qq.com
 */
@ContentView(R.layout.activity_how_sign_in)
public class HowSignInActivity extends BaseActivity {
    private static final String EXTRA_ACTIVITY = HowSignInActivity.class.getName() + ".activity";
    private static final String EXTRA_PAGE_FROM = HowSignInActivity.class.getName() + ".pageFrom";

    public static final int PAGE_FROM_ACTIVITY_DETAIL = 0;
    public static final int PAGE_FROM_SIGN_IN = 1;

    @Bind(R.id.iv_barcode)
    ImageView mBarcodeImg;
    @Bind(R.id.tv_intro)
    TextView mIntroTv;

    public static void create(Context context,Activity activity, int pageFrom){
        Intent intent = new Intent(context,HowSignInActivity.class);
        intent.putExtra(EXTRA_ACTIVITY, activity);
        intent.putExtra(EXTRA_PAGE_FROM, pageFrom);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Activity activity = getIntent().getParcelableExtra(EXTRA_ACTIVITY);
        int page_from = getIntent().getIntExtra(EXTRA_PAGE_FROM, 0);


        getTitleBar().addAction(new TitleBar.Action() {
            @Override
            public String getText() {
                return page_from == PAGE_FROM_ACTIVITY_DETAIL ? "签到": "扫一扫";
            }

            @Override
            public int getDrawable() {
                return 0;
            }

            @Override
            public void performAction(View view) {
                if(page_from == PAGE_FROM_ACTIVITY_DETAIL){
                    SignInActivity.create(HowSignInActivity.this, activity);
                }else{
                    ScanBarCodeActivity.create(HowSignInActivity.this);
                }
            }
        });

        final Bitmap bitmap = QREncode.encodeQRWithoutWhite(new QREncode.Builder(this)
                .setColor(getResources().getColor(R.color.black))
                .setContents("http://www.blocks5.com?activityId="+ activity.getId())
                .build());
        mBarcodeImg.setImageBitmap(bitmap);
        mIntroTv.setText(R.string.label_activity_barcode_intro);
    }
}
