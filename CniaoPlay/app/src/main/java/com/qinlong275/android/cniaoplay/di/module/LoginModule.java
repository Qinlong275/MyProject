package com.qinlong275.android.cniaoplay.di.module;



import com.qinlong275.android.cniaoplay.data.LoginModel;
import com.qinlong275.android.cniaoplay.data.http.ApiService;
import com.qinlong275.android.cniaoplay.presenter.contract.LoginContract;

import dagger.Module;
import dagger.Provides;

/**
 * 菜鸟窝http://www.cniao5.com 一个高端的互联网技能学习平台
 *
 * @author Ivan
 * @version V1.0
 * @Package com.cniao5.cniao5play.di
 * @Description: ${TODO}(用一句话描述该文件做什么)
 * @date
 */




@Module
public class LoginModule {



    private LoginContract.LoginView mView;

    public LoginModule(LoginContract.LoginView view){


        this.mView = view;
    }





    @Provides
    public LoginContract.LoginView provideView(){

        return mView;
    }



    @Provides
    public LoginContract.ILoginModel privodeModel(ApiService apiService){

        return  new LoginModel(apiService);
    }


}
