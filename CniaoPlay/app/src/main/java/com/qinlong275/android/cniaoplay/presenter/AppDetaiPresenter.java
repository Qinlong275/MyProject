package com.qinlong275.android.cniaoplay.presenter;

import com.qinlong275.android.cniaoplay.bean.AppInfo;
import com.qinlong275.android.cniaoplay.common.rx.RxHttpResponseCompat;
import com.qinlong275.android.cniaoplay.common.rx.subscriber.ProgressSubscriber;
import com.qinlong275.android.cniaoplay.data.AppInfoModel;
import com.qinlong275.android.cniaoplay.presenter.contract.AppinfoContract;

import javax.inject.Inject;

/**
 * Created by 秦龙 on 2018/2/13.
 */

public class AppDetaiPresenter extends BasePresenter<AppInfoModel,AppinfoContract.AppDetailview>{

    @Inject
    public AppDetaiPresenter(AppInfoModel appInfoModel, AppinfoContract.AppDetailview appDetailview) {
        super(appInfoModel, appDetailview);
    }

    public void getAppDeail(int id){
        mModel.getAppDetail(id).compose(RxHttpResponseCompat.<AppInfo>compatResult())
                .subscribe(new ProgressSubscriber<AppInfo>(mContext,mView) {
                    @Override
                    public void onNext(AppInfo appInfo) {
                        mView.showAppDetail(appInfo);
                    }
                });
    }
}
