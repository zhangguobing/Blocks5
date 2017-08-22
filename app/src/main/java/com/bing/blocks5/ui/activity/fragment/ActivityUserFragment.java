package com.bing.blocks5.ui.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bing.blocks5.ui.activity.SignUpListActivity;
import com.squareup.otto.Subscribe;
import com.bing.blocks5.R;
import com.bing.blocks5.base.BaseAdapter;
import com.bing.blocks5.base.BaseListFragment;
import com.bing.blocks5.base.BaseController;
import com.bing.blocks5.model.ActivityUser;
import com.bing.blocks5.model.event.ActivityUserFilterEvent;
import com.bing.blocks5.controller.ActivityUserController;
import com.bing.blocks5.ui.activity.adapter.ActivityUserAdapter;
import com.bing.blocks5.ui.activity.adapter.holder.ActivityUserViewHolder;
import com.bing.blocks5.ui.user.UserDetailActivity;
import com.bing.blocks5.util.EventUtil;

import java.util.List;

/**
 * author：zhangguobing on 2017/7/10 14:25
 * email：bing901222@qq.com
 */

public class ActivityUserFragment extends BaseListFragment<ActivityUser,ActivityUserViewHolder,ActivityUserController.ActivityUserUiCallbacks>
        implements ActivityUserController.SignUpList {

    private static final String EXTRA_ACTIVITY_ID = "extra_activity_id";
    private static final String EXTRA_IS_SIGN = "extra_is_sign";

    private int activity_id;
    private int is_sign;

    private boolean isFirstLoad = true;

    public static ActivityUserFragment newInstance(int is_sign,int activity_id) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_ACTIVITY_ID,activity_id);
        args.putInt(EXTRA_IS_SIGN, is_sign);
        ActivityUserFragment fragment = new ActivityUserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        activity_id = getArguments().getInt(EXTRA_ACTIVITY_ID);
        is_sign = getArguments().getInt(EXTRA_IS_SIGN);
        loadData();
    }

    @Override
    protected String getEmptyTitle() {
        return "暂无成员";
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventUtil.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventUtil.unregister(this);
    }

    private void loadData(){
        getCallbacks().getUsersByActivityId(is_sign, activity_id,"", 0, mPage = 1, "15");
    }


    @Override
    protected void refreshPage() {
        loadData();
    }

    @Override
    protected BaseController getPresenter() {
        return new ActivityUserController();
    }

    @Override
    protected void onItemClick(int position, ActivityUser item) {
        UserDetailActivity.create(getContext(),item.getUser_id());
    }

    @Override
    protected BaseAdapter<ActivityUser, ActivityUserViewHolder> getAdapter() {
        return new ActivityUserAdapter();
    }

    @Override
    public void onSignUpList(List<ActivityUser> users) {
        cancelLoading();
        onFinishRequest(users);
        if(isFirstLoad){
            if(getActivity() instanceof SignUpListActivity){
                ((SignUpListActivity)getActivity()).onUserLoaded(users,is_sign);
            }
            isFirstLoad = false;
        }
    }

    @Subscribe
    public void onFilterChange(ActivityUserFilterEvent event){
        if(isVisible() && getUserVisibleHint()){
            showLoading(R.string.label_being_loading);
            getCallbacks().getUsersByActivityId(is_sign, activity_id,event.sex, 0 , mPage = 1, "15");
        }
    }
}
