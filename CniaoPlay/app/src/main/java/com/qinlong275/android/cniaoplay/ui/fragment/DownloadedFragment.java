package com.qinlong275.android.cniaoplay.ui.fragment;

import android.support.v7.widget.RecyclerView;


import com.qinlong275.android.cniaoplay.common.apkparset.AndroidApk;
import com.qinlong275.android.cniaoplay.di.component.AppComponent;
import com.qinlong275.android.cniaoplay.di.component.DaggerAppManagerComponent;
import com.qinlong275.android.cniaoplay.di.module.AppManagerModule;
import com.qinlong275.android.cniaoplay.ui.adapter.AndroidApkAdapter;

import java.util.List;

import zlc.season.rxdownload2.entity.DownloadRecord;

/**
 * 菜鸟窝http://www.cniao5.com 一个高端的互联网技能学习平台
 *
 * @author Ivan
 * @version V1.0
 * @Package com.cniao5.cniao5play.ui.fragment
 * @Description: ${TODO}(用一句话描述该文件做什么)
 * @date
 */

public class DownloadedFragment extends AppManangerFragment{

    AndroidApkAdapter mAdapter;

    @Override
    void onEmptyClick() {
        mPresenter.getLocalApks();
    }


    @Override
    public void init() {
        super.init();


        mPresenter.getLocalApks();
    }

    @Override
    protected RecyclerView.Adapter setupAdapter() {
        mAdapter = new AndroidApkAdapter(AndroidApkAdapter.FLAG_APK);

        return mAdapter;
    }


    @Override
    public void showApps(List<AndroidApk> apps) {
        mAdapter.addData(apps);
    }
}
