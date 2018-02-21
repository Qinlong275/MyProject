package com.qinlong275.android.cniaoplay.ui.activity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;

import com.mikepenz.iconics.context.IconicsLayoutInflater;
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

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements BaseView {

    private Unbinder mUnbinder;
    protected AppAplication mAppAplication;

    @Inject
    T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(),new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(setLayout());
        mUnbinder=ButterKnife.bind(this);
        mAppAplication=(AppAplication)getApplication();
        setupActivityComponent(mAppAplication.getAppComponent());
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder!=null){
            mUnbinder.unbind();
        }
    }

    public abstract int setLayout();

    public abstract void setupActivityComponent(AppComponent appComponent);

    public abstract void init();

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
