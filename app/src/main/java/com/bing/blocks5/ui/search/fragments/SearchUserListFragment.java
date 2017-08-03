package com.bing.blocks5.ui.search.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.squareup.otto.Subscribe;
import com.bing.blocks5.R;
import com.bing.blocks5.base.BaseAdapter;
import com.bing.blocks5.base.BaseListFragment;
import com.bing.blocks5.base.BaseController;
import com.bing.blocks5.model.User;
import com.bing.blocks5.model.event.UserSearchEvent;
import com.bing.blocks5.controller.UserController;
import com.bing.blocks5.ui.search.adapter.UserListAdapter;
import com.bing.blocks5.ui.search.adapter.holder.UserViewHolder;
import com.bing.blocks5.ui.user.UserDetailActivity;
import com.bing.blocks5.util.EventUtil;
import com.bing.blocks5.util.ToastUtil;
import com.bing.blocks5.widget.MultiStateView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wjb on 2016/4/27.
 */
public class SearchUserListFragment extends BaseListFragment<User,UserViewHolder,UserController.UserUiCallbacks>
implements UserViewHolder.IUserOperateListener,UserController.UserListUi {

    @Override
    protected BaseController getPresenter() {
        return new UserController();
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
    protected String getEmptyTitle() {
        return "搜索不到匹配的用户";
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
