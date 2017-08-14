package com.bing.blocks5.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.bing.blocks5.AppCookie;
import com.bing.blocks5.R;
import com.bing.blocks5.base.BaseActivity;
import com.bing.blocks5.base.ContentView;
import com.bing.blocks5.ui.activity.fragment.CreatorActivityListFragment;
import com.bing.blocks5.ui.main.adapter.FragmentAdapter;
import com.bing.blocks5.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * author：zhangguobing on 2017/7/7 13:42
 * email：bing901222@qq.com
 * 我创建的活动
 */
@ContentView(R.layout.activity_my_created_activity)
public class CreatedActivity extends BaseActivity{
    private static final String EXTRA_USER_ID = "extra_user_id";
    private static final String EXTRA_TITLE = "extra_title";

    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;

    private int user_id;
    private String mTitle;

    public static void create(Context context,int user_id, String title){
        Intent intent = new Intent(context, CreatedActivity.class);
        intent.putExtra(EXTRA_USER_ID,user_id);
        intent.putExtra(EXTRA_TITLE,title);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        user_id = getIntent().getIntExtra(EXTRA_USER_ID,0);
        mTitle = getIntent().getStringExtra(EXTRA_TITLE);
        setTitleBar();
        initTabs();
    }

    private void setTitleBar(){
        setTitle(mTitle);
        if(getTitleBar() != null && user_id == AppCookie.getUserInfo().getId()){
                getTitleBar().addAction(new TitleBar.Action() {
                    @Override
                    public String getText() {
                        return null;
                    }

                    @Override
                    public int getDrawable() {
                        return R.mipmap.ic_add;
                    }

                    @Override
                    public void performAction(View view) {
                        AddActivityActivity.create(CreatedActivity.this);
                    }
                });
        }
    }

    private void initTabs(){
        List<String> titles = new ArrayList<>();
        titles.add("活动创建中");
        titles.add("活动进行中");
        titles.add("活动结束");
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(CreatorActivityListFragment.newInstance(0,user_id));
        fragments.add(CreatorActivityListFragment.newInstance(1,user_id));
        fragments.add(CreatorActivityListFragment.newInstance(2,user_id));
        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragments, titles));
        mTabLayout.setupWithViewPager(mViewPager);
    }

}
