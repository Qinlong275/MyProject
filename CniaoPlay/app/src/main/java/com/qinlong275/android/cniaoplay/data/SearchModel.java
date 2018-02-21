package com.qinlong275.android.cniaoplay.data;


import com.qinlong275.android.cniaoplay.bean.BaseBean;
import com.qinlong275.android.cniaoplay.bean.SearchResult;
import com.qinlong275.android.cniaoplay.data.http.ApiService;
import com.qinlong275.android.cniaoplay.presenter.contract.SearchContract;

import java.util.List;

import io.reactivex.Observable;

/**
 * 菜鸟窝http://www.cniao5.com 一个高端的互联网技能学习平台
 *
 * @author Ivan
 * @version V1.0
 * @Package com.cniao5.cniao5market.data.model
 * @Description: ${TODO}(用一句话描述该文件做什么)
 * @date
 */

public class SearchModel implements SearchContract.ISearchModel {


    private ApiService mApiService;


    public SearchModel(ApiService apiService){

        this.mApiService = apiService;
    }

    public  Observable<BaseBean<List<String>>> getSuggestion(String keyword){


        return  mApiService.searchSuggest(keyword);

    }

    @Override
    public Observable<BaseBean<SearchResult>> search(String keyword) {
        return  mApiService.search(keyword);
    }
}
