package com.qinlong275.android.cniaoplay.common.rx.subscriber;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.JsonParseException;
import com.qinlong275.android.cniaoplay.common.exception.ApiException;
import com.qinlong275.android.cniaoplay.common.exception.BaseException;
import com.qinlong275.android.cniaoplay.common.exception.ErrorMessageFactory;
import com.qinlong275.android.cniaoplay.common.rx.RxErrorHandler;
import com.qinlong275.android.cniaoplay.ui.activity.LoginActivity;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import io.reactivex.disposables.Disposable;


/**
 * Created by 秦龙 on 2018/2/9.
 */

public abstract class ErrorHandleSubscriber<T> extends DefaultSubscriber<T> {

    protected RxErrorHandler mRxErrorHandler;

    protected Context mContext;
    public ErrorHandleSubscriber(Context context) {
        mRxErrorHandler = new RxErrorHandler(context);
        mContext=context;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onError(Throwable e) {
        BaseException baseException =  mRxErrorHandler.handleError(e);

        if(baseException==null){
            e.printStackTrace();
            Log.d("ErrorHandlerSubscriber",e.getMessage());
        }
        else {

            mRxErrorHandler.showErrorMessage(baseException);
            if(baseException.getCode() == BaseException.ERROR_TOKEN){
                toLogin();
            }

        }
    }

    private void toLogin() {

        Intent intent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(intent);
    }

}
