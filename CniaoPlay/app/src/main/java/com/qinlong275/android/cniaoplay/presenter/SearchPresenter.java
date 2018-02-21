package com.qinlong275.android.cniaoplay.presenter;




import com.qinlong275.android.cniaoplay.bean.SearchResult;
import com.qinlong275.android.cniaoplay.common.rx.RxHttpResponseCompat;
import com.qinlong275.android.cniaoplay.common.rx.subscriber.ProgressSubscriber;
import com.qinlong275.android.cniaoplay.presenter.contract.SearchContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 菜鸟窝http://www.cniao5.com 一个高端的互联网技能学习平台
 *
 * @author Ivan
 * @version V1.0
 * @Package com.cniao5.cniao5market.presenter
 * @Description: ${TODO}(用一句话描述该文件做什么)
 * @date
 */

public class SearchPresenter extends BasePresenter<SearchContract.ISearchModel,SearchContract.SearchView> {


    @Inject
    public SearchPresenter(SearchContract.ISearchModel iSearchModel, SearchContract.SearchView searchtView) {
        super(iSearchModel, searchtView);
    }



    public void getSuggestions(String keyword){



        mModel.getSuggestion(keyword)
                .compose(RxHttpResponseCompat.<List<String>>compatResult())
                .subscribe(new ProgressSubscriber<List<String>>(mContext,mView) {
                    @Override
                    public void onNext(List<String> suggestions) {

                        mView.showSuggestions(suggestions);
                    }
                });

    }



    public void search(String keyword){



         saveSearchHistory(keyword);

        mModel.search(keyword)
                .compose(RxHttpResponseCompat.<SearchResult>compatResult())
                .subscribe(new ProgressSubscriber<SearchResult>(mContext,mView) {
                    @Override
                    public void onNext(SearchResult searchResult) {
                        mView.showSearchResult(searchResult);
                    }
                });

    }

    private void saveSearchHistory(String keyword) {

        // save to database
    }


    public void showHistory(){

        // get search history from  database


        List<String> list = new ArrayList<>();
        list.add("地图");
        list.add("KK");
        list.add("爱奇艺");
        list.add("播放器");
        list.add("支付宝");
        list.add("微信");
        list.add("QQ");
        list.add("TV");
        list.add("直播");
        list.add("妹子");
        list.add("美女");

        mView.showSearchHistory(list);


    }

}
