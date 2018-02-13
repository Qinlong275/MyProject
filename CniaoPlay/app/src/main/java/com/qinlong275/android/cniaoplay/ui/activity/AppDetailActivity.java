package com.qinlong275.android.cniaoplay.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;
import com.qinlong275.android.cniaoplay.R;
import com.qinlong275.android.cniaoplay.bean.AppInfo;
import com.qinlong275.android.cniaoplay.common.Constant;
import com.qinlong275.android.cniaoplay.common.imageloader.ImageLoader;
import com.qinlong275.android.cniaoplay.common.util.DensityUtil;
import com.qinlong275.android.cniaoplay.di.component.AppComponent;
import com.qinlong275.android.cniaoplay.di.component.DaggerAppDetailComponent;
import com.qinlong275.android.cniaoplay.di.module.AppModelModule;
import com.qinlong275.android.cniaoplay.di.module.AppdetailModule;
import com.qinlong275.android.cniaoplay.presenter.AppDetaiPresenter;
import com.qinlong275.android.cniaoplay.presenter.contract.AppinfoContract;
import com.qinlong275.android.cniaoplay.ui.fragment.AppDetailFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppDetailActivity extends BaseActivity<AppDetaiPresenter>{


    @BindView(R.id.view_temp)
    View mViewTemp;


    @BindView(R.id.view_content)
    FrameLayout mViewContent;

    @BindView(R.id.view_coordinator)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.txt_name)
    TextView mTxtName;

    @BindView(R.id.toolbar)
    Toolbar mToolBar;
    @BindView(R.id.img_icon)
    ImageView mImgIcon;

    private AppInfo mAppInfo;


    @Override
    public int setLayout() {
        return R.layout.activity_app_detail;
    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void init() {


        mAppInfo = (AppInfo) getIntent().getSerializableExtra("appinfo");

        ImageLoader.load(Constant.BASE_IMG_URL+mAppInfo.getIcon(),mImgIcon);
        mTxtName.setText(mAppInfo.getDisplayName());




        mToolBar.setNavigationIcon(
                new IconicsDrawable(this)
                        .icon(Ionicons.Icon.ion_ios_arrow_back)
                        .sizeDp(16)
                        .color(getResources().getColor(R.color.md_white_1000)
                        )
        );

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        View view = mAppAplication.getView();
        Bitmap bitmap=getViewImageCache(view);
        if (bitmap!=null){
            mViewTemp.setBackgroundDrawable(new BitmapDrawable(bitmap));
        }

        //固定view的位置，与原位置相同，凸起
        int[]location=new int[2];
        view.getLocationOnScreen(location);
        int left=location[0];
        int top=location[1];

        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(mViewTemp.getLayoutParams());

        marginLayoutParams.topMargin=top- DensityUtil.getStatusBarH(this);
        marginLayoutParams.leftMargin = left;
        marginLayoutParams.width = view.getWidth();
        marginLayoutParams.height =view.getHeight();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(marginLayoutParams);

        mViewTemp.setLayoutParams(params);
        open();
    }

    //展开动画
    private void open(){
        int h=DensityUtil.getScreenH(this);

        ObjectAnimator animator=ObjectAnimator.ofFloat(mViewTemp,"scaleY",1f,(float) h);
        animator.setStartDelay(500);
        animator.setDuration(1000);

        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                mViewTemp.setBackgroundColor(getResources().getColor(R.color.white));
            }

            @Override
            public void onAnimationEnd(Animator animation) {

                mViewTemp.setVisibility(View.GONE);
                mCoordinatorLayout.setVisibility(View.VISIBLE);
                initFragment();
            }
        });

        animator.start();

    }

    private Bitmap getViewImageCache(View view) {


        view.setDrawingCacheEnabled(true);
        //绘制缓存
        view.buildDrawingCache();

        Bitmap bitmap = view.getDrawingCache();

        if (bitmap == null)
            return null;


        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());

        view.destroyDrawingCache();

        return bitmap;


    }

    private void initFragment(){
        AppDetailFragment fragment=new AppDetailFragment(mAppInfo.getId());
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.add(R.id.view_content,fragment);
        transaction.commitAllowingStateLoss();
    }
}
