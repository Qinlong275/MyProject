package com.qinlong275.android.cniaoplay.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qinlong275.android.cniaoplay.AppAplication;
import com.qinlong275.android.cniaoplay.R;
import com.qinlong275.android.cniaoplay.di.component.AppComponent;
import com.qinlong275.android.cniaoplay.presenter.BasePresenter;
import com.qinlong275.android.cniaoplay.ui.BaseView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 秦龙 on 2018/2/10.
 */

public abstract class ProgressFragment<T extends BasePresenter> extends Fragment implements BaseView{

    private FrameLayout mRootView;
    private TextView mTextError;

    private View mViewProgress;
    private FrameLayout mViewContent;
    private View mViewEmpty;
    private Unbinder mUnbinder;

    protected AppAplication mAppAplication;

    @Inject
    T mPresenter ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //最外层的view
        mRootView = (FrameLayout) inflater.inflate(R.layout.fragment_progress,container,false);
        mViewProgress = mRootView.findViewById(R.id.view_progress);
        mViewContent = (FrameLayout) mRootView.findViewById(R.id.view_content);
        mViewEmpty = mRootView.findViewById(R.id.view_empty);
        mTextError=(TextView)mRootView.findViewById(R.id.text_tip);
        
        mViewEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEmptyClick();
            }
        });
        return mRootView;
    }

    abstract void onEmptyClick();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("Fragment","重建");
        this.mAppAplication = (AppAplication) getActivity().getApplication();
        setupAcitivtyComponent(mAppAplication.getAppComponent());
        setRealContentView();

        //初始化realcontent的界面
        init();
    }

    private void setRealContentView(){
        //绑定contentView的真实内容，初始时为空
        View realContentView=LayoutInflater.from(getActivity()).inflate(setLayout(),mViewContent,true);
        mUnbinder=  ButterKnife.bind(this, realContentView);
    }

    public void showProgressView(){
        showView(R.id.view_progress);
    }

    public void showContentView(){
        showView(R.id.view_content);
    }

    public void showEmptyView(){
        showView(R.id.view_empty);
    }

    public void showEmptyView(int resId){
        showView(R.id.view_empty);
        mTextError.setText(resId);
    }

    public void showEmptyView(String message){
        showView(R.id.view_empty);
        mTextError.setText(message);
        Log.d("fragment","empty");
    }

    public void showView(int viewId){
        for (int i=0;i<mRootView.getChildCount();i++){
            if (mRootView.getChildAt(i).getId()==viewId){
                mRootView.getChildAt(i).setVisibility(View.VISIBLE);
            }else {
                mRootView.getChildAt(i).setVisibility(View.INVISIBLE);
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mUnbinder != null){
            mUnbinder.unbind();
        }
    }

    @Override
    public void showLoading() {
        showProgressView();
    }

    @Override
    public void showError(String msg) {
        showEmptyView(msg);
    }

    @Override
    public void dismissLoading() {
        showContentView();
    }

    public abstract int setLayout();

    public abstract  void setupAcitivtyComponent(AppComponent appComponent);

    public abstract void  init();
}
