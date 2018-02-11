package com.qinlong275.android.cniaoplay.ui.Animation;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by 秦龙 on 2018/2/7.
 */

public class PageAnimation implements ViewPager.PageTransformer{

    public static final String TAG = "simple_PagerTransform";

    @Override
    public void transformPage(View page, float position) {

        int width = page.getWidth();
        int height = page.getHeight();
        //這裏只對右邊的view做了操作
        if (position > 0 && position <= 1) {//right scorlling
            //position是1.0->0,但是沒有等於0
            Log.e(TAG, "right----position====" + position);
            //設置該view的X軸不動
            page.setTranslationX(-width * position);
            //設置縮放中心點在該view的正中心
            page.setPivotX(width / 2);
            page.setPivotY(height / 2);
            //設置縮放比例（0.0，1.0]
            page.setScaleX(1 - position);
            page.setScaleY(1 - position);

        } else if (position >= -1 && position < 0) {//left scrolling

        } else {//center

        }
    }

}
