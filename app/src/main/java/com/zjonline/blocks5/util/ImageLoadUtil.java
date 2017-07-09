package com.zjonline.blocks5.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.zjonline.blocks5.R;

/**
 * author：zhangguobing on 2017/7/2 14:35
 * email：bing901222@qq.com
 */

public class ImageLoadUtil {

    /**
     * 加载普通图片
     * @param imageView
     * @param url
     * @param context
     */
    public static void loadImage(ImageView imageView, String url, Context context){
        if(TextUtils.isEmpty(url) || "null".equals(url)) return;
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .placeholder(R.drawable.bg_img_place_holder)
                .error(R.mipmap.img_error)
                .into(imageView);
    }

    /**
     * 加载用户头像
     * @param imageView
     * @param url
     * @param context
     */
    public static void loadAvatar(ImageView imageView,String url,Context context){
        if(TextUtils.isEmpty(url) || "null".equals(url)) return;
        Glide.with(context).load(url).asBitmap().placeholder(R.mipmap.ic_user_avatar_black).centerCrop().into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }
}
