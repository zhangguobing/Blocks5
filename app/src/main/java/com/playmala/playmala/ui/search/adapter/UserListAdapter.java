package com.playmala.playmala.ui.search.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.playmala.playmala.R;
import com.playmala.playmala.base.BaseAdapter;
import com.playmala.playmala.model.User;
import com.playmala.playmala.ui.search.adapter.holder.UserViewHolder;
import com.playmala.playmala.util.Objects;

/**
 * author：zhangguobing on 2017/7/2 15:31
 * email：bing901222@qq.com
 */

public class UserListAdapter extends BaseAdapter<User,UserViewHolder> {

    private UserViewHolder.IUserOperateListener mListener;

    public UserListAdapter() {
    }

    public UserListAdapter(UserViewHolder.IUserOperateListener listener) {
        this.mListener = listener;
    }

    @Override
    public int getViewLayoutId(int viewType) {
        return R.layout.item_user;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(View view, int viewType) {
        return new UserViewHolder(view,mListener);
    }

    @Override
    public void bindViewHolder(UserViewHolder holder, User item, int position) {
        holder.bind(item);
    }

}
