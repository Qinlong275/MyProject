package com.qinlong275.android.monitor;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 秦龙 on 2017/10/25.
 */

public class MyItemDecoration extends RecyclerView.ItemDecoration{
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //设置底部边距px
        outRect.set(0,0,0,2);
    }
}
