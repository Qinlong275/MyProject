package com.qinlong275.android.monitor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 秦龙 on 2017/8/2.
 */

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder>{
    private List<Msg> mMsglist;

    static class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout leftlayout;
        LinearLayout rightlayout;
        TextView leftMsg;
        TextView rightMsg;

        public ViewHolder(View view){
            super(view);
            leftlayout=(LinearLayout)view.findViewById(R.id.left_layout);
            rightlayout=(LinearLayout)view.findViewById(R.id.right_layout);
            leftMsg=(TextView)view.findViewById(R.id.left_msg);
            rightMsg=(TextView)view.findViewById(R.id.right_msg);
        }
    }

    public MsgAdapter(List<Msg> msgList){
        mMsglist=msgList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate
                (R.layout.msg_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Msg msg=mMsglist.get(position);
        if (msg.getType()==Msg.TYPE_RECEIVED){
            //如果收到的是消息则隐藏右边布局
            holder.leftlayout.setVisibility(View.VISIBLE);
            holder.rightlayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
        }else if (msg.getType()==Msg.TYPE_SENT){
            //如果是发出的消息则隐藏左边的布局
            holder.leftlayout.setVisibility(View.GONE);
            holder.rightlayout.setVisibility(View.VISIBLE);
            holder.rightMsg.setText(msg.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mMsglist.size();
    }
}
