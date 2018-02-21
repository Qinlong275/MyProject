package com.qinlong275.android.cniaoplay.presenter.contract;



import com.qinlong275.android.cniaoplay.bean.BaseBean;
import com.qinlong275.android.cniaoplay.bean.PageBean;
import com.qinlong275.android.cniaoplay.bean.Subject;
import com.qinlong275.android.cniaoplay.bean.SubjectDetail;
import com.qinlong275.android.cniaoplay.ui.BaseView;

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

public class SubjectContract {


    public  interface   SubjectView extends BaseView {


        void showSubjects(PageBean<Subject> subjects);
        void onLoadMoreComplete();

        void showSubjectDetail(SubjectDetail detail);

    }


    public interface ISubjectModel{

        Observable<BaseBean<PageBean<Subject>>> getSubjects(int page);

        Observable<BaseBean<SubjectDetail>> getSubjectDetail(int id);



    }
}
