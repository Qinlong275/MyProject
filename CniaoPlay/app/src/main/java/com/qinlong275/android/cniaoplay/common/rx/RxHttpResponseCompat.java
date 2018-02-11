package com.qinlong275.android.cniaoplay.common.rx;


import com.qinlong275.android.cniaoplay.bean.BaseBean;
import com.qinlong275.android.cniaoplay.common.exception.ApiException;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by 秦龙 on 2018/2/9.
 */

//封装Http响应结果预处理
public class RxHttpResponseCompat {

    public static <T> Observable.Transformer<BaseBean<T>, T> compatResult() {


        return new Observable.Transformer<BaseBean<T>, T>() {
            @Override
            public Observable<T> call(Observable<BaseBean<T>> baseBeanObservable) {


                return baseBeanObservable.flatMap(new Func1<BaseBean<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(final BaseBean<T> tBaseBean) {

                        if (tBaseBean.success()) {


                            return Observable.create(new Observable.OnSubscribe<T>() {
                                @Override
                                public void call(Subscriber<? super T> subscriber) {

                                    try {
                                        subscriber.onNext(tBaseBean.getData());
                                        subscriber.onCompleted();
                                    } catch (Exception e) {
                                        subscriber.onError(e);
                                    }
                                }
                            });


                        } else {
                            return Observable.error(new ApiException(tBaseBean.getStatus(), tBaseBean.getMessage()));
                        }

                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
            }
        };


    }
}
