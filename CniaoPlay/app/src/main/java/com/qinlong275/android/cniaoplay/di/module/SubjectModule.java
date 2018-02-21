package com.qinlong275.android.cniaoplay.di.module;




import com.qinlong275.android.cniaoplay.data.SubjectModel;
import com.qinlong275.android.cniaoplay.data.http.ApiService;
import com.qinlong275.android.cniaoplay.di.FragmentScope;
import com.qinlong275.android.cniaoplay.presenter.contract.SubjectContract;

import dagger.Module;
import dagger.Provides;

/**
 * 菜鸟窝http://www.cniao5.com 一个高端的互联网技能学习平台
 *
 * @author Ivan
 * @version V1.0
 * @Package com.cniao5.cniao5market.di.module
 * @Description: ${TODO}(用一句话描述该文件做什么)
 * @date
 */

@Module
public class SubjectModule {


    private SubjectContract.SubjectView mView;


    public SubjectModule(SubjectContract.SubjectView view){
        this.mView = view;
    }



    @FragmentScope
    @Provides
    public SubjectContract.ISubjectModel provideModel(ApiService apiService){

        return  new SubjectModel(apiService);
    }



    @FragmentScope
    @Provides
    public SubjectContract.SubjectView provideView(){


        return  mView;
    }
}
