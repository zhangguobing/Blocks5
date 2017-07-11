package com.bing.blocks5.ui.common.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
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
    private int pos;

    public GalleryAdapter(Activity activity, String[] urls, int w, int h, int x, int y, int pos) {
        this.activity = activity;
        this.urls = urls;
        this.locationH = h;
        this.locationW = w;
        this.locationX = x;
        this.locationY = y;
        this.pos = pos;
    }

    @Override
    public int getCount() {
        return urls.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        SmoothImageView smoothImageView = (SmoothImageView) LayoutInflater.from(activity).inflate(R.layout.item_gallery, null);
        container.addView(smoothImageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        smoothImageView.setOriginalInfo(locationW, locationH, locationX, locationY);
        smoothImageView.transformIn();

        Picasso.with(activity).load(urls[position]).into(smoothImageView);

        smoothImageView.setOnTransformListener(mode -> {
            if (mode == 2) {
                activity.finish();
                activity.overridePendingTransition(0, 0);
            }
        });
        smoothImageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {
                activity.finish();
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }

            @Override
            public void onOutsidePhotoTap() {
                activity.finish();
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
}
