package com.qinlong275.android.monitor;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qinlong275.android.monitor.client.connection.Client;
import com.qinlong275.android.monitor.client.controlMode.ControlOtherMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandActivity extends AppCompatActivity {
    private TextView navTextView;

    public static final String TAG = "CommandActivity";
    private EditText inputText;
    private Button send;
    private DrawerLayout mDrawerLayout;
    private String clientText;
    private TextView mTextView;
    private String clientName;
    private int offset;
    private Object[] mObject;
    private ControlOtherMode mControlOtherMode;
    private RecyclerView navRecyclerview;
    private NavClientAdapter mNavClientAdapter;
    private List<String> mNavClientItemList = new ArrayList<>();
    private int ql = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command);
        clientName = getIntent().getStringExtra("clientName");
        mTextView = (TextView) findViewById(R.id.msg_textView);
        mTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        /*initText(clientName);*/
        navRecyclerview = (RecyclerView) findViewById(R.id.nav_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        navRecyclerview.setLayoutManager(layoutManager);
        navRecyclerview.setItemAnimator(new DefaultItemAnimator());
        navRecyclerview.addItemDecoration(new MyItemDecoration());

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();
//        new NavTask().execute();
        initControlOtherMode(clientName);

        clientText = QueryPreference.getStoredString(this, "userName");
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navTextView = (TextView) findViewById(R.id.nav_username);
        navTextView.setText(clientText);


        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String content = inputText.getText().toString();
                if (!"".equals(content)) {
                    inputText.setText("");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mControlOtherMode.sendCommand(content);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

//                    final Runnable runnable = new Runnable() {
//                        @Override
//                        public void run() {
//                            //执行耗时操作
//                            try {
//                                mControlOtherMode.sendCommand(content);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    };
//
//                    Thread myThread = new Thread() {
//                        public void run() {
//                            Looper.prepare();
//                            new Handler().post(runnable);//在子线程中直接去new 一个handler
//                            Looper.loop();//这种情况下，Runnable对象是运行在子线程中的，可以进行联网操作，但是不能更新UI
//                        }
//                    };
//                    myThread.start();

//                    mTextView.append(content+"\n");
//                    //自动移动到最下面一行
//                    offset=mTextView.getLineCount()*mTextView.getLineHeight();
//                    if (offset>mTextView.getHeight()){
//                        mTextView.scrollTo(0,offset-mTextView.getHeight());
//                    }
                }
            }
        });
    }
//    }
//    private void initText(String clientName){
//        mTextView.append("Monitor [版本 1.0.2]\n2017 Monior. 保留所有权利。\n\nMonitor:\\Users\\"+clientName+"\n");
//        offset=mTextView.getLineCount()*mTextView.getLineHeight();
//        if (offset>mTextView.getHeight()){
//            mTextView.scrollTo(0,offset-mTextView.getHeight());
//        }
//    }

    private class NavViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView itemText;
        String itemName;
        private ImageView mItemView;
        private Object[] mObject;

        public NavViewHolder(View view) {
            super(view);
            mItemView = (ImageView) view.findViewById(R.id.nav_itemImageView);
            itemText = (TextView) view.findViewById(R.id.nav_itemName);
            itemView.setOnClickListener(this);
        }

        public void bindClient(String item) {
            itemName = item;
            itemText.setText(item);
            //根据名称动态改变图片样式；
            if (itemName.split("_")[0].equals("an")) {
                //安卓设备
                mItemView.setImageResource(R.drawable.ic_phone);
            } else {
                //电脑设备
                mItemView.setImageResource(R.drawable.ic_computer);
            }
        }

        @Override
        public void onClick(View v) {
//            ql = 0;
//            //退出上一个controlmode,新建一个
//            final Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    //执行耗时操作
//
//                    if (mControlOtherMode != null) {
//                        try {
//                            if (ql == 0) {
//                                mControlOtherMode.exit();
//                                Log.d(TAG, "exit" + itemName);
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            };
//
//            Thread myThread = new Thread() {
//                public void run() {
//                    Looper.prepare();
//                    new Handler().post(runnable);//在子线程中直接去new 一个handler
//                    Looper.loop();//这种情况下，Runnable对象是运行在子线程中的，可以进行联网操作，但是不能更新UI
//                }
//            };
//            myThread.start();
            new changeTask(itemName).execute();
//            initControlOtherMode(itemName);
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mDrawerLayout.closeDrawers();
//                    mTextView.setText("");
//                }
//            });

        }
    }

    private class NavClientAdapter extends RecyclerView.Adapter<NavViewHolder> {

        private List<String> mClientList;

        public NavClientAdapter(List<String> clientList) {
            mClientList = clientList;
        }

        @Override
        public NavViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_itemview, parent, false);
            final NavViewHolder holder = new NavViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(NavViewHolder holder, int position) {
            String item = mClientList.get(position);
            holder.bindClient(item);
        }

        @Override
        public int getItemCount() {
            return mClientList.size();
        }
    }

    private void updateRecyclerView(List<String> list) {
        mNavClientItemList = list;
        mNavClientAdapter = new NavClientAdapter(mNavClientItemList);
        navRecyclerview.setAdapter(mNavClientAdapter);
        mNavClientAdapter.notifyDataSetChanged();
    }

    private void initControlOtherMode(final String navClientName) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mNavClientItemList = Client.showClients();
                    if (mNavClientItemList != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateRecyclerView(mNavClientItemList);
                            }
                        });
                    } else {
                        Message msg=new Message();
                        msg.obj="你的请求错误！请稍后尝试";
                        ConnectActivity.mHandler.sendMessage(msg);
                        msg=null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mObject = Client.clientOperations.controlOther(navClientName);
                if (mObject == null) {
                    Message msg=new Message();
                    msg.obj="网络问题请稍后重试";
                    ConnectActivity.mHandler.sendMessage(msg);
                    msg=null;
                    return;
                }
                String result = (String) mObject[0];
                if (result.equals("success")) {
                    ql = 1;
                    mControlOtherMode = (ControlOtherMode) mObject[1];
                    try {
                        InputStream responseIn = mControlOtherMode.resultTranIn;
                        final byte[] buffer = new byte[1024];
                        System.out.println("3123");
//                        mTextView.setText("\n");
                        while (true) {
                            //System.out.println("11123123");
                            int length = responseIn.read(buffer);
                            System.out.println(length);
                            final String resonse = new String(buffer, 0, length, "GBK");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mTextView.append(resonse);
                                    //自动移动到文字的最低端
                                    offset = mTextView.getLineCount() * mTextView.getLineHeight();
                                    if (offset > mTextView.getHeight()) {
                                        mTextView.scrollTo(0, offset - mTextView.getHeight() + mTextView.getLineHeight());
                                    }
                                }
                            });

                            if (length == -1)
                                break;
                            //ostrm_.write(buffer, 0, length);
                        }
                        System.out.print("网络断开333");

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.print("网络断开");
//                        try {
//                            mControlOtherMode.exit();
//                        } catch (IOException e1) {
//                            e1.printStackTrace();
//                        }
//                        CommandActivity.this.finish();
                    }
                } else {
                    Message msg=new Message();
                    msg.obj=result;
                    ConnectActivity.mHandler.sendMessage(msg);
                    msg=null;
                    CommandActivity.this.finish();
                }
            }
        }).start();

    }

    private class changeTask extends AsyncTask<Void, Void, Void>{

        private String itemName;
        public changeTask(String name){
            itemName=name;
        }
        @Override
        protected Void doInBackground(Void... params) {
            if (mControlOtherMode != null) {
                try {
                        mControlOtherMode.exit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            initControlOtherMode(itemName);
            mDrawerLayout.closeDrawers();
            mTextView.setText("");
        }
    }

    private class NavTask extends AsyncTask<Void, Void, List<String>> {
        private List<String> item;

        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                item = Client.showClients();
                return item;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            if (result != null) {
                updateRecyclerView(result);
            } else {
                Toast.makeText(CommandActivity.this, "你的请求错误！请稍后尝试", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "destroy");
        super.onDestroy();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mControlOtherMode != null) {
                        mControlOtherMode.exit();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}