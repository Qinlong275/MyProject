package com.qinlong275.android.cniaoplay.data;

import com.qinlong275.android.cniaoplay.bean.BaseBean;
import com.qinlong275.android.cniaoplay.bean.LoginBean;
import com.qinlong275.android.cniaoplay.bean.RequestBean.LoginRequestBean;
import com.qinlong275.android.cniaoplay.data.http.ApiService;
import com.qinlong275.android.cniaoplay.presenter.contract.LoginContract;

import io.reactivex.Observable;


/**
 * Created by 秦龙 on 2018/2/12.
 */

public class LoginModel implements LoginContract.ILoginModel{

    private ApiService mApiService;

    public LoginModel(ApiService apiService) {
        mApiService = apiService;
    }


    @Override
    public Observable<BaseBean<LoginBean>> login(String phone, String pwd) {
        LoginRequestBean params=new LoginRequestBean();
        params.setEmail(phone);
        params.setPassword(pwd);
        return mApiService.login(params);
    }
}

