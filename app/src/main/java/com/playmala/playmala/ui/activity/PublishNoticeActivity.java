package com.playmala.playmala.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.playmala.playmala.R;
import com.playmala.playmala.base.BaseActivity;
import com.playmala.playmala.base.ContentView;
import com.playmala.playmala.ui.activity.fragment.NoticeFragment;
import com.playmala.playmala.ui.main.adapter.FragmentAdapter;

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
    public static final String KEY_GUEST_NOTICE_CONTENT = "KEY_GUEST_NOTICE_CONTENT";
    public static final String KEY_TEAM_NOTICE_CONTENT = "KEY_TEAM_NOTICE_CONTENT";

    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;

    public static void create(Context context, int activity_id,String guestNoticeContent,String teamNoticeContent){
        Intent intent = new Intent(context,PublishNoticeActivity.class);
        intent.putExtra(KEY_ACTIVITY_ID, activity_id);
        intent.putExtra(KEY_GUEST_NOTICE_CONTENT,guestNoticeContent);
        intent.putExtra(KEY_TEAM_NOTICE_CONTENT, teamNoticeContent);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        int activity_id = getIntent().getIntExtra(KEY_ACTIVITY_ID, 0);
        String guestNotice = getIntent().getStringExtra(KEY_GUEST_NOTICE_CONTENT);
        String teamNotice = getIntent().getStringExtra(KEY_TEAM_NOTICE_CONTENT);
        List<String> titles = new ArrayList<>();
        titles.add("游客公告栏");
        titles.add("团队公告栏");
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(NoticeFragment.newInstance(NoticeFragment.TYPE_GUEST,activity_id+"",guestNotice));
        fragments.add(NoticeFragment.newInstance(NoticeFragment.TYPE_TEAM,activity_id+"", teamNotice));
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
