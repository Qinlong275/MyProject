package com.qinlong275.android.cniaoplay.ui.fragment;


import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;


import com.qinlong275.android.cniaoplay.common.apkparset.AndroidApk;
import com.qinlong275.android.cniaoplay.di.component.AppComponent;
import com.qinlong275.android.cniaoplay.di.component.DaggerAppManagerComponent;
import com.qinlong275.android.cniaoplay.di.module.AppManagerModule;
import com.qinlong275.android.cniaoplay.ui.adapter.AndroidApkAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class InstalledAppAppFragment extends AppManangerFragment {


    private AndroidApkAdapter mAdapter;

    @Override
    public void init() {
        super.init();


        mPresenter.getInstalledApps();
    }
    @Override
    protected RecyclerView.Adapter setupAdapter() {

        mAdapter = new AndroidApkAdapter(AndroidApkAdapter.FLAG_APP);

        return mAdapter;
    }

    @Override
    void onEmptyClick() {
        mPresenter.getInstalledApps();
    }

    @Override
    public void setupAcitivtyComponent(AppComponent appComponent) {

        DaggerAppManagerComponent.builder().appManagerModule(new AppManagerModule(this))
                .appComponent(appComponent).build().inject(this);



    }


    @Override
    public void showApps(List<AndroidApk> apps) {
        mAdapter.addData(apps);
    }
}
