package com.qinlong275.android.cniaoplay.di.component;

import com.qinlong275.android.cniaoplay.di.FragmentScope;
import com.qinlong275.android.cniaoplay.di.module.AppinfoModule;
import com.qinlong275.android.cniaoplay.ui.fragment.CategoryAppFragment;
import com.qinlong275.android.cniaoplay.ui.fragment.GamesFragment;
import com.qinlong275.android.cniaoplay.ui.fragment.ToplistFragment;

import dagger.Component;

/**
 * Created by 秦龙 on 2018/2/11.
 */

@FragmentScope
@Component(modules = AppinfoModule.class,dependencies = AppComponent.class)
public interface AppinfoComponent {
    void injectToplistFragment(ToplistFragment fragment);
    void injectGameFragment(GamesFragment fragment);
    void injectCategoryAppFragment(CategoryAppFragment fragment);

}
