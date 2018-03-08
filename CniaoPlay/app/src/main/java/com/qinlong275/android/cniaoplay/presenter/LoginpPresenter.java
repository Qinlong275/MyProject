package com.qinlong275.android.cniaoplay.presenter;


import com.qinlong275.android.cniaoplay.bean.LoginBean;
import com.qinlong275.android.cniaoplay.common.Constant;
import com.qinlong275.android.cniaoplay.common.rx.RxBus;
import com.qinlong275.android.cniaoplay.common.rx.RxHttpResponseCompat;
import com.qinlong275.android.cniaoplay.common.rx.subscriber.ErrorHandleSubscriber;
import com.qinlong275.android.cniaoplay.common.util.ACache;
import com.qinlong275.android.cniaoplay.common.util.VerificationUtils;
import com.qinlong275.android.cniaoplay.presenter.contract.LoginContract;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * Created by 秦龙 on 2018/2/12.
 */

public class LoginpPresenter extends BasePresenter<LoginContract.ILoginModel,LoginContract.LoginView>{

    @Inject
    public LoginpPresenter(LoginContract.ILoginModel iLoginModel, LoginContract.LoginView loginView) {
        super(iLoginModel, loginView);
    }

    public void login(String phone,String pwd){
        if (!VerificationUtils.matcherPhoneNum(phone)){
            mView.checkPhoneError();
            return;
        }else {
            mView.checkPhoneSuccess();
        }

        mModel.login(phone,pwd).compose(RxHttpResponseCompat.<LoginBean>compatResult())
            .subscribe(new ErrorHandleSubscriber<LoginBean>(mContext) {
                @Override
                public void onSubscribe(Disposable d) {
                    mView.showLoading();
                }

                @Override
                public void onComplete() {
                    mView.dismissLoading();
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    mView.dismissLoading();
                }

                @Override
                public void onNext(LoginBean loginBean) {
                    mView.loginSuccess(loginBean);
                    saveuser(loginBean);
                    RxBus.getDefault().post(loginBean.getUser());//发送数据
                }
            });
    }

    private void saveuser(LoginBean bean){
        //保存token用于时间验证是否失效
        ACache aCache=ACache.get(mContext);
        aCache.put(Constant.TOKEN,bean.getToken());
        aCache.put(Constant.USER,bean.getUser());
    }
}
