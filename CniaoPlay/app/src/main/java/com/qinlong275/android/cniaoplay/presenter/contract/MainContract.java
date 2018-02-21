package com.qinlong275.android.cniaoplay.presenter.contract;


import com.qinlong275.android.cniaoplay.bean.AppInfo;
import com.qinlong275.android.cniaoplay.bean.BaseBean;
import com.qinlong275.android.cniaoplay.bean.RequestBean.AppsUpdateBean;
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

public class MainContract {


    public  interface   MainView extends BaseView {


        void requestPermissonSuccess();
        void requestPermissonFail();

        void changeAppNeedUpdateCount(int count);
    }


    public interface IMainModel{


        Observable<BaseBean<List<AppInfo>>> getUpdateApps(AppsUpdateBean param);

    }
}
