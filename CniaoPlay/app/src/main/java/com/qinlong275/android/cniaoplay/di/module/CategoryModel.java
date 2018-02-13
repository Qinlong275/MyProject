package com.qinlong275.android.cniaoplay.di.module;

import com.qinlong275.android.cniaoplay.data.http.ApiService;
import com.qinlong275.android.cniaoplay.presenter.contract.CategoryContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 秦龙 on 2018/2/12.
 */

@Module
public class CategoryModel {

    private CategoryContract.CategoryView mView;


    public CategoryModel(CategoryContract.CategoryView view) {
        mView = view;
    }

    @Provides
    public CategoryContract.CategoryView provideView(){
        return mView;
    }

    @Provides
    public CategoryContract.ICategoryModel provideModel(ApiService apiService){
        return new com.qinlong275.android.cniaoplay.data.CategoryModel(apiService);
    }
}
