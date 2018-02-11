package com.qinlong275.android.monitor;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.qinlong275.android.monitor.client.connection.Client;
import com.qinlong275.android.monitor.client.fileOperation.FileControlMode;

import java.io.IOException;

public class FileTransferService extends Service {

    private FileControlMode mFileControlMode;
    private String mState;
    private FileTransferBinder mFileTransferBinder = new FileTransferBinder();
    private Client mClient;
    private String TAG = "fileSErvice";
    private boolean isSuperClient = false;
    private String muserName;
    private String mserver;
    private String mpassword;
    private int mport;
    private String mFileName;
    private Object[] mObject;
    private ResetFilecontrolMode mResetFilecontrolMode;


    public FileTransferService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        isSuperClient = QueryPreference.getStoredChecked(getBaseContext(), "isSuperClient");
        startForeground(1, getNotification("ZeroMeter运行中"));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        stopForeground(true);
        startForeground(1, getNotification("ZeroMeter运行中"));

        return super.onStartCommand(intent,flags,startId);
//        return START_STICKY;//这样设置后，显示关闭Service很难奏效，即使退出应用，关闭前台Service通知，Service还在工作
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mFileTransferBinder;
    }

    class FileTransferBinder extends Binder {
        public void startTransfer(String directoryA, String directoryB, String filename, FileControlMode fileControlMode, String state) {
            mFileControlMode = fileControlMode;
            mState = state;

            if (mState.equals("download")) {
                Toast.makeText(FileTransferService.this, "开始下载", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(FileTransferService.this, "开始上传", Toast.LENGTH_SHORT).show();
            }
            new FileTransferTask(directoryA, directoryB, filename).execute();
        }

        public void stopService() {
            stopForeground(true);
//            stopSelf();
        }

        public void startConnect(String userName, String server, int port, String password) {
            mClient = new Client(userName, server, port, password);
//            circulConnect(userName,server,port,password);
        }

        public void circulConnect(String userName, String server, int port, String password) {
            muserName = userName;
            mserver = server;
            mport = port;
            mpassword = password;


            while (true) {
                if (!Client.isApplicateSucceed) {

                    Message msg=new Message();
                    msg.obj="连接异常，复活中";
                    ConnectActivity.mHandler.sendMessage(msg);
                    msg=null;

                    Log.d(TAG, "复活Client用户中");

                    //复活方法第一种
//                    mClient=null;
//                    mClient=new Client(muserName,mserver,mport,mpassword);;
//                    if (isSuperClient){
//                        Client.toBeSuperClient("weizhao333");
//                    }

                    //比较两种哪一个被直接杀死整个应用的概率小

                    //复活方法第二种，貌似有相对大几率被整个app杀死
                    Client.resurgence();

                    if (mFileControlMode != null) {

                        //isAlive判断有问题
//                        while (!mFileControlMode.isAlive()){
                        mResetFilecontrolMode = null;
                        ResetFilecontrolMode resetFilecontrolMode = new ResetFilecontrolMode();
                        resetFilecontrolMode.execute();
                        mResetFilecontrolMode=resetFilecontrolMode;

                        //重点！！！之后优化要释放内存空间
                        //之前出现的两个ResetFilecontrolModetask同时运行导致应用退出问题可能没解决完
                        //测试应用，有很小的可能出现，应用服务没有关闭，但是复活机制不起作用了

//                            if (mFileControlMode.isAlive()){
//                                Log.d(TAG,"复活FilecontrolMode成功");
//                            }
//                        }
                    }

                    if (mFileControlMode != null) {
                        if (Client.isApplicateSucceed && mFileControlMode.isAlive()) {
                            Log.d(TAG, "复活成功");
                        }
                    }

                }
            }
        }

        public void toSuperrClient() {
            isSuperClient = true;
        }

        public void exitSuperClient() {
            isSuperClient = false;
        }

        public void setFileControlMode(FileControlMode mfileControlMode, String name) {
            mFileControlMode = mfileControlMode;
            mFileName = name;
        }
    }

    //复活FilecontrolMode
    private class ResetFilecontrolMode extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (mFileControlMode != null) {
                mFileControlMode.exit();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ResetFileControlMode();
        }
    }

    private void ResetFileControlMode() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                mObject = Client.clientOperations.fileMode(mFileName);
                if (mObject != null) {
                    String result = (String) mObject[0];
                    if (result.equals("success")) {
                        mFileControlMode = (FileControlMode) mObject[1];
                        FileFragment.reSetFilecontrolMode(mFileControlMode);
                        Log.d(TAG, "复活FilecontrolMode");

                    } else {
                        Log.d(TAG, result);
                    }
                } else {
                    Log.d(TAG, "网络出现问题请稍后重试");
                }
            }
        }).start();

    }

    private class FileTransferTask extends AsyncTask<Void, Void, Void> {

        private String directoryA;
        private String directoryB;
        private String fileName;

        public FileTransferTask(String a, String b, String fileName) {
            directoryA = a;
            directoryB = b;
            this.fileName = fileName;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (mState.equals("download")) {
                    mFileControlMode.download("download " + directoryA + " to " + directoryB + "/" + fileName);
                } else {
                    mFileControlMode.upload("upload " + directoryA + " to " + directoryB + "/" + fileName);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Message msg=new Message();
                msg.obj= "传输过程中出现错误，请稍后重试";
                ConnectActivity.mHandler.sendMessage(msg);
                msg=null;

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (mState.equals("download")) {
//                getNotificationManger().notify(2,getNotification("下载完成"));
            } else {
//                getNotificationManger().notify(2,getNotification("上传完成"));
            }
            super.onPostExecute(aVoid);
        }
    }

    private Notification getNotification(String title) {
        Intent intent = new Intent(this, DialogActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.logo);
        builder.setContentIntent(pi);
        builder.setContentTitle(title);
        return builder.build();
    }

    private NotificationManager getNotificationManger() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "ondestroy");
        super.onDestroy();
        stopForeground(true);
    }

}
