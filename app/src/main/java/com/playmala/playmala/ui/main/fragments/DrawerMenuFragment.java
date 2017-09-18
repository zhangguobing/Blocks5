package com.playmala.playmala.ui.main.fragments;

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

import com.playmala.playmala.AppConfig;
import com.playmala.playmala.AppCookie;
import com.playmala.playmala.R;
import com.playmala.playmala.base.WebActivity;
import com.playmala.playmala.model.User;
import com.playmala.playmala.model.event.UserInfoChangeEvent;
import com.playmala.playmala.model.event.UserInfoRefreshEvent;
import com.playmala.playmala.ui.activity.CreatedActivity;
import com.playmala.playmala.ui.activity.JoinActivity;
import com.playmala.playmala.ui.common.GalleryActivity;
import com.playmala.playmala.ui.main.HomeActivity;
import com.playmala.playmala.ui.setting.SettingActivity;
import com.playmala.playmala.ui.user.FavouriteActivity;
import com.playmala.playmala.ui.user.FollowOrFansActivity;
import com.playmala.playmala.ui.user.HistoryActivity;
import com.playmala.playmala.ui.user.ProfileActivity;
import com.playmala.playmala.util.AndroidBug54971Workaround;
import com.playmala.playmala.util.EventUtil;
import com.playmala.playmala.util.ImageLoadUtil;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

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
        AndroidBug54971Workaround.assistActivity(view);
        ButterKnife.bind(this,view);
        initView();
    }

    private void initView(){
        setUser(AppCookie.getUserInfo());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.ll_profile,R.id.ll_setting,R.id.iv_avatar,
         R.id.ll_my_created_activity,R.id.ll_my_join_activity,
            R.id.ll_collect,R.id.ll_history,R.id.ll_follow,
         R.id.ll_followed,R.id.user_info_layout,R.id.ll_credit})
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
            case R.id.ll_follow:
                FollowOrFansActivity.create(getContext(),FollowOrFansActivity.TYPE_FOLLOW,AppCookie.getUserInfo().getId());
                hideSlideMenu();
                break;
            case R.id.ll_followed:
                FollowOrFansActivity.create(getContext(),FollowOrFansActivity.TYPE_FOLLOWED,AppCookie.getUserInfo().getId());
                hideSlideMenu();
                break;
            case R.id.user_info_layout:
                ProfileActivity.create(getActivity());
                hideSlideMenu();
                break;
            case R.id.ll_credit:
                WebActivity.create(getContext(), AppConfig.CREDIT_URL + AppCookie.getUserInfo().getCredit());
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
        ArrayList<String> urls = new ArrayList<>();
        urls.add(mAvatarUrl);
        b.putStringArrayList(GalleryActivity.PHOTO_SOURCE_ID, urls);
        intent.putExtras(b);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_POSITION, 0);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_X_TAG, location[0]);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_Y_TAG, location[1]);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_W_TAG, width);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_H_TAG, height);
        startActivity(intent);
        getActivity().overridePendingTransition(0, 0);
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
    public void onUserInfoChange(UserInfoChangeEvent event){
        setUser(event.user);
        AppCookie.saveUserInfo(event.user);
    }

    @Subscribe
    public void onUserRefreshChange(UserInfoRefreshEvent event){
        setUser(event.user);
        AppCookie.saveUserInfo(event.user);
    }

    public void setUser(User user){
        if(user == null) return;
        ImageLoadUtil.loadAvatar(mAvatarImg,user.getAvatar(),getContext());
        mUserNameSexTv.setText(user.getNick_name());
        int sexDrawableId = "男".equals(user.getSex()) ? R.mipmap.ic_male : R.mipmap.ic_female;
        Drawable sexDrawable = ContextCompat.getDrawable(getContext(),sexDrawableId);
        sexDrawable.setBounds(0,0,sexDrawable.getIntrinsicWidth(),sexDrawable.getIntrinsicHeight());
        mUserNameSexTv.setCompoundDrawables(null,null,sexDrawable,null);
        mUserIdTv.setText("用户ID：" + user.getId());
        mFocusNumTv.setText(user.getFollow_num()+"");
        mFollowNumTv.setText(user.getFollowed_num()+"");
        mCreditTv.setText(user.getCredit()+"");

        mAvatarUrl = user.getAvatar();
    }
}
