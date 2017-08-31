package com.bing.blocks5.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.bing.blocks5.R;
import com.bing.blocks5.base.BaseController;
import com.bing.blocks5.base.BasePresenterActivity;
import com.bing.blocks5.base.ContentView;
import com.bing.blocks5.model.Activity;
import com.bing.blocks5.model.User;
import com.bing.blocks5.controller.SearchController;
import com.bing.blocks5.ui.main.adapter.FragmentAdapter;
import com.bing.blocks5.ui.search.fragments.SearchActivityListFragment;
import com.bing.blocks5.ui.search.fragments.SearchUserListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * author：zhangguobing on 2017/7/1 21:35
 * email：bing901222@qq.com
 */
@ContentView(R.layout.activity_search)
public class SearchActivity extends BasePresenterActivity<SearchController.SearchUiCallbacks>
    implements SearchController.SearchUi{

    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    @Bind(R.id.edit_search)
    EditText mSearchEt;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        initTabs();
        mSearchEt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch();
            }
            return true;
        });
    }

    private void doSearch(){
        String id = mSearchEt.getText().toString().trim();
        if(TextUtils.isEmpty(id)) return;
        showLoading(R.string.label_being_something);
        if(mViewPager.getCurrentItem() == 0){
            getCallbacks().getActivityListByIdAndState(Integer.parseInt(id), "1,2,3,4");
        }else{
            getCallbacks().getUserById(Integer.parseInt(id));
        }
    }

    private void initTabs(){
        List<String> titles = new ArrayList<>();
        titles.add("活动");
        titles.add("用户");
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new SearchActivityListFragment());
        fragments.add(new SearchUserListFragment());
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragments, titles));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public static void create(Context context){
        Intent intent = new Intent(context,SearchActivity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.iv_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    protected BaseController getPresenter() {
        return new SearchController();
    }

    @Override
    public void activityListCallback(List<Activity> activities) {
        cancelLoading();
        mSearchEt.setText("");
    }

    @Override
    public void userCallBack(User user) {
        cancelLoading();
        mSearchEt.setText("");
    }
}
