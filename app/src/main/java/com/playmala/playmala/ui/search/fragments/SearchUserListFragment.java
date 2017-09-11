package com.playmala.playmala.ui.search.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;

import com.flyco.dialog.widget.NormalDialog;
import com.squareup.otto.Subscribe;
import com.playmala.playmala.R;
import com.playmala.playmala.base.BaseAdapter;
import com.playmala.playmala.base.BaseListFragment;
import com.playmala.playmala.base.BaseController;
import com.playmala.playmala.model.User;
import com.playmala.playmala.model.event.UserSearchEvent;
import com.playmala.playmala.controller.UserController;
import com.playmala.playmala.ui.search.adapter.UserListAdapter;
import com.playmala.playmala.ui.search.adapter.holder.UserViewHolder;
import com.playmala.playmala.ui.user.UserDetailActivity;
import com.playmala.playmala.util.EventUtil;
import com.playmala.playmala.util.ToastUtil;
import com.playmala.playmala.widget.MultiStateView;

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
    protected boolean isShowRefreshWhenEmpty() {
        return false;
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
        if(event.user == null){
            mAdapter.clearItems();
            return;
        }
        List<User> list = new ArrayList<>();
        list.add(event.user);
        onFinishRequest(list);
    }

    @Override
    public void onFollowClick(User user) {
        if(user.getIs_follow() == 0){
            showLoading(R.string.label_being_something);
            getCallbacks().follow(user.getId()+"");
        }else{
            final NormalDialog dialog = new NormalDialog(getContext());
            int color = ContextCompat.getColor(getContext(),R.color.primary_text);
            dialog.setCanceledOnTouchOutside(false);
            dialog.isTitleShow(false)
                    .cornerRadius(5)
                    .content("是不是要取消关注Ta?")
                    .contentGravity(Gravity.CENTER)
                    .contentTextColor(color)
                    .dividerColor(R.color.divider)
                    .btnTextSize(15.5f, 15.5f)
                    .btnTextColor(color,color)
                    .widthScale(0.75f)
                    .show();
            dialog.setOnBtnClickL(dialog::dismiss, () -> {
                dialog.dismiss();
                showLoading(R.string.label_being_something);
                getCallbacks().cancelFollow(user.getId()+"");
            });

        }
    }

    @Override
    public void followSuccess() {
        cancelLoading();
        ToastUtil.showText("关注成功");
        User user = mAdapter.getItems().get(0);
        user.setIs_follow(1);
        mAdapter.setItem(0,user);
    }

    @Override
    public void cancelFollowSuccess() {
        cancelLoading();
        ToastUtil.showText("已取消关注");
        User user = mAdapter.getItems().get(0);
        user.setIs_follow(0);
        mAdapter.setItem(0,user);
    }
}
