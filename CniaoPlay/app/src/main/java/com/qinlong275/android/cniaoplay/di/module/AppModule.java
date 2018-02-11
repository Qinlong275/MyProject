package com.qinlong275.android.cniaoplay.di.module;

import android.app.Application;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 秦龙 on 2018/2/6.
 */

@Module
public class AppModule {

    private Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    public Application provideApplication(){
        return mApplication;
    }

    @Provides
    @Singleton
    public Gson provideGson(){
        return new Gson();
    }

}
