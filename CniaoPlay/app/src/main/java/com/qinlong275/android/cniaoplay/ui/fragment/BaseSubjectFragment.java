package com.qinlong275.android.cniaoplay.ui.fragment;


import com.qinlong275.android.cniaoplay.bean.PageBean;
import com.qinlong275.android.cniaoplay.bean.Subject;
import com.qinlong275.android.cniaoplay.bean.SubjectDetail;
import com.qinlong275.android.cniaoplay.di.component.AppComponent;
import com.qinlong275.android.cniaoplay.di.component.DaggerSubjectComponent;
import com.qinlong275.android.cniaoplay.di.module.SubjectModule;
import com.qinlong275.android.cniaoplay.presenter.SubjectPresenter;
import com.qinlong275.android.cniaoplay.presenter.contract.SubjectContract;

/**
 * 菜鸟窝http://www.cniao5.com 一个高端的互联网技能学习平台
 *
 * @author Ivan
 * @version V1.0
 * @Package com.cniao5.cniao5play.ui.fragment
 * @Description: ${TODO}(用一句话描述该文件做什么)
 * @date
 */

public  abstract  class BaseSubjectFragment extends ProgressFragment<SubjectPresenter> implements SubjectContract.SubjectView {


    BaseSubjectFragment(){

    }


    @Override
    public void showSubjects(PageBean<Subject> subjects) {

    }

    @Override
    public void onLoadMoreComplete() {

    }

    @Override
    public void showSubjectDetail(SubjectDetail detail) {

    }


    @Override
    public void setupAcitivtyComponent(AppComponent appComponent) {

        DaggerSubjectComponent.builder().appComponent(appComponent).subjectModule(new SubjectModule(this))
                .build().inject(this);
    }
}
