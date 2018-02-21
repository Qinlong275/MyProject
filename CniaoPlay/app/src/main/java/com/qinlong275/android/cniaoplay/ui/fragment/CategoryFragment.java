package com.qinlong275.android.cniaoplay.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qinlong275.android.cniaoplay.R;
import com.qinlong275.android.cniaoplay.bean.Category;
import com.qinlong275.android.cniaoplay.common.Constant;
import com.qinlong275.android.cniaoplay.di.component.AppComponent;
import com.qinlong275.android.cniaoplay.di.component.DaggerCategoryComponent;
import com.qinlong275.android.cniaoplay.di.module.CategoryModel;
import com.qinlong275.android.cniaoplay.presenter.CateogryPresenter;
import com.qinlong275.android.cniaoplay.presenter.contract.CategoryContract;
import com.qinlong275.android.cniaoplay.ui.activity.CategoryAppActivity;
import com.qinlong275.android.cniaoplay.ui.adapter.CategoryAdapter;
import com.qinlong275.android.cniaoplay.ui.decoration.DividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by Ivan on 16/9/26.
 */

public class CategoryFragment extends ProgressFragment<CateogryPresenter> implements CategoryContract.CategoryView{


    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;

    private CategoryAdapter mAdapter;

    @Override
    void onEmptyClick() {
        mPresenter.getAllCategory();
    }

    @Override
    public int setLayout() {
        return R.layout.template_recycleview;
    }

    @Override
    public void setupAcitivtyComponent(AppComponent appComponent) {
        DaggerCategoryComponent.builder().appComponent(appComponent).categoryModel(new CategoryModel(this))
                .build().inject(this);
    }

    @Override
    public void init() {
        initRecyclerView();
        mPresenter.getAllCategory();
    }

    private void initRecyclerView(){


        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()) );

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST);

        mRecycleView.addItemDecoration(itemDecoration);
        mAdapter = new CategoryAdapter(getActivity());

        mRecycleView.setAdapter(mAdapter);

        mRecycleView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                Intent intent = new Intent(getActivity(), CategoryAppActivity.class);

                //传一个Category过去，要实现序列化接口
                intent.putExtra(Constant.CATEGORY,mAdapter.getData().get(position));

                startActivity(intent);

            }
        });

    }

    @Override
    public void showData(List<Category> categories) {
        mAdapter.addData(categories);
    }
}
