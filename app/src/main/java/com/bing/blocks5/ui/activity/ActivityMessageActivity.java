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
import com.bing.blocks5.model.Activity;
import com.bing.blocks5.model.event.ActivityMessageFilterEvent;
import com.bing.blocks5.ui.activity.fragment.MessageFragment;
import com.bing.blocks5.ui.main.adapter.FragmentAdapter;
import com.bing.blocks5.util.EventUtil;
import com.flyco.dialog.widget.ActionSheetDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * author：zhangguobing on 2017/7/20 10:31
 * email：bing901222@qq.com
 */
@ContentView(R.layout.activity_activity_message)
public class ActivityMessageActivity extends BaseActivity {

    private static final String EXTRA_ACTIVITY = "activity";

    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;

    public static void create(Context context, Activity activity){
        Intent intent = new Intent(context,ActivityMessageActivity.class);
        intent.putExtra(EXTRA_ACTIVITY, activity);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Activity activity = getIntent().getParcelableExtra(EXTRA_ACTIVITY);
        List<String> titles = new ArrayList<>();
        titles.add("游客留言");
        titles.add("队友留言");
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(MessageFragment.newInstance("0",activity.getId()+"",activity.getGuest_notice(),activity.getGuest_notice_time()));
        fragments.add(MessageFragment.newInstance("1",activity.getId()+"",activity.getTeam_notice(),activity.getTeam_notice_time()));
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragments, titles));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @OnClick({R.id.iv_back,R.id.iv_filter})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_filter:
                showFilterDialog();
                break;
        }
    }


    private void showFilterDialog() {
        final String[] contentOptions = {"全部留言", "楼主留言"};
        final ActionSheetDialog dialog = new ActionSheetDialog(this, contentOptions, null);
        dialog.isTitleShow(false).show();
        dialog.setOnOperItemClickL((parent, view, position, id) -> {
            dialog.dismiss();
            showLoading(R.string.label_being_loading);
            EventUtil.sendEvent(new ActivityMessageFilterEvent(position == 1));
        });
    }
}
