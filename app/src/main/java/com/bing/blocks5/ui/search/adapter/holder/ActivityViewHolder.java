package com.bing.blocks5.ui.search.adapter.holder;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bing.blocks5.R;
import com.bing.blocks5.base.BaseViewHolder;
import com.bing.blocks5.model.Activity;
import com.bing.blocks5.model.Config;
import com.bing.blocks5.repository.ConfigManager;
import com.bing.blocks5.util.ActivityDataConvert;
import com.bing.blocks5.util.ImageLoadUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：zhangguobing on 2017/7/2 14:11
 * email：bing901222@qq.com
 */

public class ActivityViewHolder extends BaseViewHolder<Activity> {
    @Bind(R.id.iv_activity)
    ImageView mActivityImage;
    @Bind(R.id.tv_activity_type)
    TextView mActivityTypeTv;
    @Bind(R.id.tv_activity_time)
    TextView mActivityTimeTv;
    @Bind(R.id.tv_creator_name_and_sex)
    TextView mCreatorNameAndSex;
    @Bind(R.id.tv_ceator_credit)
    TextView mCeatorCredit;
    @Bind(R.id.tv_activity_state_and_area)
    TextView mStateAndAreaTv;
    @Bind(R.id.tv_activity_people_num_and_left)
    TextView mPeopleNumAndLeftTv;

    private List<Config.ActivityTypesBean> mActivityTypes;
    private List<Config.ActivityStatesBean> mActivityStates;

    public ActivityViewHolder(View view) {
        super(view);
        mActivityTypes = ConfigManager.getInstance().getConfig().getActivity_types();
        mActivityStates = ConfigManager.getInstance().getConfig().getActivity_states();
    }

    public void bind(Activity activity){
        ImageLoadUtil.loadImage(mActivityImage,activity.getCover_url(),getContext());
        mActivityTypeTv.setText(ActivityDataConvert.getActivityTypeNameById(activity.getActivity_type_id()));
//        mActivityTypeTv.setText(getActivityTypeNameById(activity.getActivity_type_id()));
        mActivityTimeTv.setText(activity.getCreated_at() + "-" + activity.getEnd_at());
        mCreatorNameAndSex.setText(activity.getCreator().getNick_name());
        int sexDrawableId = "男".equals(activity.getCreator().getSex()) ? R.mipmap.ic_male : R.mipmap.ic_female;
        Drawable sexDrawable = getDrawable(sexDrawableId);
        sexDrawable.setBounds(0,0,sexDrawable.getIntrinsicWidth(),sexDrawable.getIntrinsicHeight());
        mCreatorNameAndSex.setCompoundDrawables(null,null,sexDrawable,null);
        mCeatorCredit.setText("信用：" + activity.getCreator().getCredit());
//        mStateAndAreaTv.setText(getActivityStateById(activity.getState()+"")+ "|" + activity.getArea());
        mStateAndAreaTv.setText(ActivityDataConvert.getActivityStateById(activity.getState()+"") + "|" + activity.getArea());
        mPeopleNumAndLeftTv.setText(activity.getMan_num() + "男" + activity.getWoman_num() + "女"+"(余" +
        activity.getMan_left() + "男" + activity.getWoman_left() + "女)");
    }

    private String getActivityTypeNameById(int activity_type_id) {
        String activityTypeName = "";
        if (mActivityTypes != null && mActivityTypes.size() > 0) {
            for (int i = 0; i < mActivityTypes.size(); i++) {
                Config.ActivityTypesBean activityType = mActivityTypes.get(i);
                if (activity_type_id == activityType.getId()) {
                    activityTypeName = activityType.getName();
                    break;
                }
            }
            return activityTypeName;
        }
        return activityTypeName;
    }

    private String getActivityStateById(String activity_state_id){
        String activityStateName = "";
        if (mActivityStates != null && mActivityStates.size() > 0) {
            for (int i = 0; i < mActivityStates.size(); i++) {
                Config.ActivityStatesBean activityState = mActivityStates.get(i);
                if (activity_state_id.equals(activityState.getId())) {
                    activityStateName = activityState.getName();
                    break;
                }
            }
            return activityStateName;
        }
        return activityStateName;
    }
}
