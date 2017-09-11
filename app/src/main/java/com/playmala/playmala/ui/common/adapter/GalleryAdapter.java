package com.playmala.playmala.ui.common.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.playmala.playmala.R;
import com.playmala.playmala.widget.SmoothImageView;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * author：zhangguobing on 2017/7/5 14:38
 * email：bing901222@qq.com
 */

public class GalleryAdapter extends PagerAdapter{

    private Activity activity;
    private ArrayList<String> urls;
    private int locationW, locationH, locationX, locationY;
    private boolean isShowTitleBar;

    public GalleryAdapter(Activity activity, ArrayList<String> urls, int w, int h, int x, int y,boolean isShowTitleBar) {
        this.activity = activity;
        this.urls = urls;
        this.locationH = h;
        this.locationW = w;
        this.locationX = x;
        this.locationY = y;
        this.isShowTitleBar = isShowTitleBar;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        SmoothImageView smoothImageView = (SmoothImageView) LayoutInflater.from(activity).inflate(R.layout.item_gallery,container,false);
        container.addView(smoothImageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        smoothImageView.setOriginalInfo(locationW, locationH, locationX, locationY);
        smoothImageView.transformIn();

        Glide.with(activity)
                .load(urls.get(position))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(R.mipmap.img_error)
                .into(smoothImageView);

        if(!isShowTitleBar){
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
        }
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

    /**
     * 删除某张图片
     * @param position
     */
    public void remove(int position){
        urls.remove(position);
        notifyDataSetChanged();
    }

    public ArrayList<String> getList(){
        return urls;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
