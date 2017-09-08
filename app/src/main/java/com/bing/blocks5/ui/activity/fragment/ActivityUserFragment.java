package com.bing.blocks5.ui.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bing.blocks5.model.Activity;
import com.bing.blocks5.model.event.ActivityUserRefreshEvent;
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
        implements ActivityUserController.SignUpList, ActivityUserViewHolder.UserOperateListener {

    private static final String EXTRA_ACTIVITY= "extra_activity";
    private static final String EXTRA_IS_Join = "extra_is_join";

    private Activity activity;
    private int is_join;

    public static ActivityUserFragment newInstance(int is_sign, Activity activity) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_ACTIVITY,activity);
        args.putInt(EXTRA_IS_Join, is_sign);
        ActivityUserFragment fragment = new ActivityUserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        activity = bundle.getParcelable(EXTRA_ACTIVITY);
        is_join = bundle.getInt(EXTRA_IS_Join);
        super.initView(savedInstanceState);
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
        getCallbacks().getUsersByActivityId("", activity.getId(), "", is_join, mPage = 1, "15");
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
        return new ActivityUserAdapter(this, activity);
    }

    @Override
    public void onSignUpList(List<ActivityUser> users) {
        cancelLoading();
        onFinishRequest(users);
        if(mPage == 1){
            if(getActivity() instanceof SignUpListActivity){
                ((SignUpListActivity)getActivity()).onUserLoaded(users, is_join);
            }
        }
    }

    @Override
    public void onStatusChange() {
        cancelLoading();
        EventUtil.sendEvent(new ActivityUserRefreshEvent());
    }

    @Subscribe
    public void onFilterChange(ActivityUserFilterEvent event){
        if(isVisible() && getUserVisibleHint()){
            showLoading(R.string.label_being_loading);
            getCallbacks().getUsersByActivityId(is_join+"", activity.getId(),event.sex, 0 , mPage = 1, "15");
        }
    }

    @Override
    public void onJoinClick(ActivityUser user) {
        showLoading(R.string.label_being_something);
        getCallbacks().setUserState(activity.getId(), user.getUser_id(), user.getState() == 0 ? 1 : 0);
    }

    @Subscribe
    public void onUserRefresh(ActivityUserRefreshEvent event){
        loadData();
    }
}
