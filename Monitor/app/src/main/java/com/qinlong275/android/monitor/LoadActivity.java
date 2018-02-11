package com.qinlong275.android.monitor;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

/**
 * Created by 秦龙 on 2017/10/17.
 */

public class LoadActivity extends AppCompatActivity {

    //time for picture display
    private static final int LOAD_DISPLAY_TIME = 1500;

    private Intent mIntent;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
        setContentView(R.layout.load);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                //Go to main activity, and finish load activity
                if (QueryPreference.getStoredString(LoadActivity.this, "userName")!=null){
                    mIntent=new Intent(LoadActivity.this,ConnectActivity.class);
                }else {
                    mIntent = new Intent(LoadActivity.this, UserActivity.class);
                }
                LoadActivity.this.startActivity(mIntent);
                LoadActivity.this.finish();
            }
        }, LOAD_DISPLAY_TIME);
    }
}
