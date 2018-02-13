package com.qinlong275.android.cniaoplay.data;

import com.qinlong275.android.cniaoplay.bean.BaseBean;
import com.qinlong275.android.cniaoplay.bean.Category;
import com.qinlong275.android.cniaoplay.data.http.ApiService;
import com.qinlong275.android.cniaoplay.presenter.contract.CategoryContract;

import java.util.List;

import rx.Observable;

/**
 * Created by 秦龙 on 2018/2/12.
 */

public class CategoryModel implements CategoryContract.ICategoryModel{

    private ApiService mApiService;

    public CategoryModel(ApiService apiService) {
        mApiService = apiService;
    }


    @Override
    public Observable<BaseBean<List<Category>>> getCategories() {
        return mApiService.getCategories();
    }
}
