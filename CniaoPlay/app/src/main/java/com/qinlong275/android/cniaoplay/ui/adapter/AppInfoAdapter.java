package com.qinlong275.android.cniaoplay.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.qinlong275.android.cniaoplay.R;
import com.qinlong275.android.cniaoplay.bean.AppInfo;
import com.qinlong275.android.cniaoplay.common.Constant;
import com.qinlong275.android.cniaoplay.common.imageloader.ImageLoader;
import com.qinlong275.android.cniaoplay.ui.widget.DownloadButtonConntroller;
import com.qinlong275.android.cniaoplay.ui.widget.DownloadProgressButton;


import zlc.season.rxdownload2.RxDownload;


public class AppInfoAdapter extends BaseQuickAdapter<AppInfo,BaseViewHolder> {

    String baseImgUrl ="http://file.market.xiaomi.com/mfc/thumbnail/png/w150q80/";

    private DownloadButtonConntroller mDownloadButtonConntroller;

    private Builder mBuilder;

    private AppInfoAdapter(Builder builder) {
        //子项的布局，有默认
        super(builder.layoutId);

        this.mBuilder = builder;

        //RxDownload是View传来的
        mDownloadButtonConntroller=new DownloadButtonConntroller(builder.mRxDownload);

        openLoadAnimation();
    }


    public static Builder builder(){

        return new Builder();
    }
    @Override
    protected void convert(BaseViewHolder helper, AppInfo item) {

        ImageLoader.load(Constant.BASE_IMG_URL+item.getIcon(), (ImageView) helper.getView(R.id.img_app_icon));
        helper.setText(R.id.txt_app_name,item.getDisplayName());

        helper.addOnClickListener(R.id.btn_download);
        View viewBtn  = helper.getView(R.id.btn_download);
        if (viewBtn instanceof  DownloadProgressButton){

            DownloadProgressButton btn = (DownloadProgressButton) viewBtn;
            //处理具体的一个App的下载事件
            mDownloadButtonConntroller.handClick(btn,item);
        }


        TextView textViewBrief = helper.getView(R.id.txt_brief);
        if(mBuilder.isUpdateStatus){

            //可扩展的TextView
            ExpandableTextView viewChangeLog =helper.getView(R.id.view_changelog);
            viewChangeLog.setText(item.getChangeLog());


            if(textViewBrief !=null) {
                textViewBrief.setVisibility( View.VISIBLE);
                textViewBrief.setText("v"+item.getVersionName() +  "  " + (item.getApkSize() / 1014 / 1024) +"Mb");
            }


        }
        else{


            TextView txtViewPosition = helper.getView(R.id.txt_position);
            if(txtViewPosition !=null) {
                txtViewPosition.setVisibility(mBuilder.isShowPosition ? View.VISIBLE : View.GONE);
                txtViewPosition.setText((item.getPosition() + 1) + " .");
            }


            TextView textViewCategoryName = helper.getView(R.id.txt_category);
            if(textViewCategoryName !=null) {
                textViewCategoryName.setVisibility(mBuilder.isShowCategoryName ? View.VISIBLE : View.GONE);
                textViewCategoryName.setText(item.getLevel1CategoryName());
            }


            if(textViewBrief !=null) {
                textViewBrief.setVisibility(mBuilder.isShowBrief ? View.VISIBLE : View.GONE);
                textViewBrief.setText(item.getBriefShow());
            }
            TextView textViewSize = helper.getView(R.id.txt_apk_size);
            if(textViewSize !=null){
                textViewSize.setText((item.getApkSize() / 1014 / 1024) +"Mb");
            }

        }
    }






    //建造者模式
    public static class  Builder{

        //设置默认的布局，可以修改
        private int layoutId=R.layout.template_appinfo;

        private boolean isShowPosition;
        private boolean isShowCategoryName;
        private boolean isShowBrief;
        private boolean isUpdateStatus;

        private RxDownload mRxDownload;

        public Builder showPosition(boolean b){

            this.isShowPosition =b;
            return this;
        }


        public Builder showCategoryName(boolean b){

            this.isShowCategoryName =b;
            return this;
        }


        public Builder showBrief(boolean b){

            this.isShowBrief =b;
            return this;
        }

        public AppInfoAdapter build(){


            return  new AppInfoAdapter(this);
        }

        public Builder layout(int resId){
            this.layoutId = resId;
            return this;
        }

        public Builder rxDownload(RxDownload rxDownload){
            this.mRxDownload = rxDownload;
            return this;
        }

        public Builder updateStatus(boolean b){
            this.isUpdateStatus = b;
            return this;
        }



    }

}
