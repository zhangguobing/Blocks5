package com.bing.blocks5.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bing.blocks5.AppCookie;
import com.bing.blocks5.base.BaseController;
import com.bing.blocks5.ui.common.GalleryActivity;
import com.bing.blocks5.util.AsyncRun;
import com.bing.blocks5.util.ImageLoadUtil;
import com.bing.blocks5.util.QiniuUploadUtils;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.lcodecore.tkrefreshlayout.utils.DensityUtil;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.bing.blocks5.R;
import com.bing.blocks5.album.Album;
import com.bing.blocks5.base.BasePresenterActivity;
import com.bing.blocks5.base.ContentView;
import com.bing.blocks5.model.Config;
import com.bing.blocks5.controller.ActivityController;
import com.bing.blocks5.repository.ConfigManager;
import com.bing.blocks5.model.request.CreateActivityParams;
import com.bing.blocks5.util.ToastUtil;
import com.bing.blocks5.widget.SwitchButton;
import com.bing.blocks5.widget.TitleBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * author：zhangguobing on 2017/6/25 14:47
 * email：bing901222@qq.com
 */
@ContentView(R.layout.activity_add_activity)
public class AddActivityActivity extends BasePresenterActivity<ActivityController.ActivityUiCallbacks>
      implements ActivityController.CreateActivityUi,Validator.ValidationListener,View.OnClickListener {

    private static final int ACTIVITY_REQUEST_SELECT_COVER = 100;
    private static final int ACTIVITY_REQUEST_SELECT_PICTURE = 101;
    private static final int ACTIVITY_REQUEST_EDIT_PICTURE = 102;

    /**
     * 上次图片的最大个数
     */
    private static final int PICTURE_MAX_COUNT = 3;

    @NotEmpty(message = "请输入开始时间")
    @Bind(R.id.tv_start_time)
    TextView mStartTimeTv;
    @NotEmpty(message = "请输入结束时间")
    @Bind(R.id.tv_end_time)
    TextView mEndTimeTv;
    @NotEmpty(message = "请选择区域")
    @Bind(R.id.tv_select_area)
    TextView mSelectAreaTv;
    @NotEmpty(message = "请选择类型")
    @Bind(R.id.tv_type)
    TextView mTypeTv;
    @Checked(message = "请选择平摊方式")
    @Bind(R.id.rg_price_type)
    RadioGroup mPriceTypeRg;
    @NotEmpty(message = "请输入名称")
    @Bind(R.id.et_activity_name)
    EditText mActivityNameEt;
    @NotEmpty(message = "请选择人数")
    @Bind(R.id.tv_people_num)
    TextView mPeopleNumTv;
    @NotEmpty(message = "请输入总费用")
    @Bind(R.id.et_total_price)
    EditText mTotalPriceEt;
//    @NotEmpty(message = "请输入费用说明")
    @Bind(R.id.et_price_content)
    EditText mPriceContentEt;
//    @NotEmpty(message = "请输入留言")
    @Bind(R.id.et_activity_content)
    EditText mActivityContentEt;
    @Bind(R.id.sb_need_identity)
    SwitchButton mNeedIdentitySwitch;
    @Bind(R.id.iv_upload_cover)
    ImageView mCoverImg;
    @Bind(R.id.picture_container)
    LinearLayout mPictureContainer;
    @Bind(R.id.iv_upload_pictures)
    ImageView mUploadPicturesImg;

    //选中的类型位置
    private int mSelectedTypePosition = -1;
    //选中的人数，男多少人，女多少人
    private List<Integer> mSelectedPeopleNum;
    //选中的起始时间
    private Date mSelectedStartDate;
    //选中的区域
    private String mSelectedArea;

    private Validator mValidator;

    private KProgressHUD mUploadProgressDialog;

    private String mCoverUrl = "";

    private ArrayList<String> mPictureUrls = new ArrayList<>();

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        TitleBar titleBar = getTitleBar();
        if (titleBar != null) {
            titleBar.setLeftTextColor(ContextCompat.getColor(this, R.color.white));
            titleBar.setLeftText("取消");
        }
        initPriceTypeRadioButton();

        mUploadProgressDialog = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.PIE_DETERMINATE)
                .setLabel(getString(R.string.lable_being_upload)).setMaxProgress(100);

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
    }

    public static void create(Activity activity) {
        Intent intent = new Intent(activity, AddActivityActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_top, R.anim.activity_stay);
    }


    private void initPriceTypeRadioButton(){
       List<Config.ActivityPriceTypesBean> priceTypesBeanList = ConfigManager.getInstance().getConfig().getActivity_price_types();
       if(priceTypesBeanList != null && priceTypesBeanList.size() > 0){
           for (Config.ActivityPriceTypesBean priceTypesBean: priceTypesBeanList){
               RadioButton radioButton = new RadioButton(this);
               RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
               layoutParams.leftMargin = DensityUtil.dp2px(this,15);
               radioButton.setLayoutParams(layoutParams);
               int padding = DensityUtil.dp2px(this,8);
               radioButton.setPadding(padding,padding,padding,padding);
               radioButton.setTextColor(getResources().getColorStateList(R.color.select_color_red));
               radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
               radioButton.setButtonDrawable(null);
               radioButton.setBackgroundResource(R.drawable.bg_radio_button_2);
               radioButton.setText(priceTypesBean.getName());
               radioButton.setId(Integer.parseInt(priceTypesBean.getId()));
               mPriceTypeRg.addView(radioButton);
           }
       }
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_stay,R.anim.slide_out_bottom);
    }

    @OnClick({R.id.iv_upload_pictures, R.id.iv_upload_cover,
            R.id.tv_start_time, R.id.tv_end_time,R.id.tv_select_area,
            R.id.tv_type,R.id.tv_people_num,R.id.btn_create_activity})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_upload_pictures:
                fromAlbum(PICTURE_MAX_COUNT - mPictureContainer.getChildCount(),ACTIVITY_REQUEST_SELECT_PICTURE);
                break;
            case R.id.iv_upload_cover:
                fromAlbum(1,ACTIVITY_REQUEST_SELECT_COVER);
                break;
            case R.id.tv_start_time:
                showStartTimePicker();
                break;
            case R.id.tv_end_time:
                showEndTimePicker();
                break;
            case R.id.tv_select_area:
                showAreaPickerView();
                break;
            case R.id.tv_type:
                showTypePickerView();
                break;
            case R.id.tv_people_num:
                showPeopleNumPickerView();
                break;
            case R.id.btn_create_activity:
                mValidator.validate();
                break;
            case R.id.add_activity_picture_id:
                if(view.getTag() != null && mPictureUrls.size() > 0){
                    String url = (String) view.getTag();
                    int index = mPictureUrls.indexOf(url);
                    if(index != -1) showGallery(view,index);
                }
                break;
        }
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
                .camera(false)
                .columnCount(3)
                .title("相册")
                .start();
    }


    private void showStartTimePicker(){
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        //时间选择器
        TimePickerView startPickerView = new TimePickerView.Builder(this, (date, v) -> {
            mStartTimeTv.setText(getTime(date));
            mSelectedStartDate = date;
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, true, true, false})
                .setLabel("年", "月", "日", "时", "分", "")
                .isCenterLabel(false)
                .setTitleText("请选择开始时间")
                .setTitleColor(ContextCompat.getColor(this,R.color.red))
                .setContentSize(18)
                .setDate(selectedDate)
                .setRangDate(startDate, null)
                .setBackgroundId(0x00FFFFFF)
                .setDecorView(null)
                .build();
        startPickerView.show();
    }

    private void showEndTimePicker() {
        Calendar selectedDate = Calendar.getInstance();
        if(mSelectedStartDate != null){
            selectedDate.setTime(mSelectedStartDate);
        }
        Calendar startDate = Calendar.getInstance();
        TimePickerView endPickerView = new TimePickerView.Builder(this, (date, v) -> {
            mEndTimeTv.setText(getTime(date));
        })
            .setType(new boolean[]{true, true, true, true, true, false})
            .setLabel("年", "月", "日", "时", "分", "")
            .isCenterLabel(false)
            .setContentSize(18)
            .setTitleText("请选择结束时间")
            .setTitleColor(ContextCompat.getColor(this,R.color.red))
            .setDate(selectedDate)
            .setRangDate(startDate, null)
            .setBackgroundId(0x00FFFFFF)
            .setDecorView(null)
            .build();
        endPickerView.setDate(selectedDate);
        endPickerView.show();
    }

    private String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return format.format(date);
    }

    private void showAreaPickerView() {
        List<Config.ActivityAreasBean> areasBeanList = ConfigManager.getInstance().getConfig().getActivity_areas();
        if(areasBeanList != null && areasBeanList.size() > 0){
            String currentCity = AppCookie.getCity();
            boolean isFind = false;
            for (Config.ActivityAreasBean activityArea : areasBeanList){
                if(activityArea.getCity().equals(currentCity)){
                    isFind = true;
                    List<String> activityAreas = activityArea.getAreas();
                    OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, (options1, options2, options3, v) -> {
                        mSelectAreaTv.setText(activityAreas.get(options1));
                        mSelectedArea = activityAreas.get(options1);
                    })
                            .setTextColorCenter(ContextCompat.getColor(this,R.color.primary_text))
                            .setContentTextSize(18)
                            .build();
                    pvOptions.setPicker(activityAreas);
                    pvOptions.show();
                    break;
                }
            }
            if(!isFind) ToastUtil.showText("暂不支持当前城市");
        }
    }

    private void showPeopleNumPickerView() {
        if(mSelectedTypePosition == -1){
            ToastUtil.showText("请先选择类型");
            return;
        }
        Config.ActivityTypesBean activityType = ConfigManager.getInstance().getConfig().getActivity_types().get(mSelectedTypePosition);
        List<String> formatPeopleNum = new ArrayList<>();
        for (List<Integer> peoples : activityType.getPeoples()){
            if(peoples != null && peoples.size() == 2){
                formatPeopleNum.add(getString(R.string.format_male_female_people_num,peoples.get(0).toString(),peoples.get(1).toString()));
            }
        }
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, (options1, options2, options3, v) -> {
            mPeopleNumTv.setText(formatPeopleNum.get(options1));
            mSelectedPeopleNum = activityType.getPeoples().get(options1);
        })
        .setTextColorCenter(ContextCompat.getColor(this,R.color.primary_text))
        .setContentTextSize(18)
        .build();
        pvOptions.setPicker(formatPeopleNum);
        pvOptions.show();
    }

    private void showTypePickerView() {
        List<Config.ActivityTypesBean> activityTypes = ConfigManager.getInstance().getConfig().getActivity_types();
        if(activityTypes != null && activityTypes.size() > 0){
            OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, (options1, options2, options3, v) -> {
                mTypeTv.setText(activityTypes.get(options1).getName());
                mSelectedTypePosition = options1;
            })
            .setTextColorCenter(ContextCompat.getColor(this,R.color.primary_text))
            .setContentTextSize(18)
            .build();
            pvOptions.setPicker(activityTypes);
            pvOptions.show();
        }
    }

    @Override
    protected BaseController getPresenter() {
        return new ActivityController();
    }

    @Override
    public void createActivitySuccess(String msg) {
        cancelLoading();
        ToastUtil.showText("创建成功");
        CreatedActivity.create(this,AppCookie.getUserInfo().getId(),"我创建的活动");
        finish();
    }

    @Override
    public void onValidationSucceeded() {
        showLoading(R.string.label_being_something);
        CreateActivityParams params = new CreateActivityParams();
        params.activity_type_id = ConfigManager.getInstance().getConfig().getActivity_types().get(mSelectedTypePosition).getId();
        params.title = mActivityNameEt.getText().toString().trim();
        params.city =  AppCookie.getCity();
        params.area = mSelectedArea;
        params.man_num = mSelectedPeopleNum.get(0);
        params.woman_num = mSelectedPeopleNum.get(1);
        params.begin_at = mStartTimeTv.getText().toString();
        params.end_at = mEndTimeTv.getText().toString();
        params.price_total = Integer.parseInt(mTotalPriceEt.getText().toString().trim());
        params.price_type = mPriceTypeRg.getCheckedRadioButtonId();
        params.price_content = mPriceContentEt.getText().toString().trim();
        params.need_identity = mNeedIdentitySwitch.isChecked() ? "1" : "0";
        params.cover_url = mCoverUrl;
        for (int i = 0; i < mPictureContainer.getChildCount(); i++) {
            View childView = mPictureContainer.getChildAt(i);
            String url = childView.getTag().toString();
            if(i == 0){
                params.img_url_1 = url;
            }else if(i == 1){
                params.img_url_2 = url;
            }else if(i == 2){
                params.img_url_3 = url;
            }
        }
        params.content = mActivityContentEt.getText().toString().trim();
        getCallbacks().createActivity(params);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
//            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            ToastUtil.showText(message);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case ACTIVITY_REQUEST_SELECT_COVER: {
                    if (data != null) {
                        String filePath = Album.parseResult(data).get(0);
                        QiniuUploadUtils.getInstance().uploadImage(filePath, new QiniuUploadUtils.QiniuUploadUtilsListener() {
                            @Override
                            public void onStart() {
                                mUploadProgressDialog.show();
                            }

                            @Override
                            public void onSuccess(String originFilePath,String destUrl) {
                                mUploadProgressDialog.dismiss();
                                ImageLoadUtil.loadImage(mCoverImg,originFilePath,AddActivityActivity.this);
                                mCoverUrl = destUrl;
                            }

                            @Override
                            public void onError(int errorCode, String msg) {
                                mUploadProgressDialog.dismiss();
                                ToastUtil.showText("上传异常");
                            }

                            @Override
                            public void onProgress(int progress) {
                                mUploadProgressDialog.setProgress(progress);
                            }
                        });
                    }
                    break;
                }
                case ACTIVITY_REQUEST_SELECT_PICTURE:
                    if(data != null) {
                        List<String> filePaths = Album.parseResult(data);
                        QiniuUploadUtils.getInstance().uploadImages(filePaths, new QiniuUploadUtils.QiniuUploadUtilsListener() {
                            @Override
                            public void onStart() {
                                AsyncRun.runInMain(() -> mUploadProgressDialog.show());
                            }

                            @Override
                            public void onSuccess(String originFilePath,String fileUrl) {
                                AsyncRun.runInMain(() -> {
                                    mUploadProgressDialog.dismiss();
                                    addChildViewToPicture(originFilePath,fileUrl);
                                    mPictureUrls.add(fileUrl);
                                });
                            }

                            @Override
                            public void onError(int errorCode, String msg) {
                                AsyncRun.runInMain(() -> {
                                    mUploadProgressDialog.dismiss();
                                    ToastUtil.showText("上传异常");
                                });

                            }

                            @Override
                            public void onProgress(int progress) {
                                AsyncRun.runInMain(() -> mUploadProgressDialog.setProgress(progress));
                            }
                        });
                        break;
                    }
                case ACTIVITY_REQUEST_EDIT_PICTURE:
                    mPictureUrls.clear();
                    mPictureUrls.addAll(GalleryActivity.parseResult(data));
                    mPictureContainer.removeAllViews();
                    for (String imageUrl : mPictureUrls){
                        addChildViewToPicture(null,imageUrl);
                    }
                    if(mPictureUrls.size() == 0)  mUploadPicturesImg.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }


    /**
     * 往容器内添加相应的子View
     */
    private void addChildViewToPicture(String filePath,String imageUrl){
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.width = DensityUtil.dp2px(this,42);
        layoutParams.leftMargin = DensityUtil.dp2px(this,20);
        imageView.setId(R.id.add_activity_picture_id);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setOnClickListener(this);
        mPictureContainer.addView(imageView);
        ImageLoadUtil.loadImage(imageView,filePath,this);
        if(filePath == null && imageUrl != null) ImageLoadUtil.loadImage(imageView,imageUrl,this);
        imageView.setTag(imageUrl);

        mUploadPicturesImg.setVisibility(mPictureContainer.getChildCount() == PICTURE_MAX_COUNT ? View.GONE : View.VISIBLE);
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
        b.putStringArrayList(GalleryActivity.PHOTO_SOURCE_ID, mPictureUrls);
        intent.putExtras(b);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_POSITION, position);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_X_TAG, location[0]);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_Y_TAG, location[1]);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_W_TAG, width);
        intent.putExtra(GalleryActivity.PHOTO_SELECT_H_TAG, height);
        intent.putExtra(GalleryActivity.PHOTO_IS_SHOW_TITLE_BAR, true);
        startActivityForResult(intent,ACTIVITY_REQUEST_EDIT_PICTURE);
    }
}
