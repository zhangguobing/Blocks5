package com.bing.blocks5.ui.activity.adapter.holder;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bing.blocks5.R;
import com.bing.blocks5.base.BaseViewHolder;
import com.bing.blocks5.model.ActivityUser;
import com.bing.blocks5.util.ImageLoadUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：zhangguobing on 2017/7/2 14:11
 * email：bing901222@qq.com
 */

public class ActivityUserViewHolder extends BaseViewHolder<ActivityUser> {

    @Bind(R.id.user_nickname_and_sex)
    TextView mUserNameSexTv;
    @Bind(R.id.user_avatar)
    ImageView mAvatarImage;

    public ActivityUserViewHolder(View view) {
        super(view);
    }

    public void bind(ActivityUser activityUser){
        ImageLoadUtil.loadAvatar(mAvatarImage,activityUser.getCreator().getAvatar(),getContext());
        mUserNameSexTv.setText(activityUser.getCreator().getNick_name());
        int sexDrawableId = "男".equals(activityUser.getCreator().getSex()) ? R.mipmap.ic_male : R.mipmap.ic_female;
        Drawable sexDrawable = getDrawable(sexDrawableId);
        sexDrawable.setBounds(0,0,sexDrawable.getIntrinsicWidth(),sexDrawable.getIntrinsicHeight());
        mUserNameSexTv.setCompoundDrawables(null,null,sexDrawable,null);
    }
}
