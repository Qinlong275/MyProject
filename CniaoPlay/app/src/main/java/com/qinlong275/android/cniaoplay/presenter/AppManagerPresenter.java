package com.qinlong275.android.cniaoplay.presenter;



import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qinlong275.android.cniaoplay.bean.AppInfo;
import com.qinlong275.android.cniaoplay.common.Constant;
import com.qinlong275.android.cniaoplay.common.apkparset.AndroidApk;
import com.qinlong275.android.cniaoplay.common.rx.RxSchedulers;
import com.qinlong275.android.cniaoplay.common.rx.subscriber.ProgressSubscriber;
import com.qinlong275.android.cniaoplay.common.util.ACache;
import com.qinlong275.android.cniaoplay.presenter.contract.AppManagerContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadFlag;
import zlc.season.rxdownload2.entity.DownloadRecord;

/**
 * 菜鸟窝http://www.cniao5.com 一个高端的互联网技能学习平台
 *
 * @author Ivan
 * @version V1.0
 * @Package com.cniao5.cniao5play.presenter
 * @Description: ${TODO}(用一句话描述该文件做什么)
 * @date
 */

public class AppManagerPresenter extends BasePresenter<AppManagerContract.IAppManagerModel,AppManagerContract.AppManagerView> {

    @Inject
    public AppManagerPresenter(AppManagerContract.IAppManagerModel iAppManagerModel, AppManagerContract.AppManagerView appManagerView) {
        super(iAppManagerModel, appManagerView);
    }

    public void getDownlodingApps(){


        mModel.getDownloadRecord().compose(RxSchedulers.<List<DownloadRecord>>io_main())


                .subscribe(new ProgressSubscriber<List<DownloadRecord>>(mContext,mView) {
                    @Override
                    public void onNext(List<DownloadRecord> downloadRecords) {

                        mView.showDownloading(downloadRecordFilter(downloadRecords));

                    }
                });


    }



    public void getLocalApks(){


        mModel.getLocalApks().compose(RxSchedulers.<List<AndroidApk>>io_main())
                .subscribe(new ProgressSubscriber<List<AndroidApk>>(mContext,mView) {
                    @Override
                    public void onNext(List<AndroidApk> androidApks) {
                        mView.showApps(androidApks);
                    }
                });
    }

    public void getInstalledApps(){

        mModel.getInstalledApps().compose(RxSchedulers.<List<AndroidApk>>io_main())
                .subscribe(new ProgressSubscriber<List<AndroidApk>>(mContext,mView) {
                    @Override
                    public void onNext(List<AndroidApk> androidApks) {
                        mView.showApps(androidApks);
                    }
                });
    }


    public void  getUpdateApps(){


        //mainactivity中存入
        String json =   ACache.get(mContext).getAsString(Constant.APP_UPDATE_LIST);


        if(!TextUtils.isEmpty(json)){

            Gson gson = new Gson();
            List<AppInfo> apps = gson.fromJson(json,new TypeToken<List<AppInfo>>(){}.getType());


            Observable.just(apps)
                    .compose(RxSchedulers.<List<AppInfo>>io_main())

                    .subscribe(new ProgressSubscriber<List<AppInfo>>(mContext,mView) {
                        @Override
                        public void onNext(List<AppInfo> appInfos) {

                            mView.showUpdateApps(appInfos);
                        }
                    });


        }


    }




    public RxDownload geRxDowanload(){

        return mModel.getRxDownload();
    }



    private List<DownloadRecord> downloadRecordFilter(List<DownloadRecord> downloadRecords){

        List<DownloadRecord> newList = new ArrayList<>();
        for (DownloadRecord r :downloadRecords){

            if(r.getFlag() != DownloadFlag.COMPLETED){

                newList.add(r);
            }
        }

        return newList;

    }
}
