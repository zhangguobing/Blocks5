package com.zjonline.blocks5.util;

import com.zjonline.blocks5.model.Config;
import com.zjonline.blocks5.repository.ConfigManager;

import java.util.List;

/**
 * author：zhangguobing on 2017/7/4 14:15
 * email：bing901222@qq.com
 */

public class ActivityDataConvert {

    public static String getActivityTypeNameById(int activity_type_id) {
        String activityTypeName = "";
        List<Config.ActivityTypesBean> activityTypes = ConfigManager.getInstance().getConfig().getActivity_types();
        if (activityTypes != null && activityTypes.size() > 0) {
            for (int i = 0; i < activityTypes.size(); i++) {
                Config.ActivityTypesBean activityType = activityTypes.get(i);
                if (activity_type_id == activityType.getId()) {
                    activityTypeName = activityType.getName();
                    break;
                }
            }
            return activityTypeName;
        }
        return activityTypeName;
    }

    public static String getActivityStateById(String activity_state_id){
        String activityStateName = "";
        List<Config.ActivityStatesBean> activityStates = ConfigManager.getInstance().getConfig().getActivity_states();
        if (activityStates != null && activityStates.size() > 0) {
            for (int i = 0; i < activityStates.size(); i++) {
                Config.ActivityStatesBean activityState = activityStates.get(i);
                if (activity_state_id.equals(activityState.getId())) {
                    activityStateName = activityState.getName();
                    break;
                }
            }
            return activityStateName;
        }
        return activityStateName;
    }

    public static String getIdentityStateById(String identity_id){
        String identityState = "";
        List<Config.UserIdentityStatesBean> identityStatesBeans = ConfigManager.getInstance().getConfig().getUser_identity_states();
        if(identityStatesBeans != null && identityStatesBeans.size() > 0){
            for (int i = 0; i < identityStatesBeans.size(); i++) {
                Config.UserIdentityStatesBean identityStatesBean = identityStatesBeans.get(i);
                if(identity_id.equals(identityStatesBean.getId())){
                    identityState = identityStatesBean.getName();
                    break;
                }
            }
            return identityState;
        }
        return identityState;
    }
}
