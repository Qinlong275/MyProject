package com.qinlong275.android.monitor;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ConnectActivity extends SingleFragmentActivity {

    //在一些类中的子线程中使用Toast，可以发到这个主线程的Handler解决，防止子线程Loop太多带来的各种问题
    public static Handler mHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            String str=(String)msg.obj;
            Toast.makeText(MyApplication.getContext(),str,Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected Fragment createFragment() {
        return ConnectFragment.newInstance();
    }
}
