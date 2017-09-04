package com.bing.blocks5.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.TextViewCompat;
import android.view.View;
import android.widget.TextView;

import com.bing.blocks5.AppCookie;
import com.bing.blocks5.R;
import com.bing.blocks5.base.BaseActivity;
import com.bing.blocks5.base.ContentView;
import com.bing.blocks5.model.Activity;
import com.bing.blocks5.model.event.ActivityMessageFilterEvent;
import com.bing.blocks5.model.event.ActivityNoticeUpdateEvent;
import com.bing.blocks5.ui.activity.fragment.MessageFragment;
import com.bing.blocks5.ui.main.adapter.FragmentAdapter;
import com.bing.blocks5.util.EventUtil;
import com.bing.blocks5.util.ToastUtil;
import com.bing.blocks5.widget.NoScrollViewPager;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.squareup.otto.Subscribe;

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
    NoScrollViewPager mViewPager;
    @Bind(R.id.tv_notice)
    TextView mNoticeTv;

    private Activity mActivity;

    private boolean isInNotTeamException = false;
    private String notTeamExceptionStr = "";

    public static void create(Context context, Activity activity){
        Intent intent = new Intent(context,ActivityMessageActivity.class);
        intent.putExtra(EXTRA_ACTIVITY, activity);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mActivity= getIntent().getParcelableExtra(EXTRA_ACTIVITY);
        List<String> titles = new ArrayList<>();
        titles.add("游客留言");
        titles.add("团队留言");
        List<Fragment> fragments = new ArrayList<>();
        if(mActivity != null){
            fragments.add(MessageFragment.newInstance("0",mActivity));
            fragments.add(MessageFragment.newInstance("1",mActivity));

            if(mActivity.getCreator() != null && AppCookie.getUserInfo().getId() == mActivity.getCreator().getId()){
                mNoticeTv.setVisibility(View.VISIBLE);
            }
        }
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragments, titles));
        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 1 && isInNotTeamException){
                    mViewPager.setCurrentItem(0);
                    ToastUtil.showText(notTeamExceptionStr);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.iv_back,R.id.tv_notice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_notice:
                PublishNoticeActivity.create(this,mActivity.getId(),mActivity.getGuest_notice(),mActivity.getTeam_notice());
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventUtil.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventUtil.unregister(this);
    }

    @Subscribe
    public void onNoticeContentUpdate(ActivityNoticeUpdateEvent event){
        if("0".equals(event.notice_type)){
            mActivity.setGuest_notice(event.notice_content);
        }else{
            mActivity.setTeam_notice(event.notice_content);
        }
    }

    public void handleNotTeamException(String notTeamExceptionStr){
        mViewPager.setCurrentItem(0);
        mViewPager.setNoScroll(true);
        isInNotTeamException = true;
        this.notTeamExceptionStr = notTeamExceptionStr;
    }

    //    private void showFilterDialog() {
//        final String[] contentOptions = {"全部留言", "楼主留言"};
//        final ActionSheetDialog dialog = new ActionSheetDialog(this, contentOptions, null);
//        dialog.isTitleShow(false).show();
//        dialog.setOnOperItemClickL((parent, view, position, id) -> {
//            dialog.dismiss();
//            showLoading(R.string.label_being_loading);
//            EventUtil.sendEvent(new ActivityMessageFilterEvent(position == 1));
//        });
//    }
}
