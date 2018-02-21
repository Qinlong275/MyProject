package com.qinlong275.android.cniaoplay.ui.fragment;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qinlong275.android.cniaoplay.bean.AppInfo;
import com.qinlong275.android.cniaoplay.common.Constant;
import com.qinlong275.android.cniaoplay.common.util.ACache;
import com.qinlong275.android.cniaoplay.ui.adapter.AppInfoAdapter;

import java.util.List;

/**
 * Created by 秦龙 on 2018/2/21.
 */

public class HotGameFragment extends BaseAppinfoFragment{

    @Override
    public void init() {
        initRecyclerView();

        String json = ACache.get(getActivity()).getAsString(Constant.HOT_GAME);


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
        return AppInfoAdapter.builder().showPosition(true).showBrief(false).showCategoryName(true).rxDownload(mRxDownload).build();
    }
}
