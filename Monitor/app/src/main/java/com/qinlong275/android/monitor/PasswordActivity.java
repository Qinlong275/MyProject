package com.qinlong275.android.monitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordActivity extends AppCompatActivity {

    private Button mButton;
    private EditText mEditText;
    private String mPassWord="qinlong";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        MangerApplication.getInstance().addActivity(this);
        mButton=(Button)findViewById(R.id.passButton);
        mEditText=(EditText)findViewById(R.id.passEdit);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password=mEditText.getText().toString();
                if (password.equals("")){
                    Toast.makeText(PasswordActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                }else {
                    if (password.equals(mPassWord)){
                        //进入控制台Activity
                        Intent intent=new Intent(PasswordActivity.this,MonitorActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(PasswordActivity.this,"Sorry,您的密码有误，请重新输入",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
