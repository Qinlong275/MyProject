package com.qinlong275.android.monitor;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.qinlong275.android.monitor.client.connection.Client;
import com.qinlong275.android.monitor.client.connection.ClientOperations;
import com.qinlong275.android.monitor.client.fileOperation.FileControlMode;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by 秦龙 on 2017/10/23.
 */

public class FileFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "FileFragment";
    private TextView mTitle;
    private List<String> mUrlList;
    private Button mBackButton;
    private ProgressBar mProgressBar;
    private FloatingActionButton mFloatingButton;
    private List<File> mFileList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private FileAdapter mFileAdapter;
    private ArrayList<String> mItemlist;
    private String mControlState = null;
    private Dialog mDialog;
    private int currentLevel = 0;
    private ProgressDialog progressDialog;
    public static FileControlMode mFileControlMode;
    private String myState = "go";
    private AlertDialog.Builder dialog;
    private boolean isSuperClient = false;
    private String myDevice;
    private View dialogView;
    private MenuItem mMenuItem;

    //选择本机
    private List<File> mPhoneFileList = new ArrayList<>();
    private RecyclerView mPhoneRecyclerView;
    private PhoneFileAdapter mPhoneFileAdapter;
    private int phoneCurrentevel = 0;
    private ArrayList<String> mPhoneItemlist;
    private FileControlMode mPhoneFileControlMode;
    private String myPhoneState = "go";
    private AlertDialog.Builder phoneFileDialog;
    private View phoneDialogview;
    private List<String> mPhoneUrlList = new ArrayList<>();
    private ImageButton phoneCardViewBack;
    private Object[] mPhoneobject;
    private String backmode = null;

    private FileTransferService.FileTransferBinder mFileTransferBinder;
    private String directoryA;
    private String directoryB;
    private String transferFileName;
    private boolean isSuperControl = false;

    private ServiceConnection mFileTransferServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mFileTransferBinder = (FileTransferService.FileTransferBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.file_fragment_edit, null);
        phoneDialogview = inflater.inflate(R.layout.choose_phone_file, null);

        Intent intent = new Intent(getActivity(), FileTransferService.class);
        getActivity().startService(intent);//启动服务
        getActivity().bindService(intent, mFileTransferServiceConnection, BIND_AUTO_CREATE);//绑定服务


        TextView phoneFileText = (TextView) phoneDialogview.findViewById(R.id.phone_text);
        phoneFileText.setText("an_" + QueryPreference.getStoredString(getActivity(), "userName"));
        ImageButton cardViewHome = (ImageButton) phoneDialogview.findViewById(R.id.choose_home);
        cardViewHome.setOnClickListener(this);
        phoneCardViewBack = (ImageButton) phoneDialogview.findViewById(R.id.choose_back);
        phoneCardViewBack.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        mPhoneRecyclerView = (RecyclerView) phoneDialogview.findViewById(R.id.phone_recyclerView);
        mPhoneRecyclerView.setLayoutManager(layoutManager);
        mPhoneFileAdapter = new PhoneFileAdapter(mPhoneFileList);
        mPhoneRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mPhoneRecyclerView.addItemDecoration(new MyItemDecoration());
        mPhoneRecyclerView.setAdapter(mPhoneFileAdapter);

        phoneFileDialog = new AlertDialog.Builder(getActivity());
        phoneFileDialog.setView(phoneDialogview);
        phoneFileDialog.setCancelable(false);
        phoneFileDialog.setNegativeButton("取消此次操作", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((ViewGroup) phoneDialogview.getParent()).removeView(phoneDialogview);//重要
                mPhoneUrlList.clear();
                phoneCurrentevel = 0;
                myPhoneState = "go";

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (mPhoneFileControlMode!=null){
                            mPhoneFileControlMode.exit();
                        }
                    }
                }).start();

            }
        });


        final EditText editText = (EditText) dialogView.findViewById(R.id.editText);
        dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("权限获取");
        dialog.setMessage("请输入密码，进入超级用户状态");
        dialog.setView(dialogView);

        dialog.setCancelable(false);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String password = editText.getText().toString();
                if (password.contains("weizhao333")) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Client.toBeSuperClient("weizhao333");
                            isSuperClient = Client.isSuperClient;

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (isSuperClient) {
                                        if (password.equals("0miweizhao333")) {
                                            isSuperControl = true;
                                            Message msg=new Message();
                                            msg.obj="您已进入开发者模式";
                                            ConnectActivity.mHandler.sendMessage(msg);
                                            msg=null;
                                        }
                                        mFileTransferBinder.toSuperrClient();
                                        QueryPreference.setStoredBoolean(getActivity(),"isSuperClient",true);
                                        mMenuItem.setIcon(R.drawable.ic_menu_b);
                                    } else {
                                        QueryPreference.setStoredBoolean(getActivity(),"isSuperClient",false);
                                        mMenuItem.setIcon(R.drawable.ic_menu_a);
                                    }
                                }
                            });
                        }
                    }).start();

                    ((ViewGroup) dialogView.getParent()).removeView(dialogView);
                } else {
                    ((ViewGroup) dialogView.getParent()).removeView(dialogView);
                    Toast.makeText(getActivity(), "您的密码错误！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((ViewGroup) dialogView.getParent()).removeView(dialogView);//重要
            }
        });

        //连接请求数据

        mUrlList = new ArrayList<>();
        new RequestFileTask("").execute();
        Log.i(TAG, "creatre");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_file, container, false);

        mTitle = (TextView) view.findViewById(R.id.title_text);
        mBackButton = (Button) view.findViewById(R.id.back_button);
        mBackButton.setOnClickListener(this);
        mBackButton.setVisibility(View.GONE);
        mFloatingButton = (FloatingActionButton) view.findViewById(R.id.floatbar);
        mFloatingButton.setOnClickListener(this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(layoutManager);
        mFileAdapter = new FileAdapter(mFileList);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new MyItemDecoration());
        mRecyclerView.setAdapter(mFileAdapter);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                myState = "back";
                //刷新RecyclerView数据
                new RequestFileTask(mUrlList.get(mUrlList.size() - 2)).execute();

                break;
            case R.id.floatbar:
                showAlertDialog("你是否要进入控制台");
                break;
            case R.id.choose_back:
                myPhoneState = "back";
                new RequestPhoneFileTask(mPhoneUrlList.get(mPhoneUrlList.size() - 2)).execute();
                break;
            case R.id.choose_home:
                backmode = "home";
                showPhone();
                break;
            default:
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.file_menu, menu);
        mMenuItem = menu.findItem(R.id.control);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.control:
                if (isSuperClient == false) {
                    //升级超级用户
                    dialog.show();
                } else {
                    //退出超级用户
                    AlertDialog.Builder backdialog = new AlertDialog.Builder(getActivity());
                    backdialog.setTitle("退出权限");
                    backdialog.setMessage("您确定要退出超级用户状态么？");
                    backdialog.setCancelable(false);
                    backdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Client.exitSuperClient();
                                    isSuperClient = Client.isSuperClient;
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (isSuperClient) {
                                                mMenuItem.setIcon(R.drawable.ic_menu_b);
                                            } else {
                                                if (isSuperControl) {
                                                    isSuperControl = false;
                                                    mFileTransferBinder.exitSuperClient();

                                                    Toast.makeText(getActivity(), "您已退出开发者模式", Toast.LENGTH_SHORT).show();
                                                }
                                                QueryPreference.setStoredBoolean(getActivity(),"isSuperClient",false);
                                                mMenuItem.setIcon(R.drawable.ic_menu_a);
                                            }
                                        }
                                    });
                                }
                            }).start();
                        }
                    });

                    backdialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    backdialog.show();
                }
        }
        return super.onOptionsItemSelected(item);

    }

    private class PhoneViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mPhonefileName;
        File myFile;
        private ImageView mPhoneFileView;
        private ImageView mPhoneFileButton;
        private Object[] mObject;

        public PhoneViewHolder(View view) {
            super(view);
            mPhoneFileView = (ImageView) view.findViewById(R.id.phone_fileview);
            mPhonefileName = (TextView) view.findViewById(R.id.phonre_file_name);
            mPhoneFileButton = (ImageView) view.findViewById(R.id.mphone_fileButton);
            mPhoneFileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //开始上传或下载

                    if (mControlState.equals("upload")) {
                        directoryA = myFile.getId();
                        transferFileName = myFile.getName();
                        Log.d(TAG, directoryA + " " + transferFileName);
                        mFileTransferBinder.startTransfer(directoryA, directoryB, transferFileName, mFileControlMode, "upload");
                    } else {
                        directoryB = myFile.getId();
                        mFileTransferBinder.startTransfer(directoryA, directoryB, transferFileName, mFileControlMode, "download");
                    }

                    ((ViewGroup) phoneDialogview.getParent()).removeView(phoneDialogview);
                    mDialog.dismiss();//关闭phoneDialog
                    mPhoneUrlList.clear();
                    phoneCurrentevel = 0;
                    myPhoneState = "go";

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (mPhoneFileControlMode!=null){
                                mPhoneFileControlMode.exit();
                            }
                        }
                    }).start();

                }
            });
            itemView.setOnClickListener(this);
        }

        public void bindFile(File file) {
            myFile = file;
            mPhonefileName.setText(file.getName());
            //根据文件的名称动态改变文件的图片样式；
            if (myFile.getName().indexOf(".") != -1) {
                mPhoneFileView.setImageResource(R.drawable.ic_phone_file);
                if (mControlState.equals("upload")) {
                    mPhoneFileButton.setVisibility(View.VISIBLE);
                } else {
                    mPhoneFileButton.setVisibility(View.INVISIBLE);
                }
            } else {
                mPhoneFileView.setImageResource(R.drawable.ic_phone_directory);
                if (mControlState.equals("upload")) {
                    mPhoneFileButton.setVisibility(View.INVISIBLE);
                } else {
                    mPhoneFileButton.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onClick(View v) {
            myPhoneState = "go";
            //刷新显示的数据
            new RequestPhoneFileTask(myFile.getId()).execute();
        }
    }

    private class PhoneFileAdapter extends RecyclerView.Adapter<PhoneViewHolder> {

        private List<File> mPhoneFileListlist;

        public PhoneFileAdapter(List<File> fileList) {
            mPhoneFileListlist = fileList;
        }

        @Override
        public PhoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.phone_file_item, parent, false);
            final PhoneViewHolder holder = new PhoneViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(PhoneViewHolder holder, int position) {
            File file = mPhoneFileListlist.get(position);
            holder.bindFile(file);
        }

        @Override
        public int getItemCount() {
            return mPhoneFileListlist.size();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView fileName;
        File myFile;
        private ImageView mFileView;
        private ImageView mFileButton;
        private Object[] mObject;

        public ViewHolder(View view) {
            super(view);
            mFileView = (ImageView) view.findViewById(R.id.fileview);
            fileName = (TextView) view.findViewById(R.id.file_name);
            mFileButton = (ImageView) view.findViewById(R.id.mFileButton);
            mFileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isSuperControl==true){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String result = "abc";
                                mPhoneobject = Client.clientOperations.fileMode("an_" +
                                        QueryPreference.getStoredString(getActivity(), "userName"));
                                if (mPhoneobject != null) {
                                    result = (String) mPhoneobject[0];
                                }
                                if (result.equals("success")) {
                                    mPhoneFileControlMode = (FileControlMode) mPhoneobject[1];
                                    mPhoneUrlList.add("");
                                } else {
                                    Message msg=new Message();
                                    msg.obj=result;
                                    ConnectActivity.mHandler.sendMessage(msg);
                                    msg=null;
                                }
                            }
                        }).start();
                    }

                    if (isSuperClient == false) {
                        //获取权限升级为超级用户
                        Toast.makeText(getActivity(), "请先点击菜单栏获取权限成为超级用户", Toast.LENGTH_SHORT).show();
                    } else {
                        if (myFile.getName().indexOf(".") != -1) {
                            //下载此文件
                            if (isSuperControl == true) {
                                mControlState = "download";
                                mDialog = phoneFileDialog.show();
                                transferFileName = myFile.getName();
                                directoryA = myFile.getId();
                                showPhone();
                            } else {
                                Toast.makeText(getActivity(), "下载了", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            //上传文件到此目录
                            //开始上传
                            if (isSuperControl == true) {
                                directoryB = myFile.getId();
                                mControlState = "upload";
                                Log.d(TAG, directoryB);
                                mDialog = phoneFileDialog.show();
                                showPhone();
                            } else {
                                Toast.makeText(getActivity(), "上传了", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                }
            });
            itemView.setOnClickListener(this);
        }

        public void bindFile(File file) {
            myFile = file;
            fileName.setText(file.getName());
            //根据文件的名称动态改变文件的图片样式；
            if (myFile.getName().indexOf(".") != -1) {
                mFileView.setImageResource(R.drawable.ic_file);
                mFileButton.setImageResource(R.drawable.ic_action_download);
            } else {
                mFileView.setImageResource(R.drawable.ic_action_fileview);
                if (currentLevel > 1) {
                    mFileButton.setVisibility(View.VISIBLE);
                    mFileButton.setImageResource(R.drawable.ic_action_upload);
                } else {
                    mFileButton.setVisibility(View.INVISIBLE);
                }
            }
        }

        @Override
        public void onClick(View v) {
            myState = "go";

            //刷新显示的数据
            if (currentLevel == 1) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mObject = Client.clientOperations.fileMode(myFile.getName());
                        if (mObject!=null){
                            String result = (String) mObject[0];
                            if (result.equals("success")) {
                                mFileControlMode = (FileControlMode) mObject[1];
                                mFileTransferBinder.setFileControlMode(mFileControlMode,myFile.getName());
                                if (myFile.getName().split("_")[0].equals("an")) {
                                    myDevice = "Android";
                                    //在主线程更新界面
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            showAndroid();
                                        }
                                    });

                                    mUrlList.add("");
                                } else {
                                    myDevice = "Computer";
                                    new RequestFileTask("").execute();
                                }
                            } else {
                                Message msg=new Message();
                                msg.obj=result;
                                ConnectActivity.mHandler.sendMessage(msg);
                                msg=null;
                            }
                        }else {
                            Message msg=new Message();
                            msg.obj= "网络出现问题请稍后重试";
                            ConnectActivity.mHandler.sendMessage(msg);
                            msg=null;
                        }

                    }
                }).start();
            } else {
                new RequestFileTask(myFile.getId()).execute();
            }
        }
    }

    private class FileAdapter extends RecyclerView.Adapter<ViewHolder> {

        private List<File> mFileListlist;

        public FileAdapter(List<File> fileList) {
            mFileListlist = fileList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_item, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            File file = mFileListlist.get(position);
            holder.bindFile(file);
        }

        @Override
        public int getItemCount() {
            return mFileListlist.size();
        }
    }

    private class RequestFileTask extends AsyncTask<Void, Void, ArrayList<String>> {
        private String myPath;

        public RequestFileTask(String myPath) {
            this.myPath = myPath;
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            try {
                if (myPath.equals("")) {
                    if (currentLevel == 0) {
                        mItemlist = Client.showClients();
                    } else if (currentLevel == 3 && myDevice.equals("Android")) {
                        mItemlist = new ArrayList<>();
                        mItemlist.add("android");
                    } else if (currentLevel == 2) {
                        mFileControlMode.exit();
                        mItemlist = Client.showClients();
                    } else {
                        mItemlist = mFileControlMode.getDir("path root:");
                    }
                } else {
                    mItemlist = mFileControlMode.getDir("path " + myPath);
                }
                return mItemlist;
            } catch (IOException io) {
                Log.e(TAG, "Failed to fetch URL: " + io);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Sorry,你的连接请求失败，请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> mArrayList) {
            if (mArrayList != null && mArrayList.size() != 0) {

                if (mArrayList.get(0).equals("android")) {
                    showAndroid();
                    return;
                } else {
                    if (myState.equals("go")) {
                        currentLevel++;
                        mUrlList.add(myPath);
                    } else {
                        mUrlList.remove(mUrlList.size() - 1);
                        currentLevel--;
                    }
                }
                paseArraylist(mArrayList);
                updateFileRecyclerView();
            } else {
                Toast.makeText(getActivity(), "你的请求错误！可能是没有权限或者为空目录", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private class RequestPhoneFileTask extends AsyncTask<Void, Void, ArrayList<String>> {
        private String myPath;

        public RequestPhoneFileTask(String myPath) {
            this.myPath = myPath;
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            try {
                if (myPath.equals("")) {
                    mPhoneItemlist = new ArrayList<>();
                    mPhoneItemlist.add("phone");
                } else {
                    if(mPhoneFileControlMode!=null){
                        mPhoneItemlist = mPhoneFileControlMode.getDir("path " + myPath);
                    }else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Sorry,你的连接请求失败，请稍后重试", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
                return mPhoneItemlist;
            } catch (IOException io) {
                Log.e(TAG, "Failed to fetch URL: " + io);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Sorry,你的连接请求失败，请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> mArrayList) {
            if (mArrayList != null && mArrayList.size() != 0) {

                if (mArrayList.get(0).equals("phone")) {
                    showPhone();
                    return;
                } else {
                    if (myPhoneState.equals("go")) {
                        phoneCurrentevel++;
                        mPhoneUrlList.add(myPath);
                    } else {
                        mPhoneUrlList.remove(mPhoneUrlList.size() - 1);
                        phoneCurrentevel--;
                    }
                }
                pasePhoneArraylist(mArrayList);
                updatePhoneFileRecyclerView();
            } else {
                Toast.makeText(getActivity(), "你的请求错误！可能是没有权限或者为空目录", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void updateFileRecyclerView() {
        mFileAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(0);
        if (currentLevel > 1) {
            mBackButton.setVisibility(View.VISIBLE);
        } else {
            mBackButton.setVisibility(View.INVISIBLE);
        }

    }

    private void showAndroid() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("/sdcard");
        arrayList.add("/storage");
        paseArraylist(arrayList);
        if (myState.equals("go")) {
            currentLevel++;
        } else {
            mUrlList.remove(mUrlList.size() - 1);
            currentLevel--;
        }
        updateFileRecyclerView();
    }

    private void showPhone() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("/sdcard");
        arrayList.add("/storage");
        if (myPhoneState.equals("go")) {
            phoneCurrentevel++;
        } else {
            mPhoneUrlList.remove(mPhoneUrlList.size() - 1);
            phoneCurrentevel--;
        }

        if (backmode != null) {
            if (backmode.equals("home")) {
                mPhoneUrlList.clear();
                mPhoneUrlList.add("");
                phoneCurrentevel = 1;
                backmode = null;
            }
        }

        pasePhoneArraylist(arrayList);
        updatePhoneFileRecyclerView();
    }

    private void updatePhoneFileRecyclerView() {
        mPhoneFileAdapter.notifyDataSetChanged();
        mPhoneRecyclerView.scrollToPosition(0);
        if (phoneCurrentevel > 1) {
            phoneCardViewBack.setVisibility(View.VISIBLE);
        } else {
            phoneCardViewBack.setVisibility(View.INVISIBLE);
        }

    }

    private void showAlertDialog(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("获取权限");
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isSuperClient) {
                    Intent intent = new Intent(getActivity(), MonitorActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "请先点击菜单栏升级为超级用户", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }

    private void paseArraylist(ArrayList<String> message) {
        File file;
        mFileList.clear();
        for (int i = 0; i < message.size(); i++) {
            if (currentLevel < 3) {
                file = new File(message.get(i), message.get(i));
            } else {
                file = new File(paseItemName(message.get(i)), message.get(i));
            }
            mFileList.add(file);
        }
    }

    private void pasePhoneArraylist(ArrayList<String> message) {
        File file;
        mPhoneFileList.clear();
        for (int i = 0; i < message.size(); i++) {
            if (phoneCurrentevel == 1) {
                file = new File(message.get(i), message.get(i));
            } else {
                file = new File(paseItemName(message.get(i)), message.get(i));
            }
            mPhoneFileList.add(file);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "detach");

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (currentLevel > 1) {
                    mFileControlMode.exit();
                }
                Client.exit();
            }
        }).start();
        QueryPreference.setStoredBoolean(getActivity(),"isSuperClient",false);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mDialog != null) {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
    }

    //    @Override
//    public void onResume() {
//        super.onResume();
//        Log.d(TAG, "onresume");
//        if (currentLevel > 1) {
//            final Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    //执行耗时操作
//                    mFileControlMode.exit();
//                    try {
//                        mItemlist = Client.showClients();
//                        currentLevel = 1;
//                        paseArraylist(mItemlist);
//
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                updateFileRecyclerView();
//                            }
//                        });
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
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
//        }
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onactivitycreate");
    }


    private String paseItemName(String message) {
        String[] portion = message.split("/");
        return portion[portion.length - 1];
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(mFileTransferServiceConnection);
        //预留，活动结束退出前台服务
        mFileTransferBinder.stopService();
        QueryPreference.setStoredBoolean(getActivity(),"isSuperClient",false);
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

    public static void reSetFilecontrolMode(FileControlMode fileControlMode){
        mFileControlMode=fileControlMode;
    }

}
