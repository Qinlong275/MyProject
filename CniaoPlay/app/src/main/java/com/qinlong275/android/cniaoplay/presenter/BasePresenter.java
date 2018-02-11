package com.qinlong275.android.cniaoplay.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.qinlong275.android.cniaoplay.ui.BaseView;

/**
 * Created by 秦龙 on 2018/2/5.
 */

public class BasePresenter<M,V extends BaseView> {

    protected M mModel;
    protected V mView;

    protected Context mContext;

    public BasePresenter(M m, V v) {
        this.mModel = m;
        this.mView = v;
        initContext();
    }


    private void initContext(){



        if(mView instanceof Fragment){
            mContext = ((Fragment)mView).getActivity();
        }
        else {
            mContext = (Activity) mView;
        }
    }
}
