package com.playmala.playmala.repository;


import com.playmala.playmala.model.Setting;
import com.playmala.playmala.util.PreferenceUtil;

public class SettingManager {

    private static final Class<Setting> CLAZZ = Setting.class;

    private Setting mSetting;

    public void saveOrUpdate(Setting setting) {
        if (setting == null) {
            return;
        }
        mSetting = setting;
        PreferenceUtil.set(CLAZZ.getName(), mSetting);
    }

    public Setting getSetting() {
        if (mSetting == null) {
            mSetting = PreferenceUtil.getObject(CLAZZ.getName(), CLAZZ);
        }

        return mSetting;
    }

    public void clear() {
        mSetting = null;
        PreferenceUtil.set(CLAZZ.getName(), "");
    }
}
