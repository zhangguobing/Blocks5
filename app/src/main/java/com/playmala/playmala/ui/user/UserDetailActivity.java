package com.playmala.playmala.ui.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.playmala.playmala.R;
import com.playmala.playmala.base.BaseController;
import com.playmala.playmala.base.BasePresenterActivity;
import com.playmala.playmala.base.ContentView;
import com.playmala.playmala.model.User;
import com.playmala.playmala.controller.UserController;
import com.playmala.playmala.ui.activity.CreatedListActivity;
import com.playmala.playmala.ui.activity.JoinListActivity;
import com.playmala.playmala.ui.common.GalleryActivity;
import com.playmala.playmala.util.ImageLoadUtil;
import com.playmala.playmala.util.ToastUtil;
import com.flyco.dialog.widget.NormalDialog;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * author：zhangguobing on 2017/7/2 20:45
 * email：bing901222@qq.com
 */
@ContentView(R.layout.activity_user_detail)
public class UserDetailActivity extends BasePresenterActivity<UserController.UserUiCallbacks>
implements UserController.UserDetailUi{

    @Bind(R.id.iv_user_avatar)
    ImageView userAvatarImg;
    @Bind(R.id.tv_focus_num)
    TextView mFocusNumTv;
    @Bind(R.id.tv_follower_num)
    TextView mFollowerNumTv;
    @Bind(R.id.tv_credit_score)
    TextView mCreditScoreTv;
    @Bind(R.id.tv_nickname_and_sex)
    TextView mNickNameSexTv;
    @Bind(R.id.iv_is_follow)
    ImageView mIsFollowImg;
    @Bind(R.id.tv_content)
    TextView mContentTv;
    @Bind(R.id.image_1)
    ImageView image_1;
    @Bind(R.id.image_2)
    ImageView image_2;
    @Bind(R.id.image_3)
    ImageView image_3;
    @Bind(R.id.tv_user_id)
    TextView mUserIdTv;
    @Bind(R.id.tv_job_and_area)
    TextView mJobAndAreaTv;
    @Bind(R.id.iv_is_identity)
    ImageView mIsIdentityImg;
    @Bind(R.id.tv_item_1)
    TextView mItemTv1;
    @Bind(R.id.tv_item_2)
    TextView mItemTv2;

    public static final String EXTRA_USER = "extra_user";
    public static final String EXTRA_USER_ID = "extra_user_id";

    private ArrayList<String> gallery_urls;
    private String mAvatarUrl;

    private int user_id;
    private boolean is_follow = false;

    public static void create(Context context,User user){
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.putExtra(EXTRA_USER,user);
        context.startActivity(intent);
    }

    public static void create(Context context,int user_id){
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.putExtra(EXTRA_USER_ID,user_id);
        context.startActivity(intent);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        final User user = getIntent().getParcelableExtra(EXTRA_USER);
        if(user != null){
            setUser(user);
        }else{
            int user_id = getIntent().getIntExtra(EXTRA_USER_ID,0);
            showLoading(R.string.label_being_loading);
            getCallbacks().getUserById(user_id);
        }
    }

    private void setUser(User user){
        user_id = user.getId();
        mUserIdTv.setText("用户ID:" + user.getId());
        String jobAndAddrStr = user.getJob() + "\n" + user.getAddr();
        if(!TextUtils.isEmpty(user.getJob())){
            jobAndAddrStr = "职业：" + jobAndAddrStr;
        }
        mJobAndAreaTv.setText(jobAndAddrStr);
        ImageLoadUtil.loadAvatar(userAvatarImg,user.getAvatar(),this);
        mAvatarUrl = user.getAvatar();
        mFocusNumTv.setText(user.getFollow_num()+"");
        mFollowerNumTv.setText(user.getFollowed_num()+"");
        mCreditScoreTv.setText(user.getCredit()+"");
        mNickNameSexTv.setText(user.getNick_name());
        mIsIdentityImg.setImageResource(user.getIdentity_state() == 0 ? R.mipmap.ic_no_has_identity : R.mipmap.ic_has_identity);
        int sexDrawableId = "男".equals(user.getSex()) ? R.mipmap.ic_male : R.mipmap.ic_female;
        Drawable sexDrawable = ContextCompat.getDrawable(this,sexDrawableId);
        sexDrawable.setBounds(0,0,sexDrawable.getIntrinsicWidth(),sexDrawable.getIntrinsicHeight());
        mNickNameSexTv.setCompoundDrawables(null,null,sexDrawable,null);
        is_follow = user.getIs_follow() == 1;
        mIsFollowImg.setImageResource(is_follow ? R.mipmap.ic_has_followed: R.mipmap.ic_focus);
        mContentTv.setText(TextUtils.isEmpty(user.getContent()) ? "未填写个性签名": "签名：" + user.getContent());
        if(!TextUtils.isEmpty(user.getImg_url_1())){
            image_1.setVisibility(View.VISIBLE);
            ImageLoadUtil.loadImage(image_1,user.getImg_url_1(),this);
        }
        if(!TextUtils.isEmpty(user.getImg_url_2())){
            image_2.setVisibility(View.VISIBLE);
            ImageLoadUtil.loadImage(image_2,user.getImg_url_2(),this);
        }
        if(!TextUtils.isEmpty(user.getImg_url_3())){
            image_3.setVisibility(View.VISIBLE);
            ImageLoadUtil.loadImage(image_3,user.getImg_url_3(),this);
        }

        String he =  "男".equals(user.getSex()) ? "他" : "她";
        mItemTv1.setText(he + "举办的活动");
        mItemTv2.setText(he + "参加的活动");

        initGalleryUrls(user);
    }

    private void initGalleryUrls(User user){
        gallery_urls = new ArrayList<>();
        if(!TextUtils.isEmpty(user.getImg_url_1())){
            gallery_urls.add(user.getImg_url_1());
        }
        if(!TextUtils.isEmpty(user.getImg_url_2())){
            gallery_urls.add(user.getImg_url_2());
        }
        if(!TextUtils.isEmpty(user.getImg_url_3())){
            gallery_urls.add(user.getImg_url_3());
        }
    }

    @OnClick({R.id.iv_back,R.id.layout_his_create_activity,R.id.layout_his_join_activity,
    R.id.image_1,R.id.image_2,R.id.image_3,R.id.ll_follow,R.id.ll_followed,R.id.iv_is_follow,R.id.iv_user_avatar})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.layout_his_create_activity:
                CreatedListActivity.create(this,user_id,mItemTv1.getText().toString());
                break;
            case R.id.layout_his_join_activity:
                JoinListActivity.create(this,user_id,mItemTv2.getText().toString());
                break;
            case R.id.image_1:
                showGallery(view,0);
                break;
            case R.id.image_2:
                showGallery(view,1);
                break;
            case R.id.image_3:
                showGallery(view,2);
                break;
            case R.id.ll_follow:
                FollowOrFansActivity.create(this,FollowOrFansActivity.TYPE_FOLLOW, user_id);
                break;
            case R.id.ll_followed:
                FollowOrFansActivity.create(this,FollowOrFansActivity.TYPE_FOLLOWED, user_id);
                break;
            case R.id.iv_is_follow:
                handleFollowClick();
                break;
            case R.id.iv_user_avatar:
                showAvatar(view);
                break;
        }
    }

    private void handleFollowClick(){
        if(is_follow){
            final NormalDialog dialog = new NormalDialog(this);
            int color = ContextCompat.getColor(this,R.color.primary_text);
            dialog.setCanceledOnTouchOutside(false);
            dialog.isTitleShow(false)
                    .cornerRadius(5)
                    .content("是不是要取消关注Ta?")
                    .contentGravity(Gravity.CENTER)
                    .contentTextColor(color)
                    .dividerColor(R.color.divider)
                    .btnTextSize(15.5f, 15.5f)
                    .btnTextColor(color,color)
                    .widthScale(0.75f)
                    .show();
            dialog.setOnBtnClickL(dialog::dismiss, () -> {
                dialog.dismiss();
                showLoading(R.string.label_being_something);
                getCallbacks().cancelFollow(user_id+"");
            });
        }else{
            showLoading(R.string.label_being_something);
            getCallbacks().follow(user_id +"");
        }
    }

    @Override
    protected BaseController getPresenter() {
        return new UserController();
    }

    @Override
    public void followSuccess() {
        is_follow = true;
        cancelLoading();
        ToastUtil.showText("已关注");
        mIsFollowImg.setImageResource(R.mipmap.ic_has_followed);
    }

    @Override
    public void cancelFollowSuccess() {
        is_follow = false;
        cancelLoading();
        ToastUtil.showText("已取消关注");
        mIsFollowImg.setImageResource(R.mipmap.ic_focus);
    }

    @Override
    public void loadUserCallback(User user) {
        cancelLoading();
        setUser(user);
    }


    private void showGallery(View v,int position) {
        int[] location = new int[2];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Rect frame = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;
            v.getLocationOnScreen(location);
            location[1] += statusBarHeight;
        } else {
            v.getLocationOnScreen(location);
        }
        v.invalidate();
        int width = v.getWidth();
        int height = v.getHeight();

        Intent intent = new Intent(this, GalleryActivity.class);
        Bundle b = new Bundle();
        b.putStringArrayList(GalleryActivity.PHOTO_SOURCE_ID, gallery_urls);
        intent.putExtras(b);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_POSITION, position);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_X_TAG, location[0]);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_Y_TAG, location[1]);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_W_TAG, width);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_H_TAG, height);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }


    private void showAvatar(View v) {
        if(TextUtils.isEmpty(mAvatarUrl)) return;
        int[] location = new int[2];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Rect frame = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;
            v.getLocationOnScreen(location);
            location[1] += statusBarHeight;
        } else {
            v.getLocationOnScreen(location);
        }
        v.invalidate();
        int width = v.getWidth();
        int height = v.getHeight();

        Intent intent = new Intent(this, GalleryActivity.class);
        Bundle b = new Bundle();
        ArrayList<String> list = new ArrayList<>();
        list.add(mAvatarUrl);
        b.putStringArrayList(GalleryActivity.PHOTO_SOURCE_ID, list);
        intent.putExtras(b);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_X_TAG, location[0]);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_Y_TAG, location[1]);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_W_TAG, width);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_H_TAG, height);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
