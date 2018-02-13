package com.qinlong275.android.cniaoplay.presenter;

import com.qinlong275.android.cniaoplay.bean.Category;
import com.qinlong275.android.cniaoplay.common.rx.RxHttpResponseCompat;
import com.qinlong275.android.cniaoplay.common.rx.subscriber.ProgressSubscriber;
import com.qinlong275.android.cniaoplay.presenter.contract.CategoryContract;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by 秦龙 on 2018/2/12.
 */

public class CateogryPresenter extends BasePresenter<CategoryContract.ICategoryModel,CategoryContract.CategoryView>{

    @Inject
    public CateogryPresenter(CategoryContract.ICategoryModel iCategoryModel, CategoryContract.CategoryView categoryView) {
        super(iCategoryModel, categoryView);
    }

    public void getAllCategory(){
        mModel.getCategories().compose(RxHttpResponseCompat.<List<Category>>compatResult())
        .subscribe(new ProgressSubscriber<List<Category>>(mContext,mView) {
            @Override
            public void onNext(List<Category> categories) {
                mView.showData(categories);
            }
        });
    }
}
