package com.qinlong275.android.cniaoplay.di.module;

import com.qinlong275.android.cniaoplay.data.AppInfoModel;
import com.qinlong275.android.cniaoplay.data.http.ApiService;
import com.qinlong275.android.cniaoplay.presenter.contract.AppinfoContract;
import com.qinlong275.android.cniaoplay.ui.adapter.RecommendAppAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 秦龙 on 2018/2/11.
 */

@Module(includes = {AppModelModule.class})
public class AppdetailModule {
    private AppinfoContract.AppDetailview mView;

    public AppdetailModule(AppinfoContract.AppDetailview view) {
        mView = view;
    }

    @Provides
    public AppinfoContract.AppDetailview provideView(){
        return mView;
    }

}
