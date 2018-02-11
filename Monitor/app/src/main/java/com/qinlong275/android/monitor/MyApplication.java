package com.qinlong275.android.monitor;

import android.app.Application;
import android.content.Context;

/**
 * Created by 秦龙 on 2017/10/28.
 */

public class MyApplication extends Application{
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }

}
