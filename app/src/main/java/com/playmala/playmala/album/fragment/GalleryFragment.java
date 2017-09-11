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
package com.playmala.playmala.album.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.playmala.playmala.R;
import com.playmala.playmala.album.GalleryWrapper;
import com.playmala.playmala.album.adapter.BasicPreviewAdapter;
import com.playmala.playmala.album.adapter.PathPreviewAdapter;
import com.playmala.playmala.album.base.NoFragment;
import com.playmala.playmala.album.impl.GalleryCallback;
import com.playmala.playmala.album.util.SelectorUtils;
import com.playmala.playmala.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Gallery.</p>
 * Created by yanzhenjie on 17-3-29.
 */
public class GalleryFragment extends NoFragment {

    private GalleryCallback mCallback;

    private int mToolBarColor;

    private View mCheckParent;
    private AppCompatCheckBox mCheckBox;

    private int mCurrentItemPosition;
    private ViewPager mViewPager;
    private TitleBar mTitleBar;

    private List<String> mCheckedPaths;
    private boolean[] mCheckedList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (GalleryCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mCallback = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.album_fragment_preview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mCheckParent = view.findViewById(R.id.layout_gallery_preview_bottom);
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
                GalleryWrapper.KEY_INPUT_TOOLBAR_COLOR,
                ContextCompat.getColor(getContext(), R.color.album_ColorPrimary));

        // noinspection ConstantConditions
        mTitleBar.setBackgroundColor(mToolBarColor);
        mTitleBar.getBackground().mutate().setAlpha(200);

        this.mCurrentItemPosition = argument.getInt(GalleryWrapper.KEY_INPUT_CURRENT_POSITION, 0);
        if (mCurrentItemPosition >= mCheckedPaths.size()) mCurrentItemPosition = 0;

        boolean hasCheck = argument.getBoolean(GalleryWrapper.KEY_INPUT_CHECK_FUNCTION, false);
        if (!hasCheck) mCheckParent.setVisibility(View.GONE);

        initializeCheckBox();
        initializeViewPager();

        setCheckedCountUI(getCheckCount());
    }

    /**
     * Bind the preview picture collection.
     *
     * @param imagePaths image list of local.
     */
    public void bindImagePaths(List<String> imagePaths) {
        this.mCheckedPaths = imagePaths;
        int length = mCheckedPaths.size();
        mCheckedList = new boolean[length];
        for (int i = 0; i < length; i++) {
            mCheckedList[i] = true;
        }
    }

    private void initializeCheckBox() {
        //noinspection RestrictedApi
        mCheckBox.setSupportButtonTintList(SelectorUtils.createColorStateList(Color.WHITE, mToolBarColor));
        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = mCheckBox.isChecked();
                mCheckedList[mCurrentItemPosition] = isChecked;
                setCheckedCountUI(getCheckCount());
            }
        });
    }

    private void initializeViewPager() {
        if (mCheckedPaths.size() > 2)
            mViewPager.setOffscreenPageLimit(2);

        BasicPreviewAdapter previewAdapter = new PathPreviewAdapter(mCheckedPaths);
        mViewPager.setAdapter(previewAdapter);
        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentItemPosition = position;
                mCheckBox.setChecked(mCheckedList[position]);
                // noinspection ConstantConditions
                mTitleBar.setTitle(mCurrentItemPosition + 1 + " / " + mCheckedPaths.size());
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
                finishStr += "(" + count + " / " + mCheckedPaths.size() + ")";
                return finishStr;
            }

            @Override
            public int getDrawable() {
                return 0;
            }

            @Override
            public void performAction(View view) {
                ArrayList<String> patList = new ArrayList<>();
                for (int i = 0; i < mCheckedList.length; i++) {
                    if (mCheckedList[i]) {
                        patList.add(mCheckedPaths.get(i));
                    }
                }
                mCallback.onGalleryResult(patList);
            }
        });
    }

    /**
     * Get check item count.
     *
     * @return number.
     */
    private int getCheckCount() {
        int i = 0;
        for (boolean b : mCheckedList) {
            if (b) i++;
        }
        return i;
    }


}
