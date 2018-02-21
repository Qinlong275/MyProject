package com.qinlong275.android.cniaoplay.di.component;


import com.qinlong275.android.cniaoplay.di.FragmentScope;
import com.qinlong275.android.cniaoplay.di.module.SearchModule;
import com.qinlong275.android.cniaoplay.ui.activity.SearchActivity;

import dagger.Component;

/**
 * 菜鸟窝http://www.cniao5.com 一个高端的互联网技能学习平台
 * @author Ivan
 * @version V1.0
 * @Package com.cniao5.cniao5market.di.component
 * @Description: ${TODO}(用一句话描述该文件做什么)
 * @date
 */

@FragmentScope
@Component(modules = SearchModule.class,dependencies= AppComponent.class)
public interface SearchComponent {

    void  inject(SearchActivity fragment);
}
