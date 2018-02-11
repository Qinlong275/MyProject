package com.qinlong275.android.cniaoplay.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 秦龙 on 2018/2/7.
 */

public class GuideFragmentAdapter extends FragmentPagerAdapter{

    List<Fragment> mFragments;

    public GuideFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setFragments(List<Fragment> fragments){
        if (mFragments==null){
            mFragments=new ArrayList<>();
        }
        mFragments=fragments;

    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
