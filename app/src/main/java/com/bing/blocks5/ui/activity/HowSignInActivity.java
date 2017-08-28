package com.bing.blocks5.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bing.blocks5.ui.main.ScanBarCodeActivity;
import com.bing.blocks5.widget.TitleBar;
import com.google.zxing.client.android.encode.QREncode;
import com.bing.blocks5.R;
import com.bing.blocks5.base.BaseActivity;
import com.bing.blocks5.base.ContentView;

import butterknife.Bind;

/**
 * author：zhangguobing on 2017/7/4 17:09
 * email：bing901222@qq.com
 */
@ContentView(R.layout.activity_how_sign_in)
public class HowSignInActivity extends BaseActivity {

    private static final String EXTRA_ACTIVITY_ID = HowSignInActivity.class.getName() + ".activityId";

    @Bind(R.id.iv_barcode)
    ImageView mBarcodeImg;
    @Bind(R.id.tv_intro)
    TextView mIntroTv;

    public static void create(Context context,String activityId){
        Intent intent = new Intent(context,HowSignInActivity.class);
        intent.putExtra(EXTRA_ACTIVITY_ID, activityId);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        getTitleBar().addAction(new TitleBar.Action() {
            @Override
            public String getText() {
                return "扫一扫";
            }

            @Override
            public int getDrawable() {
                return 0;
            }

            @Override
            public void performAction(View view) {
                ScanBarCodeActivity.create(HowSignInActivity.this);
            }
        });

        String activityId = getIntent().getStringExtra(EXTRA_ACTIVITY_ID);
        final Bitmap bitmap = QREncode.encodeQRWithoutWhite(new QREncode.Builder(this)
                .setColor(getResources().getColor(R.color.black))
                .setContents("http://www.blocks5.com?activityId="+ activityId)
                .build());
        mBarcodeImg.setImageBitmap(bitmap);
        mIntroTv.setText(R.string.label_activity_barcode_intro);
    }
}
