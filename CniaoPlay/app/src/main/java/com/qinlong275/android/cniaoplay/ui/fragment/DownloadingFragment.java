package com.qinlong275.android.cniaoplay.ui.fragment;

import android.support.v7.widget.RecyclerView;


import com.qinlong275.android.cniaoplay.di.component.AppComponent;
import com.qinlong275.android.cniaoplay.di.component.DaggerAppManagerComponent;
import com.qinlong275.android.cniaoplay.di.module.AppManagerModule;
import com.qinlong275.android.cniaoplay.ui.adapter.DownloadingAdapter;

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

public class DownloadingFragment extends AppManangerFragment{



    private DownloadingAdapter mAdapter;

    @Override
    public void init() {
        super.init();


        mPresenter.getDownlodingApps();
    }

    @Override
    protected RecyclerView.Adapter setupAdapter() {
        mAdapter = new DownloadingAdapter(mPresenter.geRxDowanload());
        return mAdapter;
    }


    @Override
    void onEmptyClick() {

    }

    @Override
    public void showDownloading(List<DownloadRecord> downloadRecords) {

        mAdapter.addData(downloadRecords);
    }
}
