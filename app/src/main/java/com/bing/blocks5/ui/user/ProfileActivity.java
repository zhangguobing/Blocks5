package com.bing.blocks5.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.bing.blocks5.AppCookie;
import com.bing.blocks5.R;
import com.bing.blocks5.album.Album;
import com.bing.blocks5.base.BasePresenter;
import com.bing.blocks5.base.BasePresenterActivity;
import com.bing.blocks5.base.ContentView;
import com.bing.blocks5.model.JsonBean;
import com.bing.blocks5.model.User;
import com.bing.blocks5.presenter.UserPresenter;
import com.bing.blocks5.util.GetJsonDataUtil;
import com.bing.blocks5.util.ImageLoadUtil;
import com.bing.blocks5.util.ToastUtil;
import com.bing.blocks5.widget.TitleBar;

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
public class ProfileActivity extends BasePresenterActivity<UserPresenter.UserUiCallbacks>
  implements UserPresenter.ProfileUi{

    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    private static final int ACTIVITY_REQUEST_SELECT_PHOTO = 100;

    private static boolean isAnimationEnter = false;

    @Bind(R.id.tv_select_area)
    TextView mSelectAreaTv;
    @Bind(R.id.tv_nick_name)
    TextView mNickNameTv;
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
    @Bind(R.id.image_1)
    ImageView mImage1;
    @Bind(R.id.image_2)
    ImageView mImage2;
    @Bind(R.id.image_3)
    ImageView mImage3;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        TitleBar titleBar = getTitleBar();
        if(titleBar != null){
            titleBar.setLeftTextColor(ContextCompat.getColor(this,R.color.white));
            titleBar.setLeftText("取消");
        }
        initJsonData();
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
        switch (view.getId()){
            case R.id.iv_avatar:
            case R.id.iv_album:
                fromAlbum(1);
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
        }
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
        String avatar_url = mAvatarImg.getTag() == null ? "" : mAvatarImg.getTag().toString();
        int age = Integer.parseInt(mAgeTv.getText().toString());
        String job = mJobEt.getText().toString().trim();
        String address = mSelectAreaTv.getText().toString();
        String content = mContentEt.getText().toString().trim();
        String image_url_1 = mImage1.getTag() == null ? "" : mImage1.getTag().toString();
        String image_url_2 = mImage1.getTag() == null ? "" : mImage2.getTag().toString();
        String image_url_3 = mImage1.getTag() == null ? "" : mImage3.getTag().toString();
        getCallbacks().updateUser(age,job,address,avatar_url,content,image_url_1,image_url_2,image_url_3);
    }

    /**
     * Select image from fromAlbum.
     */
    private void fromAlbum(int selectCount) {
        Album.album(this)
                .requestCode(ACTIVITY_REQUEST_SELECT_PHOTO)
                .toolBarColor(ContextCompat.getColor(this, R.color.red))
                .statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                .navigationBarColor(ActivityCompat.getColor(this, R.color.colorPrimaryDark))
                .selectCount(selectCount)
                .columnCount(3)
                .title("相册")
                .start();
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
    protected BasePresenter getPresenter() {
        return new UserPresenter();
    }

    @Override
    public void loadUserCallback(User user) {
        cancelLoading();
        setUser(user);
    }

    @Override
    public void updateUserCallback(User user) {
        cancelLoading();
        setUser(user);
        ToastUtil.showText("保存成功");
    }

    private void setUser(User user){
        ImageLoadUtil.loadAvatar(mAvatarImg,user.getAvatar(),this);
        ImageLoadUtil.loadImage(mImage1,user.getImg_url_1(),this);
        ImageLoadUtil.loadImage(mImage2,user.getImg_url_2(),this);
        ImageLoadUtil.loadImage(mImage3,user.getImg_url_3(),this);
        mNickNameTv.setText(user.getNick_name());
        mSexTv.setText(user.getSex());
        mAgeTv.setText(user.getAge()+"");
        mSelectAreaTv.setText(user.getAddr());
        mJobEt.setText(user.getJob());
        mContentEt.setText(user.getContent());
    }
}
