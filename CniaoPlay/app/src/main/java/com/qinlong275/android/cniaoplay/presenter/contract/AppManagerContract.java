package com.qinlong275.android.cniaoplay.presenter.contract;


import com.qinlong275.android.cniaoplay.bean.AppInfo;
import com.qinlong275.android.cniaoplay.common.apkparset.AndroidApk;
import com.qinlong275.android.cniaoplay.ui.BaseView;


import java.util.List;

import io.reactivex.Observable;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadRecord;

/**
 * Created by Ivan on 2017/1/3.
 */

public interface AppManagerContract {


    interface AppManagerView extends BaseView {


        void showDownloading(List<DownloadRecord> downloadRecords);

        void showApps(List<AndroidApk> apps);

        void showUpdateApps(List<AppInfo> appInfos);

    }

    interface IAppManagerModel {


        Observable<List<DownloadRecord>> getDownloadRecord();

        RxDownload getRxDownload();


        Observable<List<AndroidApk>> getLocalApks();

        Observable<List<AndroidApk>> getInstalledApps();
    }


}
