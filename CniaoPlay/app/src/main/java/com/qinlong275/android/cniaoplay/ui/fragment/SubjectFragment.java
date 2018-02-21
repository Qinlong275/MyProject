package com.qinlong275.android.cniaoplay.ui.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qinlong275.android.cniaoplay.R;
import com.qinlong275.android.cniaoplay.bean.PageBean;
import com.qinlong275.android.cniaoplay.bean.Subject;
import com.qinlong275.android.cniaoplay.common.rx.RxBus;
import com.qinlong275.android.cniaoplay.ui.adapter.SubjectAdapter;
import com.qinlong275.android.cniaoplay.ui.widget.SpaceItemDecoration2;


import butterknife.BindView;

/**
 * 菜鸟窝http://www.cniao5.com 一个高端的互联网技能学习平台
 *
 * @author Ivan
 * @version V1.0
 * @Package com.cniao5.cniao5play.ui.fragment
 * @Description: ${TODO}(用一句话描述该文件做什么)
 * @date
 */

public class SubjectFragment extends BaseSubjectFragment implements  BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.recycle_view)
    RecyclerView mRecyclerView;

    private SubjectAdapter mAdapter;

    int page =0;
    @Override
    public void init() {


        initRecyclerView();

        mPresenter.getSubjects(page);
    }

    @Override
    void onEmptyClick() {
        mPresenter.getSubjects(page);
    }

    @Override
    public int setLayout() {
        return R.layout.template_recycleview;
    }

    protected void initRecyclerView(){


        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        mRecyclerView.setLayoutManager(layoutManager);

        //间隔
        SpaceItemDecoration2 dividerDecoration = new SpaceItemDecoration2(5);
        mRecyclerView.addItemDecoration(dividerDecoration);


        mAdapter = new SubjectAdapter();

        //加载多页的响应
        mAdapter.setOnLoadMoreListener(this);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {


                Subject subject = mAdapter.getItem(position);

                //实现界面间的消息传递
                RxBus.getDefault().post(subject);

            }
        });

    }

    @Override
    public void showSubjects(PageBean<Subject> subjects) {

        mAdapter.addData(subjects.getDatas());

        if(subjects.isHasMore()){
            page++;
        }
        mAdapter.setEnableLoadMore(subjects.isHasMore());
    }

    @Override
    public void onLoadMoreRequested() {

        mPresenter.getSubjects(page);
    }
}
