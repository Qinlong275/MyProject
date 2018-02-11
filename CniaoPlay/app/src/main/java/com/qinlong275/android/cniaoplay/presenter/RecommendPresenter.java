package com.qinlong275.android.cniaoplay.presenter;


import com.qinlong275.android.cniaoplay.bean.IndexBean;
import com.qinlong275.android.cniaoplay.common.rx.RxHttpResponseCompat;
import com.qinlong275.android.cniaoplay.common.rx.subscriber.ProgressSubscriber;
import com.qinlong275.android.cniaoplay.data.AppInfoModel;
import com.qinlong275.android.cniaoplay.presenter.contract.AppinfoContract;

import javax.inject.Inject;

/**
 * Created by 秦龙 on 2018/2/5.
 */

public class RecommendPresenter extends BasePresenter<AppInfoModel, AppinfoContract.View> {

    @Inject
    public RecommendPresenter(AppInfoModel model, AppinfoContract.View view) {
        super(model, view);
    }

    public void requestDatas() {

        mModel.index().compose(RxHttpResponseCompat.<IndexBean>compatResult())
                .subscribe(new ProgressSubscriber<IndexBean>(mContext,mView) {
                    @Override
                    public void onNext(IndexBean indexBean) {
                        mView.showResult(indexBean);
                    }
                });
    }
}
