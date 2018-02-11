package com.qinlong275.android.monitor;

import android.app.ActivityManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DialogActivity extends AppCompatActivity implements View.OnClickListener{

    private Button yesButton;
    private Button noButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        MangerApplication.getInstance().addActivity(this);
        yesButton=(Button)findViewById(R.id.dialog_yes);
        noButton=(Button)findViewById(R.id.dialog_no);
        yesButton.setOnClickListener(this);
        noButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_yes:
                //正常退出App
                MangerApplication.getInstance().exit();
                break;
            case R.id.dialog_no:
                finish();
                break;
            default:
                break;
        }
    }
}
