package com.playmala.playmala.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;

import com.playmala.playmala.R;
import com.playmala.playmala.base.BaseAdapter;
import com.playmala.playmala.base.BaseListActivity;
import com.playmala.playmala.base.BaseController;
import com.playmala.playmala.controller.ActivityUserController;
import com.playmala.playmala.model.User;
import com.playmala.playmala.ui.search.adapter.UserListAdapter;
import com.playmala.playmala.ui.search.adapter.holder.UserViewHolder;
import com.playmala.playmala.util.ToastUtil;
import com.flyco.dialog.widget.NormalDialog;

import java.util.List;

/**
 * author：zhangguobing on 2017/7/12 09:25
 * email：bing901222@qq.com
 * 关注界面
 */

public class FollowOrFansActivity extends BaseListActivity<User,UserViewHolder,ActivityUserController.ActivityUserUiCallbacks>
     implements ActivityUserController.followOrFanUi, UserViewHolder.IUserOperateListener{

    private static String EXTRA_FOLLOW_TYPE = "follow_type";
    private static String EXTRA_USER_ID = "user_id";

    //我关注的
    public static final int TYPE_FOLLOW = 0;
    //关注我的
    public static final int TYPE_FOLLOWED = 1;

    private int follow_type;
    private int user_id;

    private int mOperatePosition = -1;

    @Override
    protected String getEmptyTitle() {
        return follow_type == TYPE_FOLLOW ? "暂无关注" : "暂无粉丝";
    }

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
        return new UserListAdapter(this);
    }

    @Override
    public void onFollowerList(List<User> users) {
        onFinishRequest(users);
    }

    @Override
    public void followSuccess() {
        cancelLoading();
        ToastUtil.showText("关注成功");
        if(mOperatePosition != -1){
            User user = mAdapter.getItems().get(mOperatePosition);
            user.setIs_follow(1);
            mAdapter.setItem(mOperatePosition,user);
        }
    }

    @Override
    public void cancelFollowSuccess() {
        cancelLoading();
        ToastUtil.showText("已取消关注");
        if(mOperatePosition != -1){
            User user = mAdapter.getItems().get(mOperatePosition);
            user.setIs_follow(0);
            mAdapter.setItem(mOperatePosition,user);
        }
    }

    @Override
    public void onFollowClick(User user) {
        mOperatePosition = mAdapter.getItems().indexOf(user);
        if (user.getIs_follow() == 0) {
            showLoading(R.string.label_being_something);
            getCallbacks().follow(user.getId() + "");
        } else {
            final NormalDialog dialog = new NormalDialog(this);
            int color = ContextCompat.getColor(this, R.color.primary_text);
            dialog.setCanceledOnTouchOutside(false);
            dialog.isTitleShow(false)
                    .cornerRadius(5)
                    .content("是不是要取消关注Ta?")
                    .contentGravity(Gravity.CENTER)
                    .contentTextColor(color)
                    .dividerColor(R.color.divider)
                    .btnTextSize(15.5f, 15.5f)
                    .btnTextColor(color, color)
                    .widthScale(0.75f)
                    .show();
            dialog.setOnBtnClickL(dialog::dismiss, () -> {
                dialog.dismiss();
                showLoading(R.string.label_being_something);
                getCallbacks().cancelFollow(user.getId() + "");
            });
        }
    }
}
