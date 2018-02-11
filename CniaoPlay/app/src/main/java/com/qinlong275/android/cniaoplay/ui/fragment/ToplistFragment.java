package com.qinlong275.android.cniaoplay.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qinlong275.android.cniaoplay.R;
import com.qinlong275.android.cniaoplay.bean.AppInfo;
import com.qinlong275.android.cniaoplay.bean.PageBean;
import com.qinlong275.android.cniaoplay.di.component.AppComponent;
import com.qinlong275.android.cniaoplay.di.component.DaggerAppinfoComponent;
import com.qinlong275.android.cniaoplay.di.module.AppinfoModule;
import com.qinlong275.android.cniaoplay.presenter.AppinfoPresenter;
import com.qinlong275.android.cniaoplay.presenter.contract.AppinfoContract;
import com.qinlong275.android.cniaoplay.ui.adapter.AppInfoAdapter;
import com.qinlong275.android.cniaoplay.ui.decoration.DividerItemDecoration;

import butterknife.BindView;


/**
 * Created by Ivan on 16/9/26.
 */

public class ToplistFragment extends BaseAppinfoFragment{


    @Override
    public void setupAcitivtyComponent(AppComponent appComponent) {
        DaggerAppinfoComponent.builder().appComponent(appComponent).appinfoModule(new AppinfoModule(this))
                .build().injectToplistFragment(this);
    }

    @Override
    int type() {
        return AppinfoPresenter.TOP_LIST;
    }

    @Override
    AppInfoAdapter buideAdapter() {
        return  AppInfoAdapter.builder().showPosition(true).showBrief(false).showCategoryName(true).build();
    }

}
