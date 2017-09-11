package com.playmala.playmala.ui.main.viewholder;

import android.view.View;
import android.widget.ImageView;

import com.playmala.playmala.R;
import com.playmala.playmala.base.BaseViewHolder;
import com.playmala.playmala.model.Config;
import com.playmala.playmala.util.ImageLoadUtil;

import butterknife.Bind;

/**
 * Created by tian on 2017/9/6.
 */

public class CategoryViewHolder extends BaseViewHolder<Config.ActivityTypesBean> {
    @Bind(R.id.iv_activity_category)
    ImageView mActivityCategoryImg;

    public CategoryViewHolder(View view) {
        super(view);
    }

    @Override
    public void bind(Config.ActivityTypesBean activityTypesBean) {
        ImageLoadUtil.loadImage(mActivityCategoryImg,activityTypesBean.getImage_url(),getContext());
    }
}
