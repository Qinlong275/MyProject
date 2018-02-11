package com.qinlong275.android.monitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class UserActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton mImageButton;
    private EditText mNameText;
    private EditText mPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        MangerApplication.getInstance().addActivity(this);
        mImageButton=(ImageButton)findViewById(R.id.affirm);
        mNameText=(EditText)findViewById(R.id.nameEdit);
        mPasswordText=(EditText)findViewById(R.id.passwordEdit);
        mImageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.affirm:
                String myName=mNameText.getText().toString();
                String myPassword=mPasswordText.getText().toString();
                if(!myName.equals("")&&!myPassword.equals("")){
                    mNameText.setEnabled(false);
                    mPasswordText.setEnabled(false);
                    mImageButton.setBackgroundResource(R.drawable.ic_action_affirmdown);
                    QueryPreference.setStoredString(this,"userName",myName);
                    QueryPreference.setStoredString(this,"userPassword",myPassword);
                    Intent intent = new Intent(UserActivity.this, ConnectActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(this,"请输入用户名和密码！",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
