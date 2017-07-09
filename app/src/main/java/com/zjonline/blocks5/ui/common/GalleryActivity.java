package com.zjonline.blocks5.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.zjonline.blocks5.R;
import com.zjonline.blocks5.ui.common.adapter.GalleryAdapter;

/**
 * author：zhangguobing on 2017/7/5 14:35
 * email：bing901222@qq.com
 */
public class GalleryActivity extends AppCompatActivity{

    public static final String PHOTO_SOURCE_ID = "PHOTO_SOURCE_ID";
    public static final String PHOTO_SELECT_POSITION = "PHOTO_SELECT_POSITION";
    public static final String PHOTO_SELECT_X_TAG = "PHOTO_SELECT_X_TAG";
    public static final String PHOTO_SELECT_Y_TAG = "PHOTO_SELECT_Y_TAG";
    public static final String PHOTO_SELECT_W_TAG = "PHOTO_SELECT_W_TAG";
    public static final String PHOTO_SELECT_H_TAG = "PHOTO_SELECT_H_TAG";

    private int locationX;
    private int locationY;
    private int locationW;
    private int locationH;
    private int position;
    private String[] urls;

    private TextView mIndicatorTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle b = intent.getExtras();
            urls = b.getStringArray(PHOTO_SOURCE_ID);

            position = intent.getIntExtra(PHOTO_SELECT_POSITION, 0);
            locationX = intent.getIntExtra(PHOTO_SELECT_X_TAG, 0);
            locationY = intent.getIntExtra(PHOTO_SELECT_Y_TAG, 0);
            locationW = intent.getIntExtra(PHOTO_SELECT_W_TAG, 0);
            locationH = intent.getIntExtra(PHOTO_SELECT_H_TAG, 0);
        }

        mIndicatorTv = (TextView) findViewById(R.id.tv_indicator);
        mIndicatorTv.setText(position+1 + "/" + urls.length);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(urls.length);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mIndicatorTv.setText(position+1 + "/" + urls.length);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        GalleryAdapter galleryAdapter = new GalleryAdapter(this, urls, locationW, locationH, locationX, locationY, position);
        viewPager.setAdapter(galleryAdapter);
        viewPager.setCurrentItem(position);
    }
}
