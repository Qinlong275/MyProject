package com.qinlong275.android.cniaoplay.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qinlong275.android.cniaoplay.R;
import com.qinlong275.android.cniaoplay.bean.AppInfo;
import com.qinlong275.android.cniaoplay.common.Constant;
import com.qinlong275.android.cniaoplay.common.imageloader.ImageLoader;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 秦龙 on 2018/2/5.
 */

public class RecommendAppAdapter extends RecyclerView.Adapter<RecommendAppAdapter.ViewHolder> {

    private Context mContext;
    private List<AppInfo> mDatas;

    private LayoutInflater mLayoutInflater;

    public RecommendAppAdapter(Context context, List<AppInfo> datas) {
        mDatas = datas;
        mLayoutInflater=LayoutInflater.from(context);
        mContext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.template_recomend_app, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AppInfo appInfo=mDatas.get(position);
        ImageLoader.load(Constant.BASE_IMG_URL+appInfo.getIcon(),holder.mImgIcon);
        holder.mTextTitle.setText(appInfo.getDisplayName());
        holder.mTextSize.setText(appInfo.getApkSize()/1024/1024+"MB");
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_icon)
        ImageView mImgIcon;
        @BindView(R.id.text_title)
        TextView mTextTitle;
        @BindView(R.id.text_size)
        TextView mTextSize;
        @BindView(R.id.btn_dl)
        Button mBtnDl;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
