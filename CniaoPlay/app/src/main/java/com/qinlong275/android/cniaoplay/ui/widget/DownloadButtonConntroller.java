package com.qinlong275.android.cniaoplay.ui.widget;

import android.content.Context;
import android.util.Log;


import com.jakewharton.rxbinding2.view.RxView;
import com.qinlong275.android.cniaoplay.R;
import com.qinlong275.android.cniaoplay.bean.AppDownloadInfo;
import com.qinlong275.android.cniaoplay.bean.AppInfo;
import com.qinlong275.android.cniaoplay.bean.BaseBean;
import com.qinlong275.android.cniaoplay.common.Constant;
import com.qinlong275.android.cniaoplay.common.rx.RxHttpResponseCompat;
import com.qinlong275.android.cniaoplay.common.rx.RxSchedulers;
import com.qinlong275.android.cniaoplay.common.util.ACache;
import com.qinlong275.android.cniaoplay.common.util.AppUtils;
import com.qinlong275.android.cniaoplay.common.util.PackageUtils;
import com.qinlong275.android.cniaoplay.common.util.PermissionUtil;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import retrofit2.http.GET;
import retrofit2.http.Path;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadBean;
import zlc.season.rxdownload2.entity.DownloadEvent;
import zlc.season.rxdownload2.entity.DownloadFlag;
import zlc.season.rxdownload2.entity.DownloadRecord;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * 菜鸟窝http://www.cniao5.com 一个高端的互联网技能学习平台
 *
 * @author Ivan
 * @version V1.0
 * @Package com.cniao5.cniao5play.ui.widget
 * @Description: ${TODO}(用一句话描述该文件做什么)
 * @date
 */

public class DownloadButtonConntroller {


    private RxDownload mRxDownload ;


//    private String mDownloadDir; // 文件下载的目录


    private Api mApi;


    public DownloadButtonConntroller(RxDownload downloader){

        this.mRxDownload = downloader;

        if(mRxDownload !=null){


            mApi = mRxDownload.getRetrofit().create(Api.class);
        }

    }


    public  void  handClick(final DownloadProgressButton btn, DownloadRecord record){

        AppInfo info =downloadRecord2AppInfo(record);

        receiveDownloadStatus(record.getUrl()).subscribe(new DownloadConsumer(btn,info));

    }


    public  void  handClick(final DownloadProgressButton btn, final AppInfo appInfo){



        if(mApi == null){

            return;
        }




        isAppInstalled(btn.getContext(),appInfo)

                .flatMap(new Function<DownloadEvent, ObservableSource<DownloadEvent>>() {
                    @Override
                    public ObservableSource<DownloadEvent> apply(@NonNull DownloadEvent event)
                            throws Exception {

                        if(DownloadFlag.UN_INSTALL == event.getFlag()){

                            return  isApkFileExsit(btn.getContext(),appInfo);

                        }
                        return  Observable.just(event);


                    }
                })
                .flatMap(new Function<DownloadEvent, ObservableSource<DownloadEvent>>() {
                    @Override
                    public ObservableSource<DownloadEvent> apply(@NonNull DownloadEvent event) throws Exception {


                        if(DownloadFlag.FILE_EXIST == event.getFlag()){

                            return  getAppDownloadInfo(appInfo)
                                    .flatMap(new Function<AppDownloadInfo, ObservableSource<DownloadEvent>>() {
                                        @Override
                                        public ObservableSource<DownloadEvent> apply(@NonNull AppDownloadInfo appDownloadInfo) throws Exception {

                                            appInfo.setAppDownloadInfo(appDownloadInfo);

                                            return receiveDownloadStatus(appDownloadInfo.getDownloadUrl());
                                        }
                                    });

                        }


                        return Observable.just(event);
                    }
                })
                .compose(RxSchedulers.<DownloadEvent>io_main())

                .subscribe(new DownloadConsumer(btn,appInfo));





    }

    private void bindClick(final DownloadProgressButton btn, final AppInfo appInfo) {

        RxView.clicks(btn).subscribe(new Consumer<Object>() {


            @Override
            public void accept(@NonNull Object o) throws Exception {


                int flag = (int) btn.getTag(R.id.tag_apk_flag);

                Log.d("downloadcontroler","flags="+flag);

                switch (flag){

                    case DownloadFlag.INSTALLED:

                        runApp(btn.getContext(),appInfo.getPackageName());
                        break;


                    // 升级 还加上去

                    case DownloadFlag.STARTED:
                        pausedDownload(appInfo.getAppDownloadInfo().getDownloadUrl());
                        break;


                    case DownloadFlag.NORMAL:
                    case DownloadFlag.PAUSED:
                        startDownload(btn,appInfo);

                        break;

                    case DownloadFlag.COMPLETED:
                        installApp(btn.getContext(),appInfo);

                        break;

                }


            }
        });
    }

    private void installApp(Context context,AppInfo appInfo) {

        Log.d("downloadcontroler","开始安装");
//        mRxDownload.getRealFiles()
        String path = ACache.get(context).getAsString(Constant.APK_DOWNLOAD_DIR) + File.separator + appInfo.getReleaseKeyHash();

        PackageUtils.install(context,path);
    }

    private void startDownload(final DownloadProgressButton btn, final AppInfo appInfo) {


        PermissionUtil.requestPermisson(btn.getContext(),WRITE_EXTERNAL_STORAGE)


                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {


                        if(aBoolean){

                            final AppDownloadInfo downloadInfo = appInfo.getAppDownloadInfo();

                            if(downloadInfo==null){

                                getAppDownloadInfo(appInfo).subscribe(new Consumer<AppDownloadInfo>() {
                                    @Override
                                    public void accept(@NonNull AppDownloadInfo appDownloadInfo) throws Exception {

                                        appInfo.setAppDownloadInfo(appDownloadInfo);

                                        download(btn,appInfo);

                                    }
                                });
                            }
                            else {

                                download(btn,appInfo);
                            }
                        }

                    }
                });






    }

    private void download(DownloadProgressButton btn,AppInfo info){


        mRxDownload.serviceDownload(appInfo2DownloadBean(info)).subscribe();

        mRxDownload.receiveDownloadStatus(info.getAppDownloadInfo().getDownloadUrl()).subscribe(new DownloadConsumer(btn,info));

    }


    private DownloadBean appInfo2DownloadBean(AppInfo info){

        DownloadBean downloadBean = new DownloadBean();

        downloadBean.setUrl(info.getAppDownloadInfo().getDownloadUrl());
        downloadBean.setSaveName(info.getReleaseKeyHash() +".apk");


        downloadBean.setExtra1(info.getId()+"");
        downloadBean.setExtra2(info.getIcon());
        downloadBean.setExtra3(info.getDisplayName());
        downloadBean.setExtra4(info.getPackageName());
        downloadBean.setExtra5(info.getReleaseKeyHash());

        return  downloadBean;
    }

    public AppInfo downloadRecord2AppInfo(DownloadRecord bean){


        AppInfo info = new AppInfo();

        info.setId(Integer.parseInt(bean.getExtra1()));
        info.setIcon(bean.getExtra2());
        info.setDisplayName(bean.getExtra3());
        info.setPackageName(bean.getExtra4());
        info.setReleaseKeyHash(bean.getExtra5());


        AppDownloadInfo downloadInfo = new AppDownloadInfo();

        downloadInfo.setDowanloadUrl(bean.getUrl());

        info.setAppDownloadInfo(downloadInfo);

        return info;



    }

    private void pausedDownload(String  url) {



        mRxDownload.pauseServiceDownload(url).subscribe();
    }

    private void runApp(Context context,String packageName) {

        AppUtils.runApp(context,packageName);
    }


    public Observable<DownloadEvent> isAppInstalled(Context context,AppInfo appInfo){



        DownloadEvent event = new DownloadEvent();

        event.setFlag(AppUtils.isInstalled(context,appInfo.getPackageName())? DownloadFlag.INSTALLED:DownloadFlag.UN_INSTALL);

        return  Observable.just(event);

    }



    public  Observable<DownloadEvent> isApkFileExsit(Context context,AppInfo appInfo){


        String path = ACache.get(context).getAsString(Constant.APK_DOWNLOAD_DIR) + File.separator + appInfo.getReleaseKeyHash();
        File file = new File(path);


        DownloadEvent event = new DownloadEvent();

        event.setFlag(file.exists()? DownloadFlag.FILE_EXIST:DownloadFlag.NORMAL);

        return  Observable.just(event);




    }




    public Observable<DownloadEvent> receiveDownloadStatus(String url){

        return  mRxDownload.receiveDownloadStatus(url);
    }




    public Observable<AppDownloadInfo> getAppDownloadInfo(AppInfo appInfo){

        return  mApi.getAppDownloadInfo(appInfo.getId()).compose(RxHttpResponseCompat.<AppDownloadInfo>compatResult());
    }



    class  DownloadConsumer implements  Consumer<DownloadEvent>{

        DownloadProgressButton btn ;

        AppInfo mAppInfo;

        public DownloadConsumer(DownloadProgressButton b,AppInfo appInfo){

            this.btn = b;
            this.mAppInfo = appInfo;
        }


        @Override
        public void accept(@NonNull DownloadEvent event) throws Exception {

            int flag = event.getFlag();

            btn.setTag(R.id.tag_apk_flag,flag);

            bindClick(btn,mAppInfo);

            switch (flag){

                case DownloadFlag.INSTALLED:
                    btn.setText("运行");
                    break;


                case DownloadFlag.NORMAL:
                    btn.download();
                    break;


                case DownloadFlag.STARTED:
                    btn.setProgress((int) event.getDownloadStatus().getPercentNumber());
                    break;

                case DownloadFlag.PAUSED:
                    btn.setProgress((int) event.getDownloadStatus().getPercentNumber());
                    btn.paused();
                    break;


                case DownloadFlag.COMPLETED: //已完成
                    btn.setText("安装");
                    installApp(btn.getContext(),mAppInfo);
                    break;
                case DownloadFlag.FAILED://下载失败
                    btn.setText("失败");
                    break;
                case DownloadFlag.DELETED: //已删除

                    break;


            }


        }
    }


    interface  Api{

        @GET("download/{id}")
        Observable<BaseBean<AppDownloadInfo>> getAppDownloadInfo(@Path("id") int id);
    }
}
