package com.playmala.playmala.ui.activity.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.playmala.playmala.R;
import com.playmala.playmala.base.BaseFragment;
import com.playmala.playmala.base.ContentView;
import com.playmala.playmala.model.Activity;
import com.playmala.playmala.util.ActivityDataConvert;
import com.google.zxing.client.android.encode.QREncode;

import butterknife.Bind;

/**
 * Created by tian on 2017/9/4.
 */
@ContentView(R.layout.fragment_sign_in_two)
public class SignInTwoFragment extends BaseFragment {
    @Bind(R.id.iv_barcode)
    ImageView mBarcodeImg;
    @Bind(R.id.time)
    TextView mTimeTv;
    @Bind(R.id.status)
    TextView mStatusTv;

    public static final String EXTRA_ACTIVITY = "extra_activity";


    public static SignInTwoFragment newInstance(Activity activity) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_ACTIVITY,activity);
        SignInTwoFragment fragment = new SignInTwoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Activity activity = getArguments().getParcelable(EXTRA_ACTIVITY);
        final Bitmap bitmap = QREncode.encodeQRWithoutWhite(new QREncode.Builder(getContext())
                .setColor(getResources().getColor(R.color.black))
                .setContents("http://www.blocks5.com?activityId="+ activity.getId())
                .build());
        mBarcodeImg.setImageBitmap(bitmap);

        mTimeTv.setText("时间：" + activity.getBegin_at() +"-"+ activity.getEnd_at());
        String statusStr = ActivityDataConvert.getActivityStateById(activity.getState()+"");
        if(activity.getState() != 2){
            statusStr = statusStr + "，不可签到";
        }
        mStatusTv.setText(statusStr);
    }
}
