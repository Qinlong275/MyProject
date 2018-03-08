package com.qinlong275.android.cniaoplay.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.qinlong275.android.cniaoplay.R;
import com.qinlong275.android.cniaoplay.bean.AppInfo;
import com.qinlong275.android.cniaoplay.common.Constant;
import com.qinlong275.android.cniaoplay.common.imageloader.ImageLoader;
import com.qinlong275.android.cniaoplay.common.util.DateUtils;
import com.qinlong275.android.cniaoplay.di.component.AppComponent;

import com.qinlong275.android.cniaoplay.di.component.DaggerAppDetailComponent;
import com.qinlong275.android.cniaoplay.di.module.AppModelModule;
import com.qinlong275.android.cniaoplay.di.module.AppdetailModule;
import com.qinlong275.android.cniaoplay.presenter.AppDetaiPresenter;
import com.qinlong275.android.cniaoplay.presenter.contract.AppinfoContract;
import com.qinlong275.android.cniaoplay.ui.activity.AppDetailActivity;
import com.qinlong275.android.cniaoplay.ui.adapter.AppInfoAdapter;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 菜鸟窝http://www.cniao5.com 一个高端的互联网技能学习平台
 *
 * @author Ivan
 * @version V1.0
 * @Package com.cniao5.cniao5play.ui.fragment
 * @Description: ${TODO}(用一句话描述该文件做什么)
 * @date
 */

@SuppressLint("ValidFragment")
public class AppDetailFragment extends ProgressFragment<AppDetaiPresenter> implements AppinfoContract.AppDetailview {
    @BindView(R.id.view_gallery)
    LinearLayout mViewGallery;

    @BindView(R.id.view_introduction)
    ExpandableTextView mViewIntroduction;
    @BindView(R.id.txt_update_time)
    TextView mTxtUpdateTime;
    @BindView(R.id.txt_version)
    TextView mTxtVersion;
    @BindView(R.id.txt_apk_size)
    TextView mTxtApkSize;
    @BindView(R.id.txt_publisher)
    TextView mTxtPublisher;
    @BindView(R.id.txt_publisher2)
    TextView mTxtPublisher2;
    @BindView(R.id.recycler_view_same_dev)
    RecyclerView mRecyclerViewSameDev;
    @BindView(R.id.recycler_view_relate)
    RecyclerView mRecyclerViewRelate;
    Unbinder unbinder;

    private int mAppId;
    private LayoutInflater mInflater;
    private AppInfoAdapter mAdapter;

    public AppDetailFragment(int appId) {
        mAppId = appId;
    }


    @Override
    public void showAppDetail(AppInfo appInfo) {
        showScreenshot(appInfo.getScreenshot());
        mViewIntroduction.setText(appInfo.getIntroduction());



        mTxtUpdateTime.setText(DateUtils.formatDate(appInfo.getUpdateTime()));
        mTxtApkSize.setText((appInfo.getApkSize() / 1014 / 1024) + " Mb");
        mTxtVersion.setText(appInfo.getVersionName());
        mTxtPublisher.setText(appInfo.getPublisherName());
        mTxtPublisher2.setText(appInfo.getPublisherName());



        mAdapter = AppInfoAdapter.builder().layout(R.layout.template_appinfo2)
                .build();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        mRecyclerViewSameDev.setLayoutManager(layoutManager);


        mAdapter.addData(appInfo.getSameDevAppInfoList());
        mRecyclerViewSameDev.setAdapter(mAdapter);

        mRecyclerViewSameDev.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                AppInfo appInfo=mAdapter.getItem(position);
                mAppAplication.setView(view);
                Intent intent=new Intent(getActivity(), AppDetailActivity.class);
                //要实现serializable接口的
                intent.putExtra("appinfo",appInfo);
                startActivity(intent);
            }
        });

        /////////////////////////////////////////////

        mAdapter = AppInfoAdapter.builder().layout(R.layout.template_appinfo2)
                .build();

        mRecyclerViewRelate.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        mAdapter.addData(appInfo.getRelateAppInfoList());
        mRecyclerViewRelate.setAdapter(mAdapter);

        mRecyclerViewRelate.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                AppInfo appInfo=mAdapter.getItem(position);
                mAppAplication.setView(view);
                Intent intent=new Intent(getActivity(), AppDetailActivity.class);
                //要实现serializable接口的
                intent.putExtra("appinfo",appInfo);
                startActivity(intent);
            }
        });

    }

    @Override
    void onEmptyClick() {
        mPresenter.getAppDeail(mAppId);
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_app_detail;
    }

    @Override
    public void setupAcitivtyComponent(AppComponent appComponent) {
        DaggerAppDetailComponent.builder().appComponent(appComponent)
                .appdetailModule(new AppdetailModule(this))
                .appModelModule(new AppModelModule())
                .build().inject(this);
    }

    @Override
    public void init() {
        mInflater = LayoutInflater.from(getActivity());
        mPresenter.getAppDeail(mAppId);
    }


    private void showScreenshot(String screentShot) {


        List<String> urls = Arrays.asList(screentShot.split(","));

        for (String url : urls) {

            ImageView imageView = (ImageView) mInflater.inflate(R.layout.template_imageview, mViewGallery, false);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(480,640);
            imageView.setLayoutParams(layoutParams);

            ImageLoader.load(Constant.BASE_IMG_URL + url, imageView);

            mViewGallery.addView(imageView);

        }

    }
}
