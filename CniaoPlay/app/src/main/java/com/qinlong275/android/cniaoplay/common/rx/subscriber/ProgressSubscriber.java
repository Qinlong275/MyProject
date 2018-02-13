package com.qinlong275.android.cniaoplay.common.rx.subscriber;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.qinlong275.android.cniaoplay.common.exception.BaseException;
import com.qinlong275.android.cniaoplay.common.util.ProgressDialogHandler;
import com.qinlong275.android.cniaoplay.ui.BaseView;

/**
 * Created by 秦龙 on 2018/2/10.
 */

public abstract class ProgressSubscriber <T> extends ErrorHandleSubscriber<T> implements ProgressDialogHandler.OnProgressCancelListener{

    private BaseView mBaseView;

    public ProgressSubscriber(Context context, BaseView baseView) {
        super(context);
        mBaseView=baseView;
    }

    @Override
    public void onStart() {
        if(isShowProgress()){
            mBaseView.showLoading();
        }
    }

    private boolean isShowProgress() {
        return true;
    }

    @Override
    public void onCompleted() {
       mBaseView.dismissLoading();
    }

    @Override
    public void onError(Throwable e) {
        BaseException baseException=mRxErrorHandler.handleError(e);
        mBaseView.showError(baseException.getDisplayMessage());
        e.printStackTrace();
    }

    @Override
    public void onCancelProgress() {
        unsubscribe();
    }

}
