package com.qinlong275.android.cniaoplay.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qinlong275.android.cniaoplay.AppAplication;
import com.qinlong275.android.cniaoplay.di.component.AppComponent;
import com.qinlong275.android.cniaoplay.presenter.BasePresenter;
import com.qinlong275.android.cniaoplay.ui.BaseView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 秦龙 on 2018/2/6.
 */

public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements BaseView{

    private Unbinder mUnbinder;

    private AppAplication mAppAplication;

    private View mRootView;


    @Inject
    T mPresenter ;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        mRootView = inflater.inflate(setLayout(), container, false);
        mUnbinder=  ButterKnife.bind(this, mRootView);



        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        this.mAppAplication = (AppAplication) getActivity().getApplication();
        setupAcitivtyComponent(mAppAplication.getAppComponent());

        init();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mUnbinder != null){
            mUnbinder.unbind();
        }
    }

    public abstract int setLayout();

    public abstract  void setupAcitivtyComponent(AppComponent appComponent);


    public abstract void  init();

    @Override
    public void showLoading() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void dismissLoading() {

    }
}
