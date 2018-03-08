package com.qinlong275.android.cniaoplay.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;
import com.qinlong275.android.cniaoplay.R;
import com.qinlong275.android.cniaoplay.bean.Category;
import com.qinlong275.android.cniaoplay.common.Constant;
import com.qinlong275.android.cniaoplay.common.imageloader.ImageLoader;


/**
 * 菜鸟窝http://www.cniao5.com 一个高端的互联网技能学习平台
 *
 * @author Ivan
 * @version V1.0
 * @Package com.cniao5.cniao5market.ui.adapter
 * @Description: ${TODO}(用一句话描述该文件做什么)
 * @date
 */

public class CategoryAdapter extends BaseQuickAdapter<Category,BaseViewHolder> {



    private Context mContext;


    public CategoryAdapter(Context context) {
        super(R.layout.template_category);
        mContext=context;
    }


    @Override
    protected void convert(BaseViewHolder helper, Category item) {

        //字体图标
        Drawable drawable=new IconicsDrawable(mContext)
                .icon(Ionicons.Icon.ion_ios_arrow_right)
                .color(Color.BLACK)
                .sizeDp(10);

        ((ImageView)helper.getView(R.id.text_image)).setImageDrawable(drawable);

        helper.setText(R.id.text_name,item.getName());

        ImageLoader.load(Constant.BASE_IMG_URL+item.getIcon(), (ImageView) helper.getView(R.id.img_icon));
    }

}
