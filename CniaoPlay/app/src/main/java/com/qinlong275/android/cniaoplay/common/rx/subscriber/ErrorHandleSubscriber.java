package com.qinlong275.android.cniaoplay.common.rx.subscriber;

import android.content.Context;

import com.google.gson.JsonParseException;
import com.qinlong275.android.cniaoplay.common.exception.ApiException;
import com.qinlong275.android.cniaoplay.common.exception.BaseException;
import com.qinlong275.android.cniaoplay.common.exception.ErrorMessageFactory;
import com.qinlong275.android.cniaoplay.common.rx.RxErrorHandler;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by 秦龙 on 2018/2/9.
 */

public abstract class ErrorHandleSubscriber<T> extends DefaultSubscriber<T> {

    protected RxErrorHandler mRxErrorHandler;

    public ErrorHandleSubscriber(Context context) {
        mRxErrorHandler = new RxErrorHandler(context);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        BaseException baseException=mRxErrorHandler.handleError(e);
        mRxErrorHandler.showErrorMessage(baseException);
    }
}
