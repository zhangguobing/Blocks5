package com.bing.blocks5.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bing.blocks5.base.BaseAdapter;
import com.bing.blocks5.base.BaseListActivity;
import com.bing.blocks5.base.BaseController;
import com.bing.blocks5.controller.ActivityUserController;
import com.bing.blocks5.model.User;
import com.bing.blocks5.ui.search.adapter.UserListAdapter;
import com.bing.blocks5.ui.search.adapter.holder.UserViewHolder;

import java.util.List;

/**
 * author：zhangguobing on 2017/7/12 09:25
 * email：bing901222@qq.com
 * 关注界面
 */

public class FollowOrFansActivity extends BaseListActivity<User,UserViewHolder,ActivityUserController.ActivityUserUiCallbacks>
     implements ActivityUserController.followOrFanUi{

    private static String EXTRA_FOLLOW_TYPE = "follow_type";
    private static String EXTRA_USER_ID = "user_id";

    //我关注的
    public static final int TYPE_FOLLOW = 0;
    //关注我的
    public static final int TYPE_FOLLOWED = 1;

    private int follow_type;
    private int user_id;


    public static void create(Context context, int follow_type, int user_id){
        Intent intent = new Intent(context, FollowOrFansActivity.class);
        intent.putExtra(EXTRA_FOLLOW_TYPE, follow_type);
        intent.putExtra(EXTRA_USER_ID, user_id);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        follow_type = getIntent().getIntExtra(EXTRA_FOLLOW_TYPE, TYPE_FOLLOW);
        user_id = getIntent().getIntExtra(EXTRA_USER_ID,0);
        setTitle(follow_type == TYPE_FOLLOW ? "关注" : "粉丝");
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getCallbacks().getFollowers(follow_type,mPage,mPageSize, user_id);
    }

    @Override
    protected void refreshPage() {
        getCallbacks().getFollowers(follow_type,mPage = 1,mPageSize, user_id);
    }

    @Override
    protected void nextPage() {
        getCallbacks().getFollowers(follow_type,mPage,mPageSize, user_id);
    }

    @Override
    protected BaseController getPresenter() {
        return new ActivityUserController();
    }

    @Override
    protected void onItemClick(int position, User item) {
        UserDetailActivity.create(this, item.getId());
    }

    @Override
    protected BaseAdapter<User, UserViewHolder> getAdapter() {
        return new UserListAdapter();
    }

    @Override
    public void onFollowerList(List<User> users) {
        onFinishRequest(users);
    }
}
