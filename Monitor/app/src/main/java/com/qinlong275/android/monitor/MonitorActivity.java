package com.qinlong275.android.monitor;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.qinlong275.android.monitor.client.connection.Client;
import com.qinlong275.android.monitor.client.controlMode.ControlOtherMode;
import com.qinlong275.android.monitor.client.fileOperation.FileControlMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MonitorActivity extends AppCompatActivity  {

    private final String TAG = "MonitorActivity";
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private ClientAdapter mClientAdapter;
    private List<String> mClientItemList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        MangerApplication.getInstance().addActivity(this);
        mRecyclerView=(RecyclerView)findViewById(R.id.monitorRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration(){
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0,0,0,100);
            }
        });
        new MonitorTask().execute();
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView itemText;
        String itemName;
        private ImageView mItemView;
        private Object[]mObject;

        public ViewHolder(View view) {
            super(view);
            mItemView = (ImageView) view.findViewById(R.id.itemImageView);
            itemText = (TextView) view.findViewById(R.id.itemName);
            itemView.setOnClickListener(this);
        }

        public void bindClient(String item) {
            itemName=item;
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
                        Intent intent=new Intent(MonitorActivity.this,CommandActivity.class);
                        intent.putExtra("clientName",itemName);
                        startActivity(intent);
        }
    }

    private class ClientAdapter extends RecyclerView.Adapter<ViewHolder> {

        private List<String> mClientList;

        public ClientAdapter(List<String> clientList) {
            mClientList=clientList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.monitor_item, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String item = mClientList.get(position);
            holder.bindClient(item);
        }

        @Override
        public int getItemCount() {
            return mClientList.size();
        }
    }


    private void showAlertDialog(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MonitorActivity.this);
        dialog.setTitle("确认连接");
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mProgressBar.setVisibility(View.VISIBLE);
                new MonitorTask().execute();
            }
        });
        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }

    private class MonitorTask extends AsyncTask<Void, Void, List<String>> {
        private List<String> item;
        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                item=Client.showClients();
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
            }else {
                Toast.makeText(MonitorActivity.this, "你的请求错误！请稍后尝试", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateRecyclerView(List<String> list) {
        mClientItemList=list;
        mClientAdapter=new ClientAdapter(mClientItemList);
        mRecyclerView.setAdapter(mClientAdapter);
        mClientAdapter.notifyDataSetChanged();
    }

}
