package com.bing.blocks5.ui.home.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bing.blocks5.AppCookie;
import com.bing.blocks5.R;
import com.bing.blocks5.model.LoginBean;
import com.bing.blocks5.ui.activity.CreatedActivity;
import com.bing.blocks5.ui.activity.JoinActivity;
import com.bing.blocks5.ui.common.GalleryActivity;
import com.bing.blocks5.ui.home.HomeActivity;
import com.bing.blocks5.ui.setting.SettingActivity;
import com.bing.blocks5.ui.user.FavouriteActivity;
import com.bing.blocks5.ui.user.HistoryActivity;
import com.bing.blocks5.ui.user.ProfileActivity;
import com.bing.blocks5.util.ImageLoadUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DrawerMenuFragment extends Fragment {

    @Bind(R.id.iv_avatar)
    ImageView mAvatarImg;
    @Bind(R.id.tv_user_name_and_sex)
    TextView mUserNameSexTv;
    @Bind(R.id.tv_user_id)
    TextView mUserIdTv;
    @Bind(R.id.tv_focus_num)
    TextView mFocusNumTv;
    @Bind(R.id.tv_follower_num)
    TextView mFollowNumTv;
    @Bind(R.id.tv_credit_score)
    TextView mCreditTv;

    private String mAvatarUrl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        initView();
    }

    private void initView(){
        LoginBean.User user = AppCookie.getUserInfo();
        ImageLoadUtil.loadAvatar(mAvatarImg,user.getAvatar(),getContext());
        mUserNameSexTv.setText(user.getNickName());
        int sexDrawableId = "男".equals(user.getSex()) ? R.mipmap.ic_male : R.mipmap.ic_female;
        Drawable sexDrawable = ContextCompat.getDrawable(getContext(),sexDrawableId);
        sexDrawable.setBounds(0,0,sexDrawable.getIntrinsicWidth(),sexDrawable.getIntrinsicHeight());
        mUserNameSexTv.setCompoundDrawables(null,null,sexDrawable,null);
        mUserIdTv.setText("用户ID：" + user.getId());
        mFocusNumTv.setText(user.getFollowNum()+"");
        mFollowNumTv.setText(user.getFollowedNum()+"");
        mCreditTv.setText(user.getCredit()+"");

        mAvatarUrl = user.getAvatar();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.ll_profile,R.id.ll_setting,R.id.iv_avatar,
         R.id.ll_my_created_activity,R.id.ll_my_join_activity,
            R.id.ll_collect,R.id.ll_history})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_avatar:
                showGallery(view);
                break;
            case R.id.ll_profile:
                ProfileActivity.create(getActivity());
                hideSlideMenu();
                break;
            case R.id.ll_setting:
                SettingActivity.create(getActivity());
                hideSlideMenu();
                break;
            case R.id.ll_my_created_activity:
                CreatedActivity.create(getContext(),AppCookie.getUserInfo().getId(),"我创建的活动");
                hideSlideMenu();
                break;
            case R.id.ll_my_join_activity:
                JoinActivity.create(getContext(),AppCookie.getUserInfo().getId(),"我参加的活动");
                hideSlideMenu();
                break;
            case R.id.ll_collect:
                FavouriteActivity.create(getContext());
                hideSlideMenu();
                break;
            case R.id.ll_history:
                HistoryActivity.create(getContext());
                hideSlideMenu();
                break;
        }
    }

    private void hideSlideMenu(){
        Activity activity = getActivity();
        if(activity instanceof HomeActivity){
            HomeActivity homeActivity = (HomeActivity) activity;
            homeActivity.getSlidingMenu().showContent(false);
        }
    }

    private void showGallery(View v) {
        if(TextUtils.isEmpty(mAvatarUrl)) return;
        int[] location = new int[2];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Rect frame = new Rect();
            getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;
            v.getLocationOnScreen(location);
            location[1] += statusBarHeight;
        } else {
            v.getLocationOnScreen(location);
        }
        v.invalidate();
        int width = v.getWidth();
        int height = v.getHeight();

        Intent intent = new Intent(getActivity(), GalleryActivity.class);
        Bundle b = new Bundle();
        b.putStringArray(GalleryActivity.PHOTO_SOURCE_ID, new String[]{mAvatarUrl});
        intent.putExtras(b);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_POSITION, 0);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_X_TAG, location[0]);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_Y_TAG, location[1]);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_W_TAG, width);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_H_TAG, height);
        startActivity(intent);
        getActivity().overridePendingTransition(0, 0);
    }
}
