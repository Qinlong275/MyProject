package com.qinlong275.android.cniaoplay.ui.adapter;

import com.qinlong275.android.cniaoplay.AppAplication;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qinlong275.android.cniaoplay.R;
import com.qinlong275.android.cniaoplay.bean.AppInfo;
import com.qinlong275.android.cniaoplay.bean.Banner;
import com.qinlong275.android.cniaoplay.bean.IndexBean;
import com.qinlong275.android.cniaoplay.common.Constant;
import com.qinlong275.android.cniaoplay.common.imageloader.ImageLoader;
import com.qinlong275.android.cniaoplay.common.util.ACache;
import com.qinlong275.android.cniaoplay.common.util.JsonUtils;
import com.qinlong275.android.cniaoplay.ui.activity.AppDetailActivity;
import com.qinlong275.android.cniaoplay.ui.activity.HotAppActivity;
import com.qinlong275.android.cniaoplay.ui.activity.HotGameActivity;
import com.qinlong275.android.cniaoplay.ui.activity.SubjectActivity;
import com.qinlong275.android.cniaoplay.ui.widget.BannerLayout;
import com.qinlong275.android.cniaoplay.ui.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zlc.season.rxdownload2.RxDownload;

/**
 * Created by 秦龙 on 2018/2/10.
 */

public class IndexMultilAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{


    public static final int TYPE_BANNER = 1;
    private static final int TYPE_ICON = 2;
    private static final int TYPE_APPS = 3;
    private static final int TYPE_GAMES = 4;



    private IndexBean mIndexBean;

    private LayoutInflater mLayoutInflater;

    private Context mContext;

    private RxDownload mRxDownload;

    private AppAplication mAplication;

    public IndexMultilAdapter(Context context, RxDownload rxDownload) {
        mContext = context;
        mAplication=(AppAplication)context.getApplicationContext();
        this.mRxDownload = rxDownload;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setData(IndexBean indexBean) {

        mIndexBean = indexBean;
    }


    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return TYPE_BANNER;
        } else if (position == 1) {
            return TYPE_ICON;
        } else if (position == 2) {
            return TYPE_APPS;
        } else if (position == 3) {
            return TYPE_GAMES;
        }

        return 0;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if (viewType == TYPE_BANNER) {

            return new BannerViewHolder(mLayoutInflater.inflate(R.layout.template_banner, parent, false));
        } else if (viewType == TYPE_ICON) {

            return new NavIconViewHolder(mLayoutInflater.inflate(R.layout.template_nav_icon, parent, false));

        }
        else if(viewType==TYPE_APPS){

            //因为嵌套recycleView,viewGroup设置为null,就会重新计算高度，否则可能显示不完整
            return  new AppViewHolder(mLayoutInflater.inflate(R.layout.template_recyleview_with_title, null, false),TYPE_APPS);
        }
        else if(viewType==TYPE_GAMES){

            return  new AppViewHolder(mLayoutInflater.inflate(R.layout.template_recyleview_with_title, null, false),TYPE_GAMES);
        }


        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (position == 0) {

            BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;

            List<Banner> banners =  mIndexBean.getBanners();
            List<String> urls = new ArrayList<>(banners.size());

            for (Banner banner : banners){

                urls.add(banner.getThumbnail());
            }

            bannerViewHolder.mBanner.setViewUrls(urls);

            bannerViewHolder.mBanner.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
                @Override
                public void onItemClick(int position) {
//                    banners.get(position)
                }
            });
        }
        else if (position==1){

            NavIconViewHolder bannerViewHolder = (NavIconViewHolder) holder;

            bannerViewHolder.mLayoutHotApp.setOnClickListener(this);
            bannerViewHolder.mLayoutHotGame.setOnClickListener(this);
            bannerViewHolder.mLayoutHotSubject.setOnClickListener(this);

        }
        else {
            AppViewHolder viewHolder = (AppViewHolder) holder;



            final AppInfoAdapter appInfoAdapter =  AppInfoAdapter.builder()
                    .showBrief(true)
                    .showCategoryName(false)
                    .showPosition(false)
                    .rxDownload(mRxDownload)
                    .build();

            if(viewHolder.type==TYPE_APPS){
                viewHolder.mText.setText("热门应用");
                appInfoAdapter.addData(mIndexBean.getRecommendApps());
                //缓存下来，在首页中间的按钮点击后页面获取
                ACache.get(mContext).put(Constant.HOT_APP, JsonUtils.toJson(mIndexBean.getRecommendApps()));
            }
            else{
                viewHolder.mText.setText("热门游戏");
                appInfoAdapter.addData(mIndexBean.getRecommendGames());
                //缓存下来，在首页中间的按钮点击后页面获取
                ACache.get(mContext).put(Constant.HOT_GAME, JsonUtils.toJson(mIndexBean.getRecommendGames()));
            }

            viewHolder.mRecyclerView.setAdapter(appInfoAdapter);

            viewHolder.mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                    AppInfo appInfo=appInfoAdapter.getItem(position);
                    mAplication.setView(view);
                    Intent intent=new Intent(mContext, AppDetailActivity.class);
                    //要实现serializable接口的
                    intent.putExtra("appinfo",appInfo);
                    mContext.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.layout_hot_app){
            mContext.startActivity(new Intent(mContext, HotAppActivity.class));
        }
        else if(v.getId() == R.id.layout_hot_subject){

            mContext.startActivity(new Intent(mContext, SubjectActivity.class));
        }
        else if (v.getId()==R.id.layout_hot_game){
            mContext.startActivity(new Intent(mContext, HotGameActivity.class));
        }
    }


    class BannerViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.banner)
        BannerLayout mBanner;

        public BannerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            mBanner.setImageLoader(new ImgLoader());
        }
    }

    class NavIconViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.layout_hot_app)
        LinearLayout mLayoutHotApp;
        @BindView(R.id.layout_hot_game)
        LinearLayout mLayoutHotGame;
        @BindView(R.id.layout_hot_subject)
        LinearLayout mLayoutHotSubject;

        NavIconViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class AppViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.text)
        TextView mText;
        @BindView(R.id.recycler_view)
        RecyclerView mRecyclerView;


        int type;

        public AppViewHolder(View itemView, int type) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.type = type;

            initRecyclerView();


        }

        private void initRecyclerView() {

            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext) {

                //不允许垂直滚动
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });

            DividerItemDecoration itemDecoration = new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL_LIST);

            mRecyclerView.addItemDecoration(itemDecoration);
        }
    }

    class  ImgLoader implements BannerLayout.ImageLoader{


        @Override
        public void displayImage(Context context, String path, ImageView imageView) {
            ImageLoader.load(path,imageView);
        }
    }
}
