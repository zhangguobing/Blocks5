package com.playmala.playmala.ui.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.playmala.playmala.ui.common.GalleryActivity;
import com.playmala.playmala.util.AsyncRun;
import com.playmala.playmala.util.QiniuUploadUtils;
import com.google.gson.Gson;
import com.playmala.playmala.AppCookie;
import com.playmala.playmala.R;
import com.playmala.playmala.album.Album;
import com.playmala.playmala.base.BaseController;
import com.playmala.playmala.base.BasePresenterActivity;
import com.playmala.playmala.base.ContentView;
import com.playmala.playmala.model.JsonBean;
import com.playmala.playmala.model.User;
import com.playmala.playmala.controller.UserController;
import com.playmala.playmala.util.GetJsonDataUtil;
import com.playmala.playmala.util.ImageLoadUtil;
import com.playmala.playmala.util.ToastUtil;
import com.playmala.playmala.widget.TitleBar;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.lcodecore.tkrefreshlayout.utils.DensityUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * author：zhangguobing on 2017/6/29 15:21
 * email：bing901222@qq.com
 */
@ContentView(R.layout.activity_profile)
public class ProfileActivity extends BasePresenterActivity<UserController.UserUiCallbacks>
  implements UserController.ProfileUi,View.OnClickListener{

    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    private static final int ACTIVITY_REQUEST_SELECT_AVATAR = 100;
    private static final int ACTIVITY_REQUEST_SELECT_ALBUM = 101;
    private static final int ACTIVITY_REQUEST_EDIT_ALBUM = 102;


    private static boolean isAnimationEnter = false;

    private String mAvatarUrl = "";

    @Bind(R.id.tv_select_area)
    TextView mSelectAreaTv;
    @Bind(R.id.tv_nick_name)
    EditText mNickNameEt;
    @Bind(R.id.tv_sex)
    TextView mSexTv;
    @Bind(R.id.tv_age)
    TextView mAgeTv;
    @Bind(R.id.et_job)
    EditText mJobEt;
    @Bind(R.id.et_content)
    EditText mContentEt;
    @Bind(R.id.iv_avatar)
    ImageView mAvatarImg;
    @Bind(R.id.ablum_container)
    LinearLayout mAblumContainer;
    @Bind(R.id.iv_album)
    ImageView mAlbumImg;

    private KProgressHUD mUploadProgressDialog;

    private static final int ALBUM_MAX_COUNT = 3;
    private ArrayList<String> mAlbumUrls = new ArrayList<>();

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        TitleBar titleBar = getTitleBar();
        if(titleBar != null){
            titleBar.setLeftTextColor(ContextCompat.getColor(this,R.color.white));
            titleBar.setLeftText("取消");
        }
        initJsonData();

        mUploadProgressDialog = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.PIE_DETERMINATE)
                .setLabel(getString(R.string.lable_being_upload)).setMaxProgress(100);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        loadData();
    }

    private void loadData(){
        if(AppCookie.getUserInfo() == null){
            ToastUtil.showText("用户信息丢失，请重新登录");
            return;
        }
        showLoading(R.string.label_being_loading);
        getCallbacks().getUserById(AppCookie.getUserInfo().getId());
    }

    public static void create(Activity activity){
        Intent intent = new Intent(activity, ProfileActivity.class);
        activity.startActivity(intent);
        isAnimationEnter = false;
    }

    public static void createWithAnimation(Activity activity){
        create(activity);
        activity.overridePendingTransition(R.anim.slide_in_top, R.anim.activity_stay);
        isAnimationEnter = true;
    }

    @Override
    public void finish() {
        super.finish();
        if(isAnimationEnter){
            overridePendingTransition(R.anim.activity_stay,R.anim.slide_out_bottom);
        }
    }

    @OnClick({R.id.iv_avatar,R.id.iv_album,R.id.tv_select_area,R.id.btn_save,R.id.tv_age})
    public void onClick(View view){
        hideKeyBoard(view);
        switch (view.getId()){
            case R.id.iv_avatar:
                fromAlbum(1, ACTIVITY_REQUEST_SELECT_AVATAR);
                break;
            case R.id.iv_album:
                fromAlbum(ALBUM_MAX_COUNT - mAblumContainer.getChildCount(), ACTIVITY_REQUEST_SELECT_ALBUM);
                break;
            case R.id.tv_select_area:
                showPickerView();
                break;
            case R.id.btn_save:
                save();
                break;
            case R.id.tv_age:
                showAgePickerView();
                break;
            case R.id.profile_album_id:
                if(view.getTag() != null && mAlbumUrls.size() > 0){
                    String url = (String) view.getTag();
                    int index = mAlbumUrls.indexOf(url);
                    if(index != -1) showGallery(view,index);
                }
                break;
        }
    }

    private void hideKeyBoard(View v){
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    private void showAgePickerView(){
        List<String> ageOptions = new ArrayList<>();
        for (int i = 18; i <= 100; i++) {
            ageOptions.add(i+"");
        }
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, (options1, options2, options3, v) -> {
            mAgeTv.setText(ageOptions.get(options1));
        })
            .setTextColorCenter(ContextCompat.getColor(this,R.color.primary_text))
            .setTitleText("请选择年龄")
            .setTitleColor(ContextCompat.getColor(this,R.color.red))
            .setContentTextSize(18)
            .build();
        pvOptions.setPicker(ageOptions);
        pvOptions.show();
    }

    private void save() {
        showLoading(R.string.label_being_something);
        int age = Integer.parseInt(mAgeTv.getText().toString());
        String job = mJobEt.getText().toString().trim();
        String address = mSelectAreaTv.getText().toString();
        String content = mContentEt.getText().toString().trim();
        String nick_name = mNickNameEt.getText().toString().trim();
        String image_url_1 = "";
        String image_url_2 = "";
        String image_url_3 = "";
        for (int i = 0; i < mAblumContainer.getChildCount(); i++) {
            View childView = mAblumContainer.getChildAt(i);
            if(childView.getTag() != null){
                String url = childView.getTag().toString();
                if(i == 0){
                    image_url_1 = url;
                }else if(i == 1){
                    image_url_2 = url;
                }else if(i == 2){
                    image_url_3 = url;
                }
            }
        }
        getCallbacks().updateUser(age,job,address,mAvatarUrl,content,image_url_1,image_url_2,image_url_3,nick_name);
    }

    /**
     * 往容器内添加相应的子View
     */
    private void addChildViewToAlbum(String filePath,String imageUrl,boolean isLoadRemoteUrl){
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.width = DensityUtil.dp2px(this,42);
        layoutParams.leftMargin = DensityUtil.dp2px(this,20);
        imageView.setLayoutParams(layoutParams);
        imageView.setId(R.id.profile_album_id);
        imageView.setOnClickListener(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mAblumContainer.addView(imageView);
        if(!TextUtils.isEmpty(filePath)){
            ImageLoadUtil.loadImage(imageView,filePath,this);
        }
        if(isLoadRemoteUrl){
            ImageLoadUtil.loadImage(imageView,imageUrl,this);
        }
        imageView.setTag(imageUrl);
        mAlbumImg.setVisibility(mAblumContainer.getChildCount() == ALBUM_MAX_COUNT ? View.GONE : View.VISIBLE);
    }

    /**
     * Select image from fromAlbum.
     */
    private void fromAlbum(int selectCount, int requestCode) {
        Album.album(this)
                .requestCode(requestCode)
                .toolBarColor(ContextCompat.getColor(this, R.color.red))
                .statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                .navigationBarColor(ActivityCompat.getColor(this, R.color.colorPrimaryDark))
                .selectCount(selectCount)
                .columnCount(3)
                .title("相册")
                .start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case ACTIVITY_REQUEST_SELECT_AVATAR: {
                    if (data != null) {
                        String filePath = Album.parseResult(data).get(0);
                        QiniuUploadUtils.getInstance().uploadImage(filePath, new QiniuUploadUtils.QiniuUploadUtilsListener() {
                            @Override
                            public void onStart() {
                                mUploadProgressDialog.show();
                            }

                            @Override
                            public void onSuccess(String filePath,String fileUrl) {
                                mUploadProgressDialog.dismiss();
                                ImageLoadUtil.loadAvatar(mAvatarImg,filePath,ProfileActivity.this);
                                mAvatarUrl = fileUrl;
                            }

                            @Override
                            public void onError(int errorCode, String msg) {
                                mUploadProgressDialog.dismiss();
                                ToastUtil.showText("上传失败," + msg);
                            }

                            @Override
                            public void onProgress(int progress) {
                                mUploadProgressDialog.setProgress(progress);
                            }
                        });
                    }
                    break;
                }
                case ACTIVITY_REQUEST_SELECT_ALBUM:
                    if(data != null) {
                        List<String> filePaths = Album.parseResult(data);
                        QiniuUploadUtils.getInstance().uploadImages(filePaths, new QiniuUploadUtils.QiniuUploadUtilsListener() {
                            @Override
                            public void onStart() {
                                AsyncRun.runInMain(() -> mUploadProgressDialog.show());
                            }

                            @Override
                            public void onSuccess(String originUrl,String destUrl) {
                                AsyncRun.runInMain(() -> {
                                    mUploadProgressDialog.dismiss();
                                    addChildViewToAlbum(originUrl,destUrl,false);
                                    mAlbumUrls.add(destUrl);
                                });
                            }

                            @Override
                            public void onError(int errorCode, String msg) {
                                AsyncRun.runInMain(() -> {
                                    mUploadProgressDialog.dismiss();
                                    ToastUtil.showText("上传失败," + msg);
                                });

                            }

                            @Override
                            public void onProgress(int progress) {
                                AsyncRun.runInMain(() -> mUploadProgressDialog.setProgress(progress));
                            }
                        });
                        break;
                    }
                case ACTIVITY_REQUEST_EDIT_ALBUM:
                    mAlbumUrls.clear();
                    mAlbumUrls.addAll(GalleryActivity.parseResult(data));
                    mAblumContainer.removeAllViews();
                    for (String imageUrl : mAlbumUrls){
                        addChildViewToAlbum(null,imageUrl,true);
                    }
                    if(mAlbumUrls.size() == 0) mAlbumImg.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    private void showPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, (options1, options2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            String tx = options1Items.get(options1).getPickerViewText()+"^"+
                    options2Items.get(options1).get(options2)+"^"+
                    options3Items.get(options1).get(options2).get(options3);
            mSelectAreaTv.setText(tx);
        })
                .setTextColorCenter(ContextCompat.getColor(this,R.color.primary_text))
                .setContentTextSize(18)
                .build();
        pvOptions.setPicker(options1Items, options2Items,options3Items);
        pvOptions.show();
    }

    private void initJsonData() {
        String JsonData = new GetJsonDataUtil().getJson(this,"province.json");//获取assets目录下的json文件数据
        ArrayList<JsonBean> jsonBean = parseData(JsonData);
        options1Items = jsonBean;
        for (int i=0;i<jsonBean.size();i++){//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
            for (int c=0; c<jsonBean.get(i).getCityList().size(); c++){//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表
                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        ||jsonBean.get(i).getCityList().get(c).getArea().size()==0) {
                    City_AreaList.add("");
                }else {
                    for (int d=0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);
                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }
            //添加城市数据
            options2Items.add(CityList);
            // 添加地区数据
            options3Items.add(Province_AreaList);
        }
    }

    public ArrayList<JsonBean> parseData(String result) {
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    @Override
    protected BaseController getPresenter() {
        return new UserController();
    }

    @Override
    public void loadUserCallback(User user) {
        cancelLoading();
        setUser(user);
    }

    @Override
    public void updateUserCallback(User user) {
        cancelLoading();
        finish();
        ToastUtil.showText("保存成功");
    }

    private void setUser(User user){
        mAvatarUrl = user.getAvatar();
        ImageLoadUtil.loadAvatar(mAvatarImg,user.getAvatar(),this);
        initAlbum(user);
        mNickNameEt.setText(user.getNick_name());
        mSexTv.setText(user.getSex());
        mAgeTv.setText(user.getAge()+"");
        mSelectAreaTv.setText(user.getAddr());
        mJobEt.setText(user.getJob());
        mContentEt.setText(user.getContent());
    }

    private void initAlbum(User user){
        mAlbumUrls.clear();
        if(!TextUtils.isEmpty(user.getImg_url_1())){
            mAlbumUrls.add(user.getImg_url_1());
        }
        if(!TextUtils.isEmpty(user.getImg_url_2())){
            mAlbumUrls.add(user.getImg_url_2());
        }
        if(!TextUtils.isEmpty(user.getImg_url_3())){
            mAlbumUrls.add(user.getImg_url_3());
        }
        if(mAlbumUrls.size() == ALBUM_MAX_COUNT){
            mAlbumImg.setVisibility(View.GONE);
        }
        mAblumContainer.removeAllViews();
        for (String imageUrl : mAlbumUrls){
            addChildViewToAlbum(null,imageUrl,true);
        }
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
        b.putStringArrayList(GalleryActivity.PHOTO_SOURCE_ID, mAlbumUrls);
        intent.putExtras(b);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_POSITION, position);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_X_TAG, location[0]);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_Y_TAG, location[1]);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_W_TAG, width);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_H_TAG, height);
        intent.putExtra(GalleryActivity.PHOTO_IS_SHOW_TITLE_BAR, true);
        startActivityForResult(intent,ACTIVITY_REQUEST_EDIT_ALBUM);
    }
}
