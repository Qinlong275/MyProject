package com.qinlong275.android.cniaoplay.ui;

/**
 * Created by 秦龙 on 2018/2/5.
 */

public interface BaseView {
    void showLoading();
    void dismissLoading();
    void showError(String msg);
}
