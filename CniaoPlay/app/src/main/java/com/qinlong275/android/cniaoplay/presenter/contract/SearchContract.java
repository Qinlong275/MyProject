package com.qinlong275.android.cniaoplay.presenter.contract;


import com.qinlong275.android.cniaoplay.bean.BaseBean;
import com.qinlong275.android.cniaoplay.bean.SearchResult;
import com.qinlong275.android.cniaoplay.ui.BaseView;

import java.util.List;

import io.reactivex.Observable;


/**
 * 菜鸟窝http://www.cniao5.com 一个高端的互联网技能学习平台
 *
 * @author Ivan
 * @version V1.0
 * @Package com.cniao5.cniao5market.contract
 * @Description: ${TODO}(用一句话描述该文件做什么)
 * @date
 */

public class SearchContract {


    public  interface   SearchView extends BaseView {


        void showSearchHistory(List<String> list);
        void showSuggestions(List<String> list);
        void showSearchResult(SearchResult result);

    }





    public interface ISearchModel{

        Observable<BaseBean<List<String>>> getSuggestion(String keyword);

        Observable<BaseBean<SearchResult>> search(String keyword);

    }
}
