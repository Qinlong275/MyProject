package com.qinlong275.android.cniaoplay.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;
import com.qinlong275.android.cniaoplay.R;
import com.qinlong275.android.cniaoplay.common.Constant;
import com.qinlong275.android.cniaoplay.di.component.AppComponent;
import com.qinlong275.android.cniaoplay.ui.adapter.ViewPagerAdapter;
import com.qinlong275.android.cniaoplay.ui.bean.FragmentInfo;
import com.qinlong275.android.cniaoplay.ui.fragment.DownloadedFragment;
import com.qinlong275.android.cniaoplay.ui.fragment.DownloadingFragment;
import com.qinlong275.android.cniaoplay.ui.fragment.InstalledAppAppFragment;
import com.qinlong275.android.cniaoplay.ui.fragment.UpgradeAppFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class AppMangerActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    private  int position;

    private void initToolbar() {


        mToolbar.setNavigationIcon(
                new IconicsDrawable(this)
                        .icon(Ionicons.Icon.ion_ios_arrow_back)
                        .sizeDp(16)
                        .color(getResources().getColor(R.color.md_white_1000)
                        )
        );

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mToolbar.setTitle(R.string.download_manager);
    }


    @Override
    public int setLayout() {
        return R.layout.activity_app_manger;
    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void init() {
        position = getIntent().getIntExtra(Constant.POSITION,0);
        initToolbar();
        initTablayout();

    }


    private void initTablayout() {




        PagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),initFragments());
        mViewPager.setOffscreenPageLimit(adapter.getCount());
        mViewPager.setAdapter(adapter);

        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.setCurrentItem(position);
        mTabLayout.getTabAt(position).select();
    }



    private List<FragmentInfo> initFragments(){

        List<FragmentInfo> mFragments = new ArrayList<>(4);

        mFragments.add(new FragmentInfo("下载",DownloadingFragment.class));
        mFragments.add(new FragmentInfo ("已完成", DownloadedFragment.class));
        mFragments.add(new FragmentInfo ("升级", UpgradeAppFragment.class));
        mFragments.add(new FragmentInfo ("已安装", InstalledAppAppFragment.class));

        return  mFragments;

    }
}
