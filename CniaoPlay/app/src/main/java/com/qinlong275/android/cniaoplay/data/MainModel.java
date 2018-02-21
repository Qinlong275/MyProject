package com.qinlong275.android.cniaoplay.data;

import com.qinlong275.android.cniaoplay.bean.AppInfo;
import com.qinlong275.android.cniaoplay.bean.BaseBean;
import com.qinlong275.android.cniaoplay.bean.RequestBean.AppsUpdateBean;
import com.qinlong275.android.cniaoplay.data.http.ApiService;
import com.qinlong275.android.cniaoplay.presenter.contract.MainContract;

import java.util.List;

import io.reactivex.Observable;

/**
 * 菜鸟窝http://www.cniao5.com 一个高端的互联网技能学习平台
 *
 * @author Ivan
 * @version V1.0
 * @Package com.cniao5.cniao5market.data.model
 * @Description: ${TODO}(用一句话描述该文件做什么)
 * @date
 */

public class MainModel implements MainContract.IMainModel {

    private ApiService mApiService;

    public MainModel(ApiService apiService){
        this.mApiService = apiService;
    }


    @Override
    public Observable<BaseBean<List<AppInfo>>> getUpdateApps(AppsUpdateBean param) {
        return mApiService.getAppsUpdateinfo(param.getPackageName(),param.getVersionCode());
    }
}
