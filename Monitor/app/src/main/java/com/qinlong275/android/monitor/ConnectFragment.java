package com.qinlong275.android.monitor;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.qinlong275.android.monitor.client.connection.Client;

import java.io.IOException;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by 秦龙 on 2017/10/16.
 */

public class ConnectFragment extends Fragment {
    private EditText severEdit;
    private EditText terminalEdit;
    private CardView connection;
    private TextView mTextView;
    private CheckBox rememberPass;
    private static final String TAG = "ConnectionFragment";
    private ProgressDialog progressDialog;
    private boolean isApplicateSucceed;
    private Client mClient;
    private FileTransferService.FileTransferBinder mFileTransferBinder;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};


    public static ConnectFragment newInstance() {
        return new ConnectFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int permission = ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }

        Intent intent = new Intent(getActivity(), FileTransferService.class);
        getActivity().startService(intent);//启动服务
        getActivity().bindService(intent, mFileTransferServiceConnection, BIND_AUTO_CREATE);//绑定服务
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "获取权限成功");
                } else {
                    Toast.makeText(getActivity(), "你否认了权限", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private ServiceConnection mFileTransferServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mFileTransferBinder = (FileTransferService.FileTransferBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.connection_fragment, container, false);
        severEdit = (EditText) view.findViewById(R.id.server);
        terminalEdit = (EditText) view.findViewById(R.id.terminal);
        connection = (CardView) view.findViewById(R.id.login);
        rememberPass = (CheckBox) view.findViewById(R.id.remember_pass);
        mTextView = (TextView) view.findViewById(R.id.headText);
        mTextView.setText("哈喽,亲爱的 " + QueryPreference.getStoredString(getActivity(), "userName"));
        boolean isRemember = QueryPreference.getStoredChecked(getActivity(), "remember");
        if (isRemember) {
            //将账号和密码都设置到文本框里
            String server = QueryPreference.getStoredString(getActivity(), "server");
            String terminal = QueryPreference.getStoredString(getActivity(), "terminal");
            severEdit.setText(server);
            terminalEdit.setText(terminal);
            rememberPass.setChecked(true);
        }

        connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String server = severEdit.getText().toString();
                final String terminal = terminalEdit.getText().toString();
                if (!server.equals("")&&(!terminal.equals(""))) {
                    if (rememberPass.isChecked()) {  //检查复选框是否被选中
                        QueryPreference.setStoredString(getActivity(), "server", server);
                        QueryPreference.setStoredString(getActivity(), "terminal", terminal);
                        QueryPreference.setStoredBoolean(getActivity(), "remember", true);
                    }
                    showProgressDialog();
//                new ConnectionTask(server, terminal).execute();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            mFileTransferBinder.startConnect(QueryPreference.getStoredString(getActivity(), "userName"),
                                    server, Integer.parseInt(terminal), "mine00544");
                            if (Client.isApplicateSucceed) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        closeProgressDialog();
                                    }
                                });
                                Intent startIntent = new Intent(getActivity(), FileActivity.class);
                                startActivity(startIntent);
                                mFileTransferBinder.circulConnect(QueryPreference.getStoredString(getActivity(), "userName"),
                                        server, Integer.parseInt(terminal), "mine00544");
                            } else {

                                Message msg = new Message();
                                msg.obj = "请求失败，请重试";
                                ConnectActivity.mHandler.sendMessage(msg);
                                msg = null;
                            }
                        }
                    }).start();
                }else {
                    Toast.makeText(getActivity(), "输入为空，请重新输入", Toast.LENGTH_SHORT).show();
                }

            }

        });
        return view;
    }


    private class ConnectionTask extends AsyncTask<Void, Void, Boolean> {
        private String server;
        private String terminal;
        private Client mClient;

        public ConnectionTask(String server, String terminal) {
            this.server = server;
            this.terminal = terminal;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    mClient = new Client(QueryPreference.getStoredString(getActivity(), "userName"),
                            server, Integer.parseInt(terminal), "mine00544");
                    if (Client.isApplicateSucceed) {
                        isApplicateSucceed = true;
                    } else {
                        isApplicateSucceed = false;
                    }
                }
            }).start();

//            final Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    //执行耗时操作
//                    mClient = new Client(QueryPreference.getStoredString(getActivity(), "userName"),
//                            server, Integer.parseInt(terminal), "mine00544");
//                    if (Client.isApplicateSucceed) {
//                        isApplicateSucceed = true;
//                    } else {
//                        isApplicateSucceed = false;
//                    }
//                }
//            };
//
//            new Thread() {
//                public void run() {
//                    Looper.prepare();
//                    new Handler().post(runnable);//在子线程中直接去new 一个handler
//                    Looper.loop();//这种情况下，Runnable对象是运行在子线程中的，可以进行联网操作，但是不能更新UI
//                }
//            }.start();
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            if (isApplicateSucceed) {
                Intent intent = new Intent(getActivity(), FileActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "请求失败，请重试", Toast.LENGTH_SHORT).show();
            }
        }

    }

    //显示进度对话框
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("请稍等");
            progressDialog.setMessage("正在申请连接");
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    //关闭进度对话框
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(mFileTransferServiceConnection);
        //预留，活动结束退出前台服务
        mFileTransferBinder.stopService();
    }
}
