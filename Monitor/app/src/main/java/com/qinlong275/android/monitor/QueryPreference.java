package com.qinlong275.android.monitor;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by 秦龙 on 2017/10/16.
 */

public class QueryPreference {
    public static String getStoredString(Context context,String option){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(option,null);
    }

    public static boolean getStoredChecked(Context context,String option){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(option,false);
    }

    public static void setStoredString(Context context,String tag,String content){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(tag,content)
                .apply();
    }

    public static void setStoredBoolean(Context context,String tag,boolean content){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(tag,content)
                .apply();
    }
}
