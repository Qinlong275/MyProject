package com.qinlong275.android.cniaoplay.ui.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;
import com.qinlong275.android.cniaoplay.R;
import com.qinlong275.android.cniaoplay.di.component.AppComponent;
import com.qinlong275.android.cniaoplay.ui.fragment.HotAppFragment;
import com.qinlong275.android.cniaoplay.ui.fragment.HotGameFragment;

import butterknife.BindView;

/**
 * Created by 秦龙 on 2018/2/21.
 */

public class HotGameActivity extends BaseActivity{


    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.content_layout)
    FrameLayout mContentLayout;

    @Override
    public int setLayout() {
        return R.layout.activity_hot_app;
    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void init() {

        mToolBar.setNavigationIcon(
                new IconicsDrawable(this)
                        .icon(Ionicons.Icon.ion_ios_arrow_back)
                        .sizeDp(16)
                        .color(getResources().getColor(R.color.md_white_1000)
                        )
        );
        mToolBar.setTitle(R.string.hot_game);

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        showFragment();
    }

    private void showFragment() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        HotGameFragment fragment = new HotGameFragment();
        ft.add(R.id.content_layout,fragment);

        ft.commit();

    }

}
