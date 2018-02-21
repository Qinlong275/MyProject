package com.qinlong275.android.cniaoplay.presenter;


import com.qinlong275.android.cniaoplay.bean.PageBean;
import com.qinlong275.android.cniaoplay.bean.Subject;
import com.qinlong275.android.cniaoplay.bean.SubjectDetail;
import com.qinlong275.android.cniaoplay.common.rx.RxHttpResponseCompat;
import com.qinlong275.android.cniaoplay.common.rx.subscriber.ErrorHandleSubscriber;
import com.qinlong275.android.cniaoplay.common.rx.subscriber.ProgressSubscriber;
import com.qinlong275.android.cniaoplay.presenter.contract.SubjectContract;

import javax.inject.Inject;

import io.reactivex.Observer;

/**
 * 菜鸟窝http://www.cniao5.com 一个高端的互联网技能学习平台
 *
 * @author Ivan
 * @version V1.0
 * @Package com.cniao5.cniao5market.presenter
 * @Description: ${TODO}(用一句话描述该文件做什么)
 * @date
 */

public class SubjectPresenter extends BasePresenter<SubjectContract.ISubjectModel,SubjectContract.SubjectView> {


    @Inject
    public SubjectPresenter(SubjectContract.ISubjectModel iSubjectModel,
                            SubjectContract.SubjectView subjectView) {
        super(iSubjectModel, subjectView);
    }




    public void  getSubjects(int page){


        Observer subscriber =null;

        if(page ==0){

            subscriber= new ProgressSubscriber<PageBean<Subject>>(mContext,mView) {
                @Override
                public void onNext(PageBean<Subject> subjectPageBean) {
                    mView.showSubjects(subjectPageBean);
                }
            };
        }
        else{
            subscriber = new ErrorHandleSubscriber<PageBean<Subject>>(mContext) {
                @Override
                public void onComplete() {
                    mView.onLoadMoreComplete();
                }

                @Override
                public void onNext(PageBean<Subject> pageBean) {

                    mView.showSubjects(pageBean);
                }
            };
        }

        mModel.getSubjects(page)
                .compose(RxHttpResponseCompat.<PageBean<Subject>>compatResult())
                .subscribe(subscriber);


    }



    public void getSubjectDetail(int id){


        mModel.getSubjectDetail(id).compose(RxHttpResponseCompat.<SubjectDetail>compatResult())
                .subscribe(new ProgressSubscriber<SubjectDetail>(mContext,mView) {
                    @Override
                    public void onNext(SubjectDetail subjectDetail) {

                        mView.showSubjectDetail(subjectDetail);
                    }
                });


    }










}
