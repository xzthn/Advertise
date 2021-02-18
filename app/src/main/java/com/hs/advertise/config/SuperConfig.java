package com.hs.advertise.config;

import android.content.Context;

/**
 * 配置文件的超类
 *
 * @author Allen
 */
public abstract class SuperConfig {

    /**
     * 返回配置文件的名称
     *
     * @return
     */
    public abstract String getConfig();


    public void putString(Context context, String key, String value) {
        context.getSharedPreferences(getConfig(), Context.MODE_PRIVATE).edit().putString(key, value).commit();
    }

    public String getString(Context context, String key) {
        return context.getSharedPreferences(getConfig(), Context.MODE_PRIVATE).getString(key, "");
    }

    public void putLong(Context context, String key, Long value) {
        context.getSharedPreferences(getConfig(), Context.MODE_PRIVATE).edit().putLong(key, value).commit();
    }

    public Long getLong(Context context, String key) {
        return context.getSharedPreferences(getConfig(), Context.MODE_PRIVATE).getLong(key, 0);
    }


    public void putBoolean(Context context, String key, boolean value) {
        context.getSharedPreferences(getConfig(), Context.MODE_PRIVATE).edit().putBoolean(key, value).commit();
    }

    public boolean getBoolean(Context context, String key) {
        return context.getSharedPreferences(getConfig(), Context.MODE_PRIVATE).getBoolean(key, false);
    }


    public void putInt(Context context, String key, int value) {
        context.getSharedPreferences(getConfig(), Context.MODE_PRIVATE).edit().putInt(key, value).commit();
    }

    public int getInt(Context context, String key) {
        return context.getSharedPreferences(getConfig(), Context.MODE_PRIVATE).getInt(key, 0);
    }

}
