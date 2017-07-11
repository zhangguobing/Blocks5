package com.bing.blocks5.ui.search.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bing.blocks5.R;
import com.bing.blocks5.base.BaseAdapter;
import com.bing.blocks5.model.User;
import com.bing.blocks5.ui.search.adapter.holder.UserViewHolder;
import com.bing.blocks5.util.Objects;

/**
 * author：zhangguobing on 2017/7/2 15:31
 * email：bing901222@qq.com
 */

public class UserListAdapter extends BaseAdapter<User,UserViewHolder> {

    private UserViewHolder.IUserOperateListener mListener;

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

    @Override
    protected boolean areItemsTheSame(User oldItem, User newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    protected boolean areContentsTheSame(User oldItem, User newItem) {
        return Objects.equals(oldItem,newItem);
    }


}
