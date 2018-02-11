package com.qinlong275.android.cniaoplay.presenter.contract;

import com.qinlong275.android.cniaoplay.bean.AppInfo;
import com.qinlong275.android.cniaoplay.bean.IndexBean;
import com.qinlong275.android.cniaoplay.bean.PageBean;
import com.qinlong275.android.cniaoplay.presenter.BasePresenter;
import com.qinlong275.android.cniaoplay.ui.BaseView;

import java.util.List;

/**
 * Created by 秦龙 on 2018/2/5.
 */

public interface AppinfoContract {

    interface View extends BaseView{

        void showResult(IndexBean indexBean);

        void onRequestPermissionError();
        void onRequestPermissionSuccess();
    }

    interface AppinfoView extends BaseView{

        void showResult(PageBean<AppInfo> appInfoPageBean);

        void onLoadComplete();
    }
}
