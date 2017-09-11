package com.playmala.playmala.ui.search.adapter.holder;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.playmala.playmala.AppCookie;
import com.playmala.playmala.R;
import com.playmala.playmala.base.BaseViewHolder;
import com.playmala.playmala.model.User;
import com.playmala.playmala.util.ImageLoadUtil;

import butterknife.Bind;

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
        mListener = listener;
    }

    public void bind(User user){
        ImageLoadUtil.loadAvatar(mAvatarImage,user.getAvatar(),getContext());
        mUserNameSexTv.setText(user.getNick_name());
        int sexDrawableId = "男".equals(user.getSex()) ? R.mipmap.ic_male : R.mipmap.ic_female;
        Drawable sexDrawable = getDrawable(sexDrawableId);
        sexDrawable.setBounds(0,0,sexDrawable.getIntrinsicWidth(),sexDrawable.getIntrinsicHeight());
        mUserNameSexTv.setCompoundDrawables(null,null,sexDrawable,null);
        if(user.getId() == AppCookie.getUserInfo().getId()){
            mFocusImageBtn.setVisibility(View.GONE);
        }else{
            mFocusImageBtn.setVisibility(View.VISIBLE);
            mFocusImageBtn.setImageResource(user.getIs_follow() == 0 ? R.mipmap.ic_focus : R.mipmap.ic_has_followed);
        }
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
