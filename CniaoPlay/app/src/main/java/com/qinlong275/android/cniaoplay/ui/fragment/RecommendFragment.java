package com.qinlong275.android.cniaoplay.ui.fragment;

import android.app.ProgressDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.qinlong275.android.cniaoplay.R;


import com.qinlong275.android.cniaoplay.bean.IndexBean;
import com.qinlong275.android.cniaoplay.di.component.AppComponent;
import com.qinlong275.android.cniaoplay.di.component.DaggerRecommendCompoent;
import com.qinlong275.android.cniaoplay.di.module.RecommendModule;
import com.qinlong275.android.cniaoplay.presenter.RecommendPresenter;
import com.qinlong275.android.cniaoplay.presenter.contract.AppinfoContract;
import com.qinlong275.android.cniaoplay.ui.adapter.IndexMultilAdapter;

import javax.inject.Inject;

import butterknife.BindView;


/**
 * Created by Ivan on 16/9/26.
 */

public class RecommendFragment extends ProgressFragment<RecommendPresenter> implements AppinfoContract.View{

    IndexMultilAdapter mAdapter;

    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;

    @Inject
    ProgressDialog mProgressDialog;

    @Override
    public int setLayout() {
        return R.layout.template_recycleview;
    }

    @Override
    public void setupAcitivtyComponent(AppComponent appComponent) {
        DaggerRecommendCompoent.builder().appComponent(appComponent)
                .recommendModule(new RecommendModule(this)).build().inject(this);
    }

    @Override
    public void init() {
        initRecyclrView();
        mPresenter.requestDatas();
//        mPresenter.requestPermission();
    }


    public void initRecyclrView(){
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onEmptyClick() {
        mPresenter.requestDatas();
    }

    @Override
    public void showResult(IndexBean indexBean) {
        mAdapter=new IndexMultilAdapter(getActivity());
        mAdapter.setData(indexBean);
        mRecycleView.setAdapter(mAdapter);
    }


    @Override
    public void onRequestPermissionError() {
        Toast.makeText(getActivity(),"你已拒绝授权",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionSuccess() {
        mPresenter.requestDatas();
    }

}
