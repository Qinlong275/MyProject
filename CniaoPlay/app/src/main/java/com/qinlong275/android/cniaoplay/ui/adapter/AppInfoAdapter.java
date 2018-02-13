package com.qinlong275.android.cniaoplay.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qinlong275.android.cniaoplay.R;
import com.qinlong275.android.cniaoplay.bean.AppInfo;
import com.qinlong275.android.cniaoplay.common.Constant;
import com.qinlong275.android.cniaoplay.common.imageloader.ImageLoader;


/**
 * 菜鸟窝http://www.cniao5.com 一个高端的互联网技能学习平台
 *
 * @author Ivan
 * @version V1.0
 * @Package com.cniao5.cniao5play.ui.adapter
 * @Description: ${TODO}(用一句话描述该文件做什么)
 * @date
 */

public class AppInfoAdapter extends BaseQuickAdapter<AppInfo,BaseViewHolder> {

    String baseImgUrl ="http://file.market.xiaomi.com/mfc/thumbnail/png/w150q80/";


    private Builder mBuilder;

    private AppInfoAdapter(Builder builder) {
        super(builder.layoutId);

        this.mBuilder = builder;

        openLoadAnimation();
    }


    public static Builder builder(){

        return new Builder();
    }
    @Override
    protected void convert(BaseViewHolder helper, AppInfo item) {

        ImageLoader.load(Constant.BASE_IMG_URL+item.getIcon(), (ImageView) helper.getView(R.id.img_app_icon));
        helper.setText(R.id.txt_app_name,item.getDisplayName());


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

        TextView textViewBrief = helper.getView(R.id.txt_brief);
        if(textViewCategoryName !=null) {
            textViewBrief.setVisibility(mBuilder.isShowBrief ? View.VISIBLE : View.GONE);
            textViewBrief.setText(item.getBriefShow());
        }


        TextView textViewSize = helper.getView(R.id.txt_apk_size);

        if(textViewSize !=null){
            textViewSize.setText((item.getApkSize() / 1014 / 1024) +"Mb");
        }

    }






    //建造者模式
    public static class  Builder{

        private int layoutId=R.layout.template_appinfo;

        private boolean isShowPosition;
        private boolean isShowCategoryName;
        private boolean isShowBrief;

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
    }

}
