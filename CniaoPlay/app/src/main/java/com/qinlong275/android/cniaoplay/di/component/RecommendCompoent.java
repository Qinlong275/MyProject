package com.qinlong275.android.cniaoplay.di.component;

import com.qinlong275.android.cniaoplay.di.FragmentScope;
import com.qinlong275.android.cniaoplay.di.module.RecommendModule;
import com.qinlong275.android.cniaoplay.ui.fragment.RecommendFragment;

import dagger.Component;

/**
 * Created by 秦龙 on 2018/2/6.
 */

@FragmentScope
@Component(modules = RecommendModule.class,dependencies = AppComponent.class)
public interface RecommendCompoent {

    void inject(RecommendFragment fragment);
}
