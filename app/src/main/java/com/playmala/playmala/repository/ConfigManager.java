package com.playmala.playmala.repository;

import com.playmala.playmala.model.Config;
import com.playmala.playmala.util.PreferenceUtil;

public class ConfigManager {

    private static final Class<Config> CLAZZ = Config.class;

    private Config mConfig;

    private static ConfigManager instance;

    private ConfigManager(){}

    public static ConfigManager getInstance(){
        if(instance == null){
            synchronized (ConfigManager.class){
                if(instance == null){
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    public void saveOrUpdateConfig(Config config) {
        if (config == null) {
            return;
        }
        this.mConfig = config;
        PreferenceUtil.set(CLAZZ.getName(), this.mConfig);
    }

    public Config getConfig(){
        if (mConfig == null) {
            mConfig = PreferenceUtil.getObject(CLAZZ.getName(), CLAZZ);
        }
        return mConfig;
    }

    public void clear() {
        mConfig = null;
        PreferenceUtil.set(CLAZZ.getName(), "");
    }
}
