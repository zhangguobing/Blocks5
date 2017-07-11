/*
 * Copyright © Yan Zhenjie. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bing.blocks5.album.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bing.blocks5.R;
import com.bing.blocks5.album.AlbumWrapper;
import com.bing.blocks5.album.adapter.AlbumImagePreviewAdapter;
import com.bing.blocks5.album.adapter.BasicPreviewAdapter;
import com.bing.blocks5.album.base.NoFragment;
import com.bing.blocks5.album.entity.AlbumImage;
import com.bing.blocks5.album.util.SelectorUtils;
import com.bing.blocks5.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * <p>Preview the pictures in the folder in enlarged form.</p>
 * Created by Yan Zhenjie on 2017/3/25.
 */
public class AlbumPreviewFragment extends NoFragment {

    private int mToolBarColor;

    private AppCompatCheckBox mCheckBox;
    private TitleBar mTitleBar;

    private ViewPager mViewPager;
    private List<AlbumImage> mAlbumImages = new ArrayList<>(1);
    private List<AlbumImage> mCheckedImages = new ArrayList<>(1);
    private int mCurrentItemPosition;

    private int mAllowSelectCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.album_fragment_preview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mCheckBox = (AppCompatCheckBox) view.findViewById(R.id.cb_album_check);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mTitleBar = (TitleBar) view.findViewById(R.id.title_bar);
        mTitleBar.setTitleColor(ContextCompat.getColor(getContext(),R.color.white));
        mTitleBar.setSubTitleColor(ContextCompat.getColor(getContext(),R.color.white));
        mTitleBar.setActionTextColor(ContextCompat.getColor(getContext(),R.color.white));
        mTitleBar.setLeftImageResource(R.mipmap.ic_navigate_back);
        mTitleBar.setLeftClickListener(v -> {
            finish();
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle argument = getArguments();
        mToolBarColor = argument.getInt(
                AlbumWrapper.KEY_INPUT_TOOLBAR_COLOR,
                ContextCompat.getColor(getContext(), R.color.album_ColorPrimary));
        mAllowSelectCount = argument.getInt(AlbumWrapper.KEY_INPUT_LIMIT_COUNT, Integer.MAX_VALUE);

        // noinspection ConstantConditions
        mTitleBar.setBackgroundColor(mToolBarColor);
        mTitleBar.getBackground().mutate().setAlpha(120);

        initializeCheckBox();
        initializeViewPager();

        setCheckedCountUI(mCheckedImages.size());
    }

    /**
     * Bind the click folder.
     *
     * @param albumImages image list.
     */
    public void bindAlbumImages(List<AlbumImage> albumImages, List<AlbumImage> checkedImages, int currentItemPosition) {
        this.mAlbumImages.addAll(albumImages);
        this.mCheckedImages = checkedImages;
        this.mCurrentItemPosition = currentItemPosition;
    }

    private void initializeCheckBox() {
        //noinspection RestrictedApi
        mCheckBox.setSupportButtonTintList(SelectorUtils.createColorStateList(Color.WHITE, mToolBarColor));
        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = mCheckBox.isChecked();
                AlbumImage albumImage = mAlbumImages.get(mCurrentItemPosition);
                albumImage.setChecked(isChecked);

                if (isChecked) {
                    if (mCheckedImages.size() >= mAllowSelectCount) {
                        Toast.makeText(
                                getContext(),
                                String.format(Locale.getDefault(), getString(R.string.album_check_limit), mAllowSelectCount),
                                Toast.LENGTH_LONG).show();

                        mCheckBox.setChecked(false);
                        albumImage.setChecked(false);
                    } else {
                        mCheckedImages.add(albumImage);
                    }
                } else {
                    mCheckedImages.remove(albumImage);
                }
                setCheckedCountUI(mCheckedImages.size());
            }
        });
    }

    private void initializeViewPager() {
        if (mAlbumImages.size() > 2)
            mViewPager.setOffscreenPageLimit(2);

        BasicPreviewAdapter previewAdapter = new AlbumImagePreviewAdapter(mAlbumImages);
        mViewPager.setAdapter(previewAdapter);
        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentItemPosition = position;
                AlbumImage albumImage = mAlbumImages.get(mCurrentItemPosition);
                mCheckBox.setChecked(albumImage.isChecked());
                // noinspection ConstantConditions
                mTitleBar.setTitle(mCurrentItemPosition + 1 + " / " + mAlbumImages.size());
            }
        };
        mViewPager.addOnPageChangeListener(pageChangeListener);
        mViewPager.setCurrentItem(mCurrentItemPosition);
        // Forced call.
        pageChangeListener.onPageSelected(mCurrentItemPosition);
    }

    /**
     * Set the number of selected pictures.
     *
     * @param count number.
     */
    private void setCheckedCountUI(int count) {
        mTitleBar.removeAllActions();
        mTitleBar.addAction(new TitleBar.Action() {
            @Override
            public String getText() {
                String finishStr = getString(R.string.album_menu_finish);
                finishStr += "(" + count + " / " + mAllowSelectCount + ")";
                return finishStr;
            }

            @Override
            public int getDrawable() {
                return 0;
            }

            @Override
            public void performAction(View view) {
                 setResult(RESULT_OK);
                 finish();
            }
        });
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.album_menu_preview, menu);
//        mFinishMenuItem = menu.findItem(R.id.album_menu_finish);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int itemId = item.getItemId();
//        if (itemId == R.id.album_menu_finish) {
//            setResult(RESULT_OK);
//            finish();
//        }
//        return true;
//    }
}
