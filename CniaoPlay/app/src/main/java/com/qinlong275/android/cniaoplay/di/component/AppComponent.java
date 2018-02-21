package com.qinlong275.android.cniaoplay.di.component;

import android.app.Application;

import com.qinlong275.android.cniaoplay.common.DownloadModule;
import com.qinlong275.android.cniaoplay.common.rx.RxErrorHandler;
import com.qinlong275.android.cniaoplay.data.http.ApiService;
import com.qinlong275.android.cniaoplay.di.module.AppModule;
import com.qinlong275.android.cniaoplay.di.module.HttpModule;

import javax.inject.Singleton;

import dagger.Component;
import zlc.season.rxdownload2.RxDownload;

/**
 * Created by 秦龙 on 2018/2/6.
 */

@Singleton
@Component(modules = {AppModule.class, HttpModule.class, DownloadModule.class})
public interface AppComponent {

    public ApiService getApiService();

    public Application getApplication();

    public RxErrorHandler getRxErrorHandler();

    public RxDownload getRxDownload();
}
