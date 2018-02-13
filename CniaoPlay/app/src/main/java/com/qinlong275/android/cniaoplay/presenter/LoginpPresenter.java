package com.qinlong275.android.cniaoplay.presenter;

import com.hwangjr.rxbus.RxBus;
import com.qinlong275.android.cniaoplay.bean.LoginBean;
import com.qinlong275.android.cniaoplay.common.Constant;
import com.qinlong275.android.cniaoplay.common.rx.RxHttpResponseCompat;
import com.qinlong275.android.cniaoplay.common.rx.subscriber.ErrorHandleSubscriber;
import com.qinlong275.android.cniaoplay.common.util.ACache;
import com.qinlong275.android.cniaoplay.common.util.VerificationUtils;
import com.qinlong275.android.cniaoplay.presenter.contract.LoginContract;

import javax.inject.Inject;

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
                public void onStart() {
                    mView.showLoading();
                }

                @Override
                public void onCompleted() {
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
                    RxBus.get().post(loginBean.getUser());
                }
            });
    }

    private void saveuser(LoginBean bean){
        ACache aCache=ACache.get(mContext);
        aCache.put(Constant.TOKEN,bean.getToken());
        aCache.put(Constant.USER,bean.getUser());
    }
}
