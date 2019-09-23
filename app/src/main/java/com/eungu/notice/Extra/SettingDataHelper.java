package com.eungu.notice.Extra;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingDataHelper {
    public static final String PREFS_NAME = "SettingFile";
    public static final String MAIN_TITLE = "MainTitle";
    public static final String MAIN_CONTENT = "MainContent";
    public static final String MAIN_CATEGORY = "category";
    public static final String NONE = "none";
    public static final String URL = "internet";
    public static final String CALL = "phonecall";
    public static final String APP = "app";
    Context context;

    public SettingDataHelper(Context context) {
        this.context = context;
    }

    public void settingStringData(String key, String value){
        SharedPreferences setting = context.getSharedPreferences(SettingDataHelper.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getStringData(String key, String defaultValue){
        SharedPreferences setting = context.getSharedPreferences(SettingDataHelper.PREFS_NAME, Context.MODE_PRIVATE);
        return setting.getString(key, defaultValue);
    }
}
