package com.qinlong275.android.cniaoplay.ui.fragment;


import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qinlong275.android.cniaoplay.bean.AppInfo;
import com.qinlong275.android.cniaoplay.common.Constant;
import com.qinlong275.android.cniaoplay.common.util.ACache;
import com.qinlong275.android.cniaoplay.presenter.AppinfoPresenter;
import com.qinlong275.android.cniaoplay.ui.adapter.AppInfoAdapter;

import java.util.List;

/**
 * Created by Ivan on 16/9/26.
 */

public class HotAppFragment extends BaseAppinfoFragment {


    @Override
    public void init() {
        initRecyclerView();

        String json = ACache.get(getActivity()).getAsString(Constant.HOT_APP);


        if (!TextUtils.isEmpty(json)) {

            Gson gson = new Gson();
            List<AppInfo> apps = gson.fromJson(json, new TypeToken<List<AppInfo>>() {
            }.getType());
            mAppInfoAdapter.addData(apps);
        }

    }

    @Override
    int type() {
        return 0;
    }

    @Override
    AppInfoAdapter buideAdapter() {
        return AppInfoAdapter.builder().showPosition(false).showBrief(false).showCategoryName(true).rxDownload(mRxDownload).build();
    }
}
