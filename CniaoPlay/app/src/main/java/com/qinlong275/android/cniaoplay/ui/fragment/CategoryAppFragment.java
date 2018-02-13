package com.qinlong275.android.cniaoplay.ui.fragment;

import android.annotation.SuppressLint;

import com.qinlong275.android.cniaoplay.di.component.AppComponent;
import com.qinlong275.android.cniaoplay.di.component.DaggerAppinfoComponent;
import com.qinlong275.android.cniaoplay.di.module.AppinfoModule;
import com.qinlong275.android.cniaoplay.ui.adapter.AppInfoAdapter;

/**
 * Created by 秦龙 on 2018/2/12.
 */


@SuppressLint("ValidFragment")
public class CategoryAppFragment extends BaseAppinfoFragment{


    private int categoryId;
    private int mFlagType;


    public CategoryAppFragment(int categoryId, int flagType){
        this.categoryId = categoryId;
        this.mFlagType = flagType;
    }

    @Override
    public void init() {
        mPresenter.requestCategoryApps(categoryId,page,mFlagType);
        initRecyclerView();
    }

    @Override
    int type() {
        return 0;
    }

    @Override
    AppInfoAdapter buideAdapter() {
        return  AppInfoAdapter.builder().showPosition(false).showBrief(true).showCategoryName(false).build();
    }

    @Override
    public void setupAcitivtyComponent(AppComponent appComponent) {
        DaggerAppinfoComponent.builder().appComponent(appComponent).appinfoModule(new AppinfoModule(this))
                .build().injectCategoryAppFragment(this);

    }
}
