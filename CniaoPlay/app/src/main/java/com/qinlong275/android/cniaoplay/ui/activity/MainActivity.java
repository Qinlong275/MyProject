package com.qinlong275.android.cniaoplay.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.qinlong275.android.cniaoplay.R;
import com.qinlong275.android.cniaoplay.di.component.AppComponent;
import com.qinlong275.android.cniaoplay.ui.adapter.ViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private View headView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //使用字体图片
        super.onCreate(savedInstanceState);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void init() {
        initDrawerLayout();
        initTabLayout();
    }

    private void initTabLayout() {
        PagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager());
        //设置后台加载页面的个数
        mViewPager.setOffscreenPageLimit(adapter.getCount());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    private void initDrawerLayout() {
        headView = mNavigationView.getHeaderView(0);
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "headview", Toast.LENGTH_SHORT).show();
            }
        });

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_app_update:
                        Toast.makeText(MainActivity.this, "应用更新", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_message:
                        Toast.makeText(MainActivity.this, "信息", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        mToolBar.inflateMenu(R.menu.toolbar_menu);

        // implements DrawerLayout.DrawerListener监听状态,生成一个类似打开drawer键
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar, R.string.open, R.string.close);
        drawerToggle.syncState();
        mDrawerLayout.addDrawerListener(drawerToggle);
    }

}
