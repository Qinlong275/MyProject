package com.qinlong275.android.cniaoplay.ui.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qinlong275.android.cniaoplay.R;
import com.qinlong275.android.cniaoplay.bean.Subject;
import com.qinlong275.android.cniaoplay.common.Constant;


/**
 * 菜鸟窝http://www.cniao5.com 一个高端的互联网技能学习平台
 *
 * @author Ivan
 * @version V1.0
 * @Package com.cniao5.cniao5market.ui.adapter
 * @Description: ${TODO}(用一句话描述该文件做什么)
 * @date
 */

public class SubjectAdapter extends BaseQuickAdapter<Subject,BaseViewHolder> {







    public SubjectAdapter() {
        super(R.layout.template_imageview);
    }

    @Override
    protected void convert(BaseViewHolder helper, Subject item) {


        ImageView imageView =  helper.getView(R.id.imageview);
        String url = Constant.BASE_IMG_URL+item.getMticon();
        Glide.with(mContext).load(url).into(imageView);

    }
}
