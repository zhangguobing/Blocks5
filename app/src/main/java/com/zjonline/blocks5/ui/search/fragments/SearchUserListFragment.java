package com.zjonline.blocks5.ui.search.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.squareup.otto.Subscribe;
import com.zjonline.blocks5.R;
import com.zjonline.blocks5.base.BaseAdapter;
import com.zjonline.blocks5.base.BaseListFragment;
import com.zjonline.blocks5.base.BasePresenter;
import com.zjonline.blocks5.model.User;
import com.zjonline.blocks5.model.event.UserSearchEvent;
import com.zjonline.blocks5.presenter.UserPresenter;
import com.zjonline.blocks5.ui.search.adapter.UserListAdapter;
import com.zjonline.blocks5.ui.search.adapter.holder.UserViewHolder;
import com.zjonline.blocks5.ui.user.UserDetailActivity;
import com.zjonline.blocks5.util.EventUtil;
import com.zjonline.blocks5.util.ToastUtil;
import com.zjonline.blocks5.widget.MultiStateView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wjb on 2016/4/27.
 */
public class SearchUserListFragment extends BaseListFragment<User,UserViewHolder,UserPresenter.UserUiCallbacks>
implements UserViewHolder.IUserOperateListener,UserPresenter.UserListUi {

    @Override
    protected BasePresenter getPresenter() {
        return new UserPresenter();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
    }

    @Override
    protected int getDefaultState() {
        return MultiStateView.STATE_CONTENT;
    }

    @Override
    protected void onItemClick(int position, User item) {
        UserDetailActivity.create(getActivity(),item);
    }

    @Override
    protected boolean getEnableRefresh() {
        return false;
    }

    @Override
    protected boolean getEnableLoadMore() {
        return false;
    }

    @Override
    protected BaseAdapter<User, UserViewHolder> getAdapter() {
        return new UserListAdapter(this);
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

    @Subscribe
    public void onUserList(UserSearchEvent event){
        List<User> list = new ArrayList<>();
        list.add(event.user);
        onFinishRequest(list);
    }

    @Override
    public void onFollowClick(User user) {
        showLoading(R.string.label_being_something);
        getCallbacks().follow(user.getId()+"");
    }

    @Override
    public void followSuccess() {
        cancelLoading();
        ToastUtil.showText("关注成功");
        User user = mAdapter.getItems().get(0);
        user.setIs_follow(1);
        mAdapter.setItem(0,user);
    }
}
