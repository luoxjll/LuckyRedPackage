package com.ringkoo.luckyredpackage.config;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

/**
 * @author xj.luo
 * @email xj_luo@foxmail.com
 * @date Created on 2020/11/18
 */

public class ConfigManger {
    private static final ConfigManger OUR_INSTANCE = new ConfigManger();
    private SharedPreferences preferences;

    public ConfigManger() {

    }

    public void init(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static ConfigManger getInstance() {
        return OUR_INSTANCE;
    }

    public int getOpenDelayTime() {
        return Integer.parseInt(preferences.getString("edit_time_preference", "150"));
    }
}
