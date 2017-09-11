package com.playmala.playmala.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.playmala.playmala.R;
import com.playmala.playmala.ui.common.adapter.GalleryAdapter;
import com.playmala.playmala.widget.TitleBar;

import java.util.ArrayList;

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
    public static final String PHOTO_IS_SHOW_TITLE_BAR = "PHOTO_IS_SHOW_TITLE_BAR";

    public static final String KEY_OUTPUT_IMAGE_URLS_LIST = "key_output_image_urls_list";

    private int locationX;
    private int locationY;
    private int locationW;
    private int locationH;
    private int position;
    private ArrayList<String> urls;
    //是否显示标题栏 默认不显示
    private boolean isShowTitleBar;

    private TextView mIndicatorTv;

    private ViewPager mViewPager;
    private GalleryAdapter mGalleryAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle b = intent.getExtras();
            urls = b.getStringArrayList(PHOTO_SOURCE_ID);

            position = intent.getIntExtra(PHOTO_SELECT_POSITION, 0);
            locationX = intent.getIntExtra(PHOTO_SELECT_X_TAG, 0);
            locationY = intent.getIntExtra(PHOTO_SELECT_Y_TAG, 0);
            locationW = intent.getIntExtra(PHOTO_SELECT_W_TAG, 0);
            locationH = intent.getIntExtra(PHOTO_SELECT_H_TAG, 0);
            isShowTitleBar = intent.getBooleanExtra(PHOTO_IS_SHOW_TITLE_BAR,false);
        }

        initTitleBar();

        mIndicatorTv = (TextView) findViewById(R.id.tv_indicator);
        mIndicatorTv.setText(position+1 + "/" + urls.size());

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(urls.size());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mIndicatorTv.setText(position+1 + "/" + urls.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mGalleryAdapter = new GalleryAdapter(this, urls, locationW, locationH,
                locationX, locationY, isShowTitleBar);
        mViewPager.setAdapter(mGalleryAdapter);
        mViewPager.setCurrentItem(position);
    }


    /**
     * 删除当前图片
     */
    private void deleteCurrentImage(){
        int position = mViewPager.getCurrentItem();
        mGalleryAdapter.remove(position);
    }

    private void initTitleBar(){
        if(isShowTitleBar){
            TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
            titleBar.setVisibility(View.VISIBLE);
            titleBar.setTitle("相册");
            titleBar.setTitleColor(ContextCompat.getColor(this,R.color.white));
            titleBar.setLeftImageResource(R.mipmap.ic_navigate_back);
            titleBar.setLeftClickListener(view -> finish());
            titleBar.addAction(new TitleBar.Action() {
                @Override
                public String getText() {
                    return null;
                }

                @Override
                public int getDrawable() {
                    return R.mipmap.ic_delete;
                }

                @Override
                public void performAction(View view) {
                    //删除图片
                    deleteCurrentImage();
                    if(urls.size() > 0){
                        mIndicatorTv.setText("1/" + urls.size());
                        mViewPager.setCurrentItem(0);
                    }else{
                        titleBar.removeActionAt(0);
                        mIndicatorTv.setVisibility(View.GONE);
                    }
                }
            });
            titleBar.addAction(new TitleBar.Action() {
                @Override
                public String getText() {
                    return null;
                }

                @Override
                public int getDrawable() {
                    return R.drawable.album_ic_done_white;
                }

                @Override
                public void performAction(View view) {
                    //完成
                    Intent intent = new Intent();
                    intent.putExtra(KEY_OUTPUT_IMAGE_URLS_LIST,mGalleryAdapter.getList());
                    setResult(RESULT_OK,intent);
                    finish();
                }
            });
        }
    }

    /**
     * Resolve the newest photo url list
     * @param intent
     * @return
     */
    public static @NonNull ArrayList<String> parseResult(Intent intent){
       ArrayList<String> urlList = intent.getStringArrayListExtra(KEY_OUTPUT_IMAGE_URLS_LIST);
       if(urlList == null){
           urlList = new ArrayList<>();
       }
       return urlList;
    }

}
