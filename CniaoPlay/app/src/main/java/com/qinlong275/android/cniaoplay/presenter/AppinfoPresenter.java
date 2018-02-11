package com.qinlong275.android.cniaoplay.presenter;

import com.qinlong275.android.cniaoplay.bean.AppInfo;
import com.qinlong275.android.cniaoplay.bean.BaseBean;
import com.qinlong275.android.cniaoplay.bean.PageBean;
import com.qinlong275.android.cniaoplay.common.rx.RxHttpResponseCompat;
import com.qinlong275.android.cniaoplay.common.rx.subscriber.ErrorHandleSubscriber;
import com.qinlong275.android.cniaoplay.common.rx.subscriber.ProgressSubscriber;
import com.qinlong275.android.cniaoplay.data.AppInfoModel;
import com.qinlong275.android.cniaoplay.presenter.contract.AppinfoContract;

import javax.inject.Inject;

import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by 秦龙 on 2018/2/11.
 */

public class AppinfoPresenter extends BasePresenter<AppInfoModel,AppinfoContract.AppinfoView> {

    public static final int  TOP_LIST=1;
    public static final int  GAME=2;

    @Inject
    public AppinfoPresenter(AppInfoModel appInfoModel, AppinfoContract.AppinfoView toplistView) {
        super(appInfoModel, toplistView);
    }

    public void requestData(int page,int type){

        Subscriber subscriber=null;
        if (page==0){
            //第一页
            subscriber=new ProgressSubscriber<PageBean<AppInfo>>(mContext,mView) {
                @Override
                public void onNext(PageBean<AppInfo> appInfoPageBean) {
                    mView.showResult(appInfoPageBean);
                }
            };
        }else {
            //加载下一页
            subscriber=new ErrorHandleSubscriber<PageBean<AppInfo>>(mContext) {

                @Override
                public void onCompleted() {
                    mView.onLoadComplete();
                }

                @Override
                public void onNext(PageBean<AppInfo> appInfoPageBean) {
                    mView.showResult(appInfoPageBean);
                }
            };
        }

        Observable observable=getObservable(type,page);
        observable
                .compose(RxHttpResponseCompat.<PageBean<AppInfo>>compatResult())
                .subscribe(subscriber);
    }

    private Observable<BaseBean<PageBean<AppInfo>>> getObservable(int type,int page){

        switch (type){

            case TOP_LIST:
                return  mModel.topList(page);


            case GAME:
                return  mModel.games(page);

            default:
                return Observable.empty();
        }

    }
}
