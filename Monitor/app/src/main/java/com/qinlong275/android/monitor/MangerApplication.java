package com.qinlong275.android.monitor;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by 秦龙 on 2017/11/24.
 */

public class MangerApplication {

    private List<Activity> mActivityList=new LinkedList<Activity>();

    private static MangerApplication instance;

    private MangerApplication(){

    }

    public static MangerApplication getInstance(){
        if (instance==null){
            instance=new MangerApplication();
        }
        return instance;
    }

    public void addActivity(Activity activity){
        mActivityList.add(activity);
    }

    public void exit(){
        for (Activity activity:mActivityList){
            activity.finish();
        }
        System.exit(0);
    }
}
