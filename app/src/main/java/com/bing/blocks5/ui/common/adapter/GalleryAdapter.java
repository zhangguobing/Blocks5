package com.bing.blocks5.ui.common.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bing.blocks5.R;
import com.bing.blocks5.widget.SmoothImageView;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * author：zhangguobing on 2017/7/5 14:38
 * email：bing901222@qq.com
 */

public class GalleryAdapter extends PagerAdapter{

    private Activity activity;
    private String[] urls;
    private int locationW, locationH, locationX, locationY;
    private Bitmap bitmap;

    public GalleryAdapter(Activity activity, String[] urls, int w, int h, int x, int y, Bitmap bitmap) {
        this.activity = activity;
        this.urls = urls;
        this.locationH = h;
        this.locationW = w;
        this.locationX = x;
        this.locationY = y;
        this.bitmap = bitmap;
    }

    @Override
    public int getCount() {
        return urls.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        SmoothImageView smoothImageView = (SmoothImageView) LayoutInflater.from(activity).inflate(R.layout.item_gallery,container,false);
        container.addView(smoothImageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        smoothImageView.setOriginalInfo(locationW, locationH, locationX, locationY);
        smoothImageView.transformIn();

        Glide.with(activity)
                .load(urls[position])
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(bitmap == null ? null : new BitmapDrawable(bitmap))
                .error(R.mipmap.img_error)
                .into(smoothImageView);

        smoothImageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {
                finishActivity();
            }

            @Override
            public void onOutsidePhotoTap() {
                finishActivity();
            }
        });
        return smoothImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private void finishActivity(){
        activity.finish();
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
