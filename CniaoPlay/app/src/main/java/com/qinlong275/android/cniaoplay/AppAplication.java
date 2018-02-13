package com.qinlong275.android.cniaoplay;

import android.app.Application;
import android.content.Context;
import android.view.View;

import com.google.gson.Gson;

import com.qinlong275.android.cniaoplay.di.component.AppComponent;
import com.qinlong275.android.cniaoplay.di.component.DaggerAppComponent;
import com.qinlong275.android.cniaoplay.di.module.AppModule;
import com.qinlong275.android.cniaoplay.di.module.HttpModule;

/**
 * Created by 秦龙 on 2018/2/6.
 */

public class AppAplication extends Application{

    private View mView;
    private AppComponent mAppComponent;

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    public static AppAplication get(Context context){
        return (AppAplication)context.getApplicationContext();
    }

    public View getView() {
        return mView;
    }

    public void setView(View view) {
        mView = view;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent=DaggerAppComponent.builder().appModule(new AppModule(this))
                .httpModule(new HttpModule()).build();
    }
}
