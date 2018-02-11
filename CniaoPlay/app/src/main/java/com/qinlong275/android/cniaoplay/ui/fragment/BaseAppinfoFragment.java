package com.qinlong275.android.cniaoplay.ui.fragment;

import android.content.pm.PackageManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qinlong275.android.cniaoplay.R;
import com.qinlong275.android.cniaoplay.bean.AppInfo;
import com.qinlong275.android.cniaoplay.bean.PageBean;
import com.qinlong275.android.cniaoplay.di.component.AppComponent;
import com.qinlong275.android.cniaoplay.presenter.AppinfoPresenter;
import com.qinlong275.android.cniaoplay.presenter.contract.AppinfoContract;
import com.qinlong275.android.cniaoplay.ui.adapter.AppInfoAdapter;
import com.qinlong275.android.cniaoplay.ui.decoration.DividerItemDecoration;

import butterknife.BindView;

/**
 * Created by 秦龙 on 2018/2/11.
 */

public abstract class BaseAppinfoFragment extends ProgressFragment<AppinfoPresenter>
        implements AppinfoContract.AppinfoView,BaseQuickAdapter.RequestLoadMoreListener{
    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;

    private AppInfoAdapter mAppInfoAdapter;
    int page=0;

    @Override
    public int setLayout() {
        return R.layout.template_recycleview;
    }

    @Override
    public void init() {
        initRecyclerView();
        mPresenter.requestData(page,type());
    }

    private void initRecyclerView(){
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST);

        mRecycleView.addItemDecoration(itemDecoration);
        mAppInfoAdapter=buideAdapter();
        mAppInfoAdapter.setOnLoadMoreListener(this);
        mRecycleView.setAdapter(mAppInfoAdapter);
    }

    abstract int type();
    abstract AppInfoAdapter buideAdapter();

    @Override
    public void showResult(PageBean<AppInfo> appInfoPageBean) {
        mAppInfoAdapter.addData(appInfoPageBean.getDatas());
        if (appInfoPageBean.isHasMore()){
            page++;
        }
        mAppInfoAdapter.setEnableLoadMore(appInfoPageBean.isHasMore());
    }

    @Override
    public void onLoadComplete() {
        mAppInfoAdapter.loadMoreComplete();
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.requestData(type(),page);
    }
}
