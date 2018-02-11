package com.qinlong275.android.cniaoplay.data;

import com.qinlong275.android.cniaoplay.bean.AppInfo;
import com.qinlong275.android.cniaoplay.bean.BaseBean;
import com.qinlong275.android.cniaoplay.bean.IndexBean;
import com.qinlong275.android.cniaoplay.bean.PageBean;
import com.qinlong275.android.cniaoplay.data.http.ApiService;

import retrofit2.Callback;
import rx.Observable;

/**
 * Created by 秦龙 on 2018/2/5.
 */

public class AppInfoModel {

    private ApiService mApiService;

    public AppInfoModel(ApiService apiService) {
        mApiService = apiService;
    }

    public Observable<BaseBean<IndexBean>> index(){
        return mApiService.index();
    }

    public  Observable<BaseBean<PageBean<AppInfo>>> topList(int page){

        return  mApiService.topList(page);
    }

    public  Observable<BaseBean<PageBean<AppInfo>>> games(int page){

        return  mApiService.games(page);
    }
}
