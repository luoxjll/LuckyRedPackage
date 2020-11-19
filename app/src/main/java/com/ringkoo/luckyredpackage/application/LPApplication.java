package com.ringkoo.luckyredpackage.application;

import android.app.Application;

import androidx.annotation.NonNull;

import com.ringkoo.luckyredpackage.config.ConfigManger;

/**
 * @author xj.luo
 * @email xj_luo@foxmail.com
 * @date Created on 2020/11/17
 */

public class LPApplication extends Application {

    private static LPApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        if (mInstance == null) {
            mInstance = this;
        }

        ConfigManger.getInstance().init(this);
    }

    @NonNull
    public static LPApplication getInstance() {
        return mInstance;
    }
}
