package com.qinlong275.android.cniaoplay.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.qinlong275.android.cniaoplay.ui.bean.FragmentInfo;
import com.qinlong275.android.cniaoplay.ui.fragment.CategoryAppFragment;
import com.qinlong275.android.cniaoplay.ui.fragment.CategoryFragment;
import com.qinlong275.android.cniaoplay.ui.fragment.GamesFragment;
import com.qinlong275.android.cniaoplay.ui.fragment.RecommendFragment;
import com.qinlong275.android.cniaoplay.ui.fragment.ToplistFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 秦龙 on 2018/2/1.
 */

public class CategoryAppPagerAdapter extends FragmentStatePagerAdapter {

    private List<String> titles = new ArrayList<>(3);

    private int mCategoryId;
    public CategoryAppPagerAdapter(FragmentManager fm,int categoryId) {
        super(fm);
        mCategoryId=categoryId;
        titles.add("精品");
        titles.add("排行");
        titles.add("新品");
    }

    @Override
    public Fragment getItem(int position) {
        return new CategoryAppFragment(mCategoryId,position);
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
