package com.qinlong275.android.cniaoplay.ui.bean;

import android.support.v4.app.Fragment;

/**
 * Created by 秦龙 on 2018/2/1.
 */

public class FragmentInfo {
    private String title;
    private Class mFragment;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Class getFragment() {
        return mFragment;
    }

    public void setFragment(Class fragment) {
        mFragment = fragment;
    }

    public FragmentInfo(String title, Class fragment) {
        this.title = title;
        mFragment = fragment;
    }

    public FragmentInfo() {

    }
}
