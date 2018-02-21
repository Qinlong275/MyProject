package com.qinlong275.android.cniaoplay.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qinlong275.android.cniaoplay.R;
import com.qinlong275.android.cniaoplay.di.component.AppComponent;
import com.qinlong275.android.cniaoplay.di.component.DaggerAppinfoComponent;
import com.qinlong275.android.cniaoplay.di.module.AppinfoModule;
import com.qinlong275.android.cniaoplay.presenter.AppinfoPresenter;
import com.qinlong275.android.cniaoplay.ui.adapter.AppInfoAdapter;


/**
 * Created by Ivan on 16/9/26.
 */

public class GamesFragment extends BaseAppinfoFragment {

    @Override
    int type() {
        return AppinfoPresenter.GAME;
    }

    @Override
    AppInfoAdapter buideAdapter() {
        return  AppInfoAdapter.builder()
                .showPosition(false)
                .showBrief(true)
                .showCategoryName(true)
                .rxDownload(mRxDownload)
                .build();
    }

}
