package com.bing.blocks5.ui.search.adapter.holder;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bing.blocks5.AppCookie;
import com.bing.blocks5.R;
import com.bing.blocks5.base.BaseViewHolder;
import com.bing.blocks5.model.User;
import com.bing.blocks5.util.ImageLoadUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：zhangguobing on 2017/7/2 14:11
 * email：bing901222@qq.com
 */

public class UserViewHolder extends BaseViewHolder<User> {

    @Bind(R.id.user_nickname_and_sex)
    TextView mUserNameSexTv;
    @Bind(R.id.ib_focus)
    ImageButton mFocusImageBtn;
    @Bind(R.id.user_avatar)
    ImageView mAvatarImage;

    private IUserOperateListener mListener;

    public UserViewHolder(View view,IUserOperateListener listener) {
        super(view);
        ButterKnife.bind(this,view);
        mListener = listener;
    }

    public void bind(User user){
        ImageLoadUtil.loadAvatar(mAvatarImage,user.getAvatar(),getContext());
        mUserNameSexTv.setText(user.getNick_name());
        int sexDrawableId = "男".equals(user.getSex()) ? R.mipmap.ic_male : R.mipmap.ic_female;
        Drawable sexDrawable = getDrawable(sexDrawableId);
        sexDrawable.setBounds(0,0,sexDrawable.getIntrinsicWidth(),sexDrawable.getIntrinsicHeight());
        mUserNameSexTv.setCompoundDrawables(null,null,sexDrawable,null);
        mFocusImageBtn.setVisibility(user.getIs_follow() == 0 && user.getId() != AppCookie.getUserInfo().getId() ? View.VISIBLE : View.GONE);
        mFocusImageBtn.setOnClickListener(v -> {
             if(mListener != null){
                 mListener.onFollowClick(user);
             }
        });
    }

    public interface IUserOperateListener{
        void onFollowClick(User user);
    }
}
