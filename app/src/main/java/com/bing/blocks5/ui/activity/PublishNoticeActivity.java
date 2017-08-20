package com.bing.blocks5.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.bing.blocks5.R;
import com.bing.blocks5.base.BaseActivity;
import com.bing.blocks5.base.ContentView;
import com.bing.blocks5.ui.activity.fragment.NoticeFragment;
import com.bing.blocks5.ui.main.adapter.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by tian on 2017/8/20.
 * 发布公告栏
 */
@ContentView(R.layout.activity_publish_notice)
public class PublishNoticeActivity extends BaseActivity {

    public static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";

    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;

    public static void create(Context context, int activity_id){
        Intent intent = new Intent(context,PublishNoticeActivity.class);
        intent.putExtra(KEY_ACTIVITY_ID, activity_id);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        int activity_id = getIntent().getIntExtra(KEY_ACTIVITY_ID, 0);
        List<String> titles = new ArrayList<>();
        titles.add("游客公告栏");
        titles.add("团队公告栏");
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(NoticeFragment.newInstance(NoticeFragment.TYPE_GUEST,activity_id+""));
        fragments.add(NoticeFragment.newInstance(NoticeFragment.TYPE_TEAM,activity_id+""));
        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragments, titles));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
