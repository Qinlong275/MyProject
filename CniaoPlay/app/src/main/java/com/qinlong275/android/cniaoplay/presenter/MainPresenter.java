package com.qinlong275.android.cniaoplay.presenter;



import com.qinlong275.android.cniaoplay.bean.AppInfo;
import com.qinlong275.android.cniaoplay.bean.RequestBean.AppsUpdateBean;
import com.qinlong275.android.cniaoplay.common.Constant;
import com.qinlong275.android.cniaoplay.common.apkparset.AndroidApk;
import com.qinlong275.android.cniaoplay.common.rx.RxHttpResponseCompat;
import com.qinlong275.android.cniaoplay.common.rx.subscriber.ProgressSubscriber;
import com.qinlong275.android.cniaoplay.common.util.ACache;
import com.qinlong275.android.cniaoplay.common.util.AppUtils;
import com.qinlong275.android.cniaoplay.common.util.JsonUtils;
import com.qinlong275.android.cniaoplay.common.util.PermissionUtil;
import com.qinlong275.android.cniaoplay.presenter.contract.MainContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static android.Manifest.permission.READ_PHONE_STATE;

/**
 * 菜鸟窝http://www.cniao5.com 一个高端的互联网技能学习平台
 *
 * @author Ivan
 * @version V1.0
 * @Package com.cniao5.cniao5market.presenter
 * @Description: ${TODO}(用一句话描述该文件做什么)
 * @date
 */

public class MainPresenter extends BasePresenter<MainContract.IMainModel,MainContract.MainView> {

    @Inject
    public MainPresenter(MainContract.IMainModel iMainModel, MainContract.MainView mainView) {
        super(iMainModel, mainView);
    }


    public  void requestPermisson(){


        PermissionUtil.requestPermisson(mContext,READ_PHONE_STATE).doOnNext(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {

                if(!aBoolean){
                    mView.requestPermissonFail();
                }

            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
               mView.requestPermissonSuccess();
            }
        });

    }




    public void getAppUpdateInfo(){


        getIntalledApps()
                .flatMap(new Function<AppsUpdateBean, ObservableSource<List<AppInfo>>>() {
            @Override
            public ObservableSource<List<AppInfo>> apply(@NonNull AppsUpdateBean params) throws Exception {

                 return  mModel.getUpdateApps(params).compose(RxHttpResponseCompat.<List<AppInfo>>compatResult());
            }
        }).subscribe(new ProgressSubscriber<List<AppInfo>>(mContext,mView) {
            @Override
            public void onNext(List<AppInfo> appInfos) {

                if(appInfos !=null){

                    ACache.get(mContext).put(Constant.APP_UPDATE_LIST, JsonUtils.toJson(appInfos));
                }

                mView.changeAppNeedUpdateCount(appInfos==null?0:appInfos.size());


            }
        });

    }





    private Observable<AppsUpdateBean> getIntalledApps(){


        return  Observable.create(new ObservableOnSubscribe<AppsUpdateBean>() {


            @Override
            public void subscribe(ObservableEmitter<AppsUpdateBean> e) throws Exception {

               e.onNext(buildParams(AppUtils.getInstalledApps(mContext)));
                e.onComplete();
            }
        });

    }





    private AppsUpdateBean buildParams(List<AndroidApk> apks){


        StringBuilder packageNameBuilder = new StringBuilder();
        StringBuilder versionCodeBuilder = new StringBuilder();

        for(AndroidApk apk :apks){

            if(!apk.isSystem()){

                packageNameBuilder.append(apk.getPackageName()).append(",");
                versionCodeBuilder.append(apk.getAppVersionCode()).append(",");
            }
        }

        AppsUpdateBean param = new AppsUpdateBean();
        param.setPackageName(packageNameBuilder.toString());
        param.setVersionCode(versionCodeBuilder.toString());

        return param;

    }


}
