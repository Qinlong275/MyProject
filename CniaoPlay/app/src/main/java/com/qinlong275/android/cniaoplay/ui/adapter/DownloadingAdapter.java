package com.qinlong275.android.cniaoplay.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qinlong275.android.cniaoplay.R;
import com.qinlong275.android.cniaoplay.bean.AppInfo;
import com.qinlong275.android.cniaoplay.common.Constant;
import com.qinlong275.android.cniaoplay.common.imageloader.ImageLoader;
import com.qinlong275.android.cniaoplay.ui.widget.DownloadButtonConntroller;
import com.qinlong275.android.cniaoplay.ui.widget.DownloadProgressButton;

import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadRecord;

/**
 * 菜鸟窝http://www.cniao5.com 一个高端的互联网技能学习平台
 *
 * @author Ivan
 * @version V1.0
 * @Package com.cniao5.cniao5play.ui.adapter
 * @Description: ${TODO}(用一句话描述该文件做什么)
 * @date
 */

public class DownloadingAdapter extends BaseQuickAdapter<DownloadRecord,BaseViewHolder> {

    private DownloadButtonConntroller mDownloadButtonConntroller;

    public DownloadingAdapter(RxDownload rxDownload) {
        super(R.layout.template_app_downloading);
        mDownloadButtonConntroller = new DownloadButtonConntroller(rxDownload);

        openLoadAnimation();
    }



    @Override
    protected void convert(BaseViewHolder helper, DownloadRecord item) {


        AppInfo appInfo = mDownloadButtonConntroller.downloadRecord2AppInfo(item);


        ImageLoader.load(Constant.BASE_IMG_URL+appInfo.getIcon(), (ImageView) helper.getView(R.id.img_app_icon));
        helper.setText(R.id.txt_app_name,appInfo.getDisplayName());


        helper.addOnClickListener(R.id.btn_download);
        View viewBtn  = helper.getView(R.id.btn_download);

        if (viewBtn instanceof DownloadProgressButton){

            DownloadProgressButton btn = (DownloadProgressButton) viewBtn;
            mDownloadButtonConntroller.handClick(btn,item);
        }
    }

}
