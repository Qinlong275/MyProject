package com.qinlong275.android.cniaoplay.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikepenz.ionicons_typeface_library.Ionicons;
import com.qinlong275.android.cniaoplay.R;
import com.qinlong275.android.cniaoplay.bean.User;
import com.qinlong275.android.cniaoplay.common.Constant;
import com.qinlong275.android.cniaoplay.common.font.Cniao5Font;
import com.qinlong275.android.cniaoplay.common.imageloader.GlideCircleTransform;
import com.qinlong275.android.cniaoplay.common.imageloader.ImageLoader;
import com.qinlong275.android.cniaoplay.common.rx.RxBus;
import com.qinlong275.android.cniaoplay.common.util.ACache;
import com.qinlong275.android.cniaoplay.common.util.PermissionUtil;
import com.qinlong275.android.cniaoplay.di.component.AppComponent;
import com.qinlong275.android.cniaoplay.di.component.DaggerMainComponent;
import com.qinlong275.android.cniaoplay.di.module.MainModule;
import com.qinlong275.android.cniaoplay.presenter.MainPresenter;
import com.qinlong275.android.cniaoplay.presenter.contract.MainContract;
import com.qinlong275.android.cniaoplay.ui.adapter.ViewPagerAdapter;
import com.qinlong275.android.cniaoplay.ui.bean.FragmentInfo;
import com.qinlong275.android.cniaoplay.ui.fragment.CategoryFragment;
import com.qinlong275.android.cniaoplay.ui.fragment.GamesFragment;
import com.qinlong275.android.cniaoplay.ui.fragment.RecommendFragment;
import com.qinlong275.android.cniaoplay.ui.fragment.ToplistFragment;
import com.qinlong275.android.cniaoplay.ui.widget.BadgeActionProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;


public class MainActivity extends BaseActivity <MainPresenter> implements MainContract.MainView {
    private static final String TAG = "MainActivity";

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

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
    private ImageView mUserHeadView;
    private TextView mTextUserName;

    private  BadgeActionProvider badgeActionProvider;

    @Override
    public int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerMainComponent.builder().appComponent(appComponent)
                .mainModule(new MainModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "获取权限成功");
                } else {
                    Toast.makeText(this, "你否认了权限", Toast.LENGTH_SHORT).show();
                }
        }
    }


    @Override
    public void init() {

        //从设置页面取得安装信息
        boolean key_smart_install= getSharedPreferences(getPackageName()+"_preferences",MODE_PRIVATE).getBoolean("key_smart_install",false);

        Log.d("MainActivity","key_smart_install="+key_smart_install);

        RxBus.getDefault().toObservable(User.class).subscribe(new Consumer<User>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull User user) throws Exception {

                initHeadView(user);
            }
        });

        mPresenter.requestPermisson();

        //判断是否有应用更新，来决定页面菜单栏的显示
        mPresenter.getAppUpdateInfo();

    }

    private void initToolbar(){

        mToolBar.inflateMenu(R.menu.toolbar_menu);

        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getItemId() == R.id.action_search){

                    startActivity(new Intent(MainActivity.this,SearchActivity.class));
                }

                return true;
            }
        });

        MenuItem downloadMenuItem = mToolBar.getMenu().findItem(R.id.action_download);


        //提供菜单栏按钮的显示
        badgeActionProvider = (BadgeActionProvider) MenuItemCompat.getActionProvider(downloadMenuItem);

        badgeActionProvider.setIcon(DrawableCompat.wrap(new IconicsDrawable(this, Cniao5Font.Icon.cniao_download).color(ContextCompat.getColor(this,R.color.white))));

        badgeActionProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toAppManagerActivity(badgeActionProvider.getBadgeNum()>0?2:0);

            }
        });

    }

    private void toAppManagerActivity(int position){

        //根据是否有更新来决定开启AppManagerActivity时显示的是哪一个界面
        Intent intent = new Intent(MainActivity.this,AppMangerActivity.class);

        intent.putExtra(Constant.POSITION,position);

        startActivity(new Intent(intent));

    }

    private List<FragmentInfo> initFragments(){

        List<FragmentInfo> mFragments = new ArrayList<>(4);

        mFragments.add(new FragmentInfo("推荐",RecommendFragment.class));
        mFragments.add(new FragmentInfo ("排行", ToplistFragment.class));


        mFragments.add(new FragmentInfo ("游戏", GamesFragment.class));
        mFragments.add(new FragmentInfo ("分类", CategoryFragment.class));

        return  mFragments;

    }

    private void initTabLayout() {
        PagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager(),initFragments());
        //设置后台加载页面的个数,不会每次左右切换Fragment重新请求数据
        mViewPager.setOffscreenPageLimit(adapter.getCount());
        mViewPager.setAdapter(adapter);
        //绑定Tablayout和Viewpager
        mTabLayout.setupWithViewPager(mViewPager);

    }

    private void initDrawerLayout() {
        headView = mNavigationView.getHeaderView(0);

        mUserHeadView = (ImageView) headView.findViewById(R.id.img_avatar);
        mUserHeadView.setImageDrawable(new IconicsDrawable(this, Cniao5Font.Icon.cniao_head).colorRes(R.color.white));

        mTextUserName = (TextView) headView.findViewById(R.id.txt_username);

        mNavigationView.getMenu().findItem(R.id.menu_app_update).setIcon(new IconicsDrawable(this, Ionicons.Icon.ion_ios_loop));
        mNavigationView.getMenu().findItem(R.id.menu_download_manager).setIcon(new IconicsDrawable(this, Cniao5Font.Icon.cniao_download));
        mNavigationView.getMenu().findItem(R.id.menu_app_uninstall).setIcon(new IconicsDrawable(this, Ionicons.Icon.ion_ios_trash_outline));
        mNavigationView.getMenu().findItem(R.id.menu_setting).setIcon(new IconicsDrawable(this, Ionicons.Icon.ion_ios_gear_outline));

        mNavigationView.getMenu().findItem(R.id.menu_logout).setIcon(new IconicsDrawable(this, Cniao5Font.Icon.cniao_shutdown));


        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_logout:
                        logout();
                        break;
                    case R.id.menu_download_manager:

                        toAppManagerActivity(1);

                        break;
                    case R.id.menu_app_uninstall:

                        toAppManagerActivity(3);

                        break;
                    case R.id.menu_app_update:

                        toAppManagerActivity(2);

                        break;

                    case R.id.menu_setting:

                        startActivity(new Intent(MainActivity.this,SettingActivity.class));

                        break;

                }
                return false;
            }
        });

        // implements DrawerLayout.DrawerListener监听状态,生成一个类似打开drawer键
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar, R.string.open, R.string.close);
        drawerToggle.syncState();
        mDrawerLayout.addDrawerListener(drawerToggle);
    }

    private void logout(){
        ACache aCache=ACache.get(this);
        aCache.put(Constant.TOKEN,"");
        aCache.put(Constant.USER,"");
        mUserHeadView.setImageDrawable(new IconicsDrawable(this, Cniao5Font.Icon.cniao_head).colorRes(R.color.white));
        mUserHeadView.setClickable(true);
        mTextUserName.setText("未登录");

        mUserHeadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });

        Toast.makeText(MainActivity.this,"您已退出登录",Toast.LENGTH_LONG).show();
    }


    private void initHeadView(User user){
        Glide.with(this).load(user.getLogo_url()).transform(new GlideCircleTransform(this)).into(mUserHeadView);
        mTextUserName.setText(user.getUsername());
        mUserHeadView.setOnClickListener(null);
    }

    private void initUser(){
        Object objUser= ACache.get(this).getAsObject(Constant.USER);
        if (objUser==null){
            mUserHeadView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }
            });
        }else {
            User user=(User)objUser;
            initHeadView(user);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void requestPermissonSuccess() {

        initToolbar();
        initDrawerLayout();
        initTabLayout();
        initUser();
    }

    @Override
    public void requestPermissonFail() {

        Toast.makeText(MainActivity.this,"授权失败....",Toast.LENGTH_LONG).show();
    }

    @Override
    public void changeAppNeedUpdateCount(int count) {

        if(count>0){

            badgeActionProvider.setText(count+"");
        }
        else{

            badgeActionProvider.hideBadge();
        }

    }
}
