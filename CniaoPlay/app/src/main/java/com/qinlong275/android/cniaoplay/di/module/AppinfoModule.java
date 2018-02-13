package com.qinlong275.android.cniaoplay.di.module;

import android.app.ProgressDialog;

import com.qinlong275.android.cniaoplay.data.AppInfoModel;
import com.qinlong275.android.cniaoplay.data.http.ApiService;
import com.qinlong275.android.cniaoplay.presenter.contract.AppinfoContract;
import com.qinlong275.android.cniaoplay.ui.adapter.AppInfoAdapter;
import com.qinlong275.android.cniaoplay.ui.adapter.RecommendAppAdapter;
import com.qinlong275.android.cniaoplay.ui.fragment.RecommendFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 秦龙 on 2018/2/11.
 */

@Module(includes = AppModelModule.class)
public class AppinfoModule {
    private AppinfoContract.AppinfoView mView;

    public AppinfoModule(AppinfoContract.AppinfoView view) {
        mView = view;
    }

    @Provides
    public AppinfoContract.AppinfoView provideView(){
        return mView;
    }


}
