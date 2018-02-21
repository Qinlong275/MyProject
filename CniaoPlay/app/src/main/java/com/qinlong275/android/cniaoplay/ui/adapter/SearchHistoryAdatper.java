package com.qinlong275.android.cniaoplay.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qinlong275.android.cniaoplay.R;

/**
 * 菜鸟窝http://www.cniao5.com 一个高端的互联网技能学习平台
 *
 * @author Ivan
 * @version V1.0
 * @Package com.cniao5.cniao5market.ui.adapter
 * @Description: ${TODO}(用一句话描述该文件做什么)
 * @date
 */

public class SearchHistoryAdatper extends BaseQuickAdapter<String,BaseViewHolder> {
    public SearchHistoryAdatper() {
        super(R.layout.template_search_history);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.btn,item);

        helper.addOnClickListener(R.id.btn);

    }
}
