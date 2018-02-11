package com.qinlong275.android.monitor;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class FileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        MangerApplication.getInstance().addActivity(this);
        Toolbar toolbar=(Toolbar)findViewById(R.id.head);
        setSupportActionBar(toolbar);
    }
}
