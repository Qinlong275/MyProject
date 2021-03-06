package com.qinlong275.android.cniaoplay.common.rx.subscriber;

import android.app.ProgressDialog;
import android.content.Context;

import com.qinlong275.android.cniaoplay.common.rx.RxErrorHandler;
import com.qinlong275.android.cniaoplay.common.util.ProgressDialogHandler;

import io.reactivex.disposables.Disposable;

/**
 * Created by 秦龙 on 2018/2/9.
 */

public abstract class ProgressDialogSubscriber<T> extends ErrorHandleSubscriber<T> implements ProgressDialogHandler.OnProgressCancelListener{

    Context mContext;

    private Disposable mDisposable;

    private ProgressDialog mProgressDialog;

    public ProgressDialogSubscriber(Context context) {
        super(context);
        mContext = context;
    }

    protected boolean isShowProgressDialog(){
        return  true;
    }

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable=d;
        if(isShowProgressDialog()){
            showProgressDialog();
        }
    }

    @Override
    public void onComplete() {
        if(isShowProgressDialog()){
           dismissprogressDialog();
        }
    }

    private void initProgressDialog(){
        if (mProgressDialog==null){
            mProgressDialog=new ProgressDialog(mContext);
            mProgressDialog.setMessage("loading...");
        }

    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        if(isShowProgressDialog()){
            dismissprogressDialog();
        }

    }

    @Override
    public void onCancelProgress() {
        mDisposable.dispose();
    }

    private void showProgressDialog(){
        initProgressDialog();
        mProgressDialog.show();
    }

    private void dismissprogressDialog(){
        if (mProgressDialog!=null&&mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }
}
