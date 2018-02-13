package com.qinlong275.android.cniaoplay.presenter.contract;

import com.qinlong275.android.cniaoplay.bean.BaseBean;
import com.qinlong275.android.cniaoplay.bean.Category;
import com.qinlong275.android.cniaoplay.ui.BaseView;

import java.util.List;

import rx.Observable;

/**
 * Created by 秦龙 on 2018/2/12.
 */

public interface CategoryContract {

    public interface ICategoryModel{
        Observable<BaseBean<List<Category>>> getCategories();
    }

    public interface CategoryView extends BaseView{
        public void showData(List<Category> categories);
    }


}
