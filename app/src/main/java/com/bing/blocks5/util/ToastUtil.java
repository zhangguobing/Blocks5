package com.bing.blocks5.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bing.blocks5.Blocks5App;
import com.bing.blocks5.R;

/**
 * author：zhangguobing on 2017/6/14 09:18
 * email：bing901222@qq.com
 * 自定义的toast,用来提示当前操作是否成功
 */

public class ToastUtil {

    private static Toast mToast;

    public static void init(Context context){
        if(mToast == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            View layout = inflater.inflate(R.layout.layout_toast, null);
            mToast = new Toast(context);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.setView(layout);
        }
    }

    public static void showText(String text) {
        if(mToast != null){
            View view = mToast.getView();
            int padding = DensityUtil.dip2px(Blocks5App.getContext(),8);
            view.setPadding(padding,padding,padding,padding);
            ((TextView)view.findViewById(R.id.tv_toast_msg)).setText(text);
            ImageView image = (ImageView)view.findViewById(R.id.iv_toast);
            image.setVisibility(View.GONE);
            mToast.show();
        }
    }

    public static void showText(int res) {
        showText(Blocks5App.getContext().getString(res));
    }


    public static void showImageAndText(int imageResid,String text) {
        if(mToast != null){
            View view = mToast.getView();
            int padding = DensityUtil.dip2px(Blocks5App.getContext(),20);
            view.setPadding(padding,padding,padding,padding);
            ((TextView)view.findViewById(R.id.tv_toast_msg)).setText(text);
            ImageView image = (ImageView)view.findViewById(R.id.iv_toast);
            image.setVisibility(View.VISIBLE);
            image.setImageResource(imageResid);
            mToast.show();
        }
    }

    public static void showImageAndText(int imageResid,int res) {
        showImageAndText(imageResid, Blocks5App.getContext().getString(res));
    }

}
