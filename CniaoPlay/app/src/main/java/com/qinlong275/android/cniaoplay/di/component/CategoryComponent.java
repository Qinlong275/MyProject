package com.qinlong275.android.cniaoplay.di.component;




import com.qinlong275.android.cniaoplay.di.FragmentScope;
import com.qinlong275.android.cniaoplay.di.module.CategoryModel;
import com.qinlong275.android.cniaoplay.ui.fragment.CategoryFragment;

import dagger.Component;

/**
 * 菜鸟窝http://www.cniao5.com 一个高端的互联网技能学习平台
 *
 * @author Ivan
 * @version V1.0
 * @Package com.cniao5.cniao5play.di
 * @Description: ${TODO}(用一句话描述该文件做什么)
 * @date
 */

@FragmentScope
@Component(modules = CategoryModel.class,dependencies = AppComponent.class)
public interface CategoryComponent {


    void inject(CategoryFragment fragment);
}
