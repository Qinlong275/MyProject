package com.qinlong275.android.cniaoplay.di.component;



import com.qinlong275.android.cniaoplay.di.FragmentScope;
import com.qinlong275.android.cniaoplay.di.module.AppManagerModule;
import com.qinlong275.android.cniaoplay.ui.fragment.AppManangerFragment;
import com.qinlong275.android.cniaoplay.ui.fragment.DownloadedFragment;
import com.qinlong275.android.cniaoplay.ui.fragment.DownloadingFragment;
import com.qinlong275.android.cniaoplay.ui.fragment.InstalledAppAppFragment;

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
@Component(modules = AppManagerModule.class,dependencies = AppComponent.class)
public interface AppManagerComponent {

    void inject(AppManangerFragment fragment);
//    void injectDownloaded(DownloadedFragment fragment);
//    void injectInstalled(InstalledAppAppFragment fragment);
}
