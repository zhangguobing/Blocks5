package com.bing.blocks5.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.bing.blocks5.R;
import com.bing.blocks5.base.BaseActivity;
import com.bing.blocks5.base.ContentView;
import com.bing.blocks5.model.Activity;
import com.bing.blocks5.ui.activity.fragment.SignInFragment;
import com.bing.blocks5.ui.activity.fragment.SignInTwoFragment;
import com.bing.blocks5.ui.main.adapter.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by tian on 2017/8/20.
 * 活动签到页
 */
@ContentView(R.layout.activity_sign_in)
public class SignInActivity extends BaseActivity{

    public static final String KEY_ACTIVITY = "KEY_ACTIVITY";

    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;

    private Activity activity;


    public static void create(Context context, Activity activity){
        Intent intent = new Intent(context,SignInActivity.class);
        intent.putExtra(KEY_ACTIVITY, activity);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        activity = getIntent().getParcelableExtra(KEY_ACTIVITY);
        if(activity.getState() == 2){
            List<String> titles = new ArrayList<>();
            titles.add("未签到");
            titles.add("已签到");
            List<Fragment> fragments = new ArrayList<>();
            fragments.add(SignInFragment.newInstance(SignInFragment.TYPE_NO_CONFIRM,activity));
            fragments.add(SignInFragment.newInstance(SignInFragment.TYPE_HAS_CONFIRMED,activity));
            mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragments, titles));
            mTabLayout.setupWithViewPager(mViewPager);
        }else{
            List<String> titles = new ArrayList<>();
            titles.add("签到");
            List<Fragment> fragments = new ArrayList<>();
            fragments.add(SignInTwoFragment.newInstance(activity));
            mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragments, titles));
            int color = ContextCompat.getColor(this, R.color.white);
            mTabLayout.setTabTextColors(color,color);
            mTabLayout.setupWithViewPager(mViewPager);
        }
    }

    @OnClick({R.id.tv_how_sign_in,R.id.iv_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_how_sign_in:
                HowSignInActivity.create(this, activity, HowSignInActivity.PAGE_FROM_SIGN_IN);
                break;
        }
    }


}
