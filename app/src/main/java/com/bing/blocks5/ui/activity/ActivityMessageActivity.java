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
import com.bing.blocks5.ui.activity.fragment.MessageFragment;
import com.bing.blocks5.ui.main.adapter.FragmentAdapter;
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
public class ActivityMessageActivity extends BaseActivity{

    private static final String EXTRA_ACTIVITY_ID = "activity_id";

    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;

    public static void create(Context context,String activity_id){
        Intent intent = new Intent(context,ActivityMessageActivity.class);
        intent.putExtra(EXTRA_ACTIVITY_ID, activity_id);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        String activity_id = getIntent().getStringExtra(EXTRA_ACTIVITY_ID);
        List<String> titles = new ArrayList<>();
        titles.add("游客留言");
        titles.add("队友留言");
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(MessageFragment.newInstance("0",activity_id));
        fragments.add(MessageFragment.newInstance("1",activity_id));
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
        });
    }
}
