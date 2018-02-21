package com.qinlong275.android.cniaoplay.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.qinlong275.android.cniaoplay.ui.bean.FragmentInfo;
import com.qinlong275.android.cniaoplay.ui.fragment.CategoryFragment;
import com.qinlong275.android.cniaoplay.ui.fragment.GamesFragment;
import com.qinlong275.android.cniaoplay.ui.fragment.ToplistFragment;
import com.qinlong275.android.cniaoplay.ui.fragment.RecommendFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 秦龙 on 2018/2/1.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<FragmentInfo> mFragments ;

    public ViewPagerAdapter(FragmentManager fm,List<FragmentInfo> fragments) {
        super(fm);
//        initFragments();
        mFragments=fragments;
    }

    private void initFragments() {
        mFragments.add(new FragmentInfo("推荐",RecommendFragment.class));
        mFragments.add(new FragmentInfo("排行",ToplistFragment.class));
        mFragments.add(new FragmentInfo("游戏",GamesFragment.class));
        mFragments.add(new FragmentInfo("分类",CategoryFragment.class));
    }

    @Override
    public Fragment getItem(int position) {
        try {
            return (Fragment) mFragments.get(position).getFragment().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return  null;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragments.get(position).getTitle();
    }
}
