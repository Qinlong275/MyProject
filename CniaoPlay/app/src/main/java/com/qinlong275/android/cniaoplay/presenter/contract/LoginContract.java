package com.qinlong275.android.cniaoplay.presenter.contract;


import com.qinlong275.android.cniaoplay.bean.BaseBean;
import com.qinlong275.android.cniaoplay.bean.LoginBean;
import com.qinlong275.android.cniaoplay.ui.BaseView;

import io.reactivex.Observable;


/**
 * 菜鸟窝http://www.cniao5.com 一个高端的互联网技能学习平台
 *
 * @author Ivan
 * @version V1.0
 * @Package com.cniao5.cniao5play.presenter.contract
 * @Description: ${TODO}(用一句话描述该文件做什么)
 * @date
 */

public interface LoginContract {


    public interface  ILoginModel{

        Observable<BaseBean<LoginBean>> login(String phone, String pwd);

    }


    public interface  LoginView extends BaseView {


        void checkPhoneError();
        void checkPhoneSuccess();

        void loginSuccess(LoginBean bean);
//        void loginError(String msg);

    }
}
