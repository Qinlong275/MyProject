package com.qinlong275.android.cniaoplay.di.module;

import android.app.ProgressDialog;

import com.qinlong275.android.cniaoplay.data.AppInfoModel;
import com.qinlong275.android.cniaoplay.data.http.ApiService;
import com.qinlong275.android.cniaoplay.presenter.contract.AppinfoContract;
import com.qinlong275.android.cniaoplay.ui.adapter.RecommendAppAdapter;
import com.qinlong275.android.cniaoplay.ui.fragment.RecommendFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 秦龙 on 2018/2/6.
 */

@Module
public class RecommendModule {

    private AppinfoContract.View mView;

    public RecommendModule(AppinfoContract.View view) {
        mView = view;
    }

    @Provides
    public AppinfoContract.View provideView(){
        return mView;
    }

    @Provides
    public RecommendAppAdapter provideAdapter(){
//        return new RecommendAppAdapter()
        return null;
    }

    @Provides
    public ProgressDialog provideProgressdialog(AppinfoContract.View view){
        return new ProgressDialog(((RecommendFragment)view).getActivity());
    }

    @Provides
    public AppInfoModel provideMode(ApiService apiService){
        return new AppInfoModel(apiService);
    }
}
