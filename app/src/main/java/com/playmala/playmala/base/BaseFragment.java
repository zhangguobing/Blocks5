package com.playmala.playmala.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.playmala.playmala.util.AndroidBug54971Workaround;
import com.playmala.playmala.util.HideKeyBoardUtil;

import butterknife.ButterKnife;

/**
 * author：zhangguobing on 2017/6/15 11:02
 * email：bing901222@qq.com
 */

public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResId(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AndroidBug54971Workaround.assistActivity(view);
        ButterKnife.bind(this, view);
        handleArguments(getArguments());
        initView(savedInstanceState);
        HideKeyBoardUtil.init(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    protected void initView(Bundle savedInstanceState) {

    }

    protected int getLayoutResId() {
        for (Class c = getClass(); c != Fragment.class; c = c.getSuperclass()) {
            ContentView annotation = (ContentView) c.getAnnotation(ContentView.class);
            if (annotation != null) {
                return annotation.value();
            }
        }
        return 0;
    }

    protected void handleArguments(Bundle arguments) {}

    protected final void showLoading(@StringRes int textResId) {
        showLoading(getString(textResId));
    }

    protected final void showLoading(String text) {
        Activity hostActivity = getActivity();
        if (hostActivity instanceof BaseActivity) {
            ((BaseActivity) hostActivity).showLoading(text);
        }
    }

    protected final void cancelLoading() {
        Activity hostActivity = getActivity();
        if (hostActivity instanceof BaseActivity) {
            ((BaseActivity) hostActivity).cancelLoading();
        }
    }
}
