package com.qinlong275.android.monitor.client.fileOperation;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.qinlong275.android.monitor.ConnectActivity;
import com.qinlong275.android.monitor.File;
import com.qinlong275.android.monitor.FileActivity;
import com.qinlong275.android.monitor.MyApplication;
import com.qinlong275.android.monitor.R;
import com.qinlong275.android.monitor.client.connection.Server;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * 控制端下载线程
 * @author Administrator
 *
 */
public class ControlDownloadThread extends Thread{

	private Socket socketIn = null;
	private BufferedInputStream dataIn = null;
	private BufferedOutputStream dataOut = null;

	public ControlDownloadThread(int port,BufferedOutputStream dataOut) throws UnknownHostException, IOException {
		// TODO Auto-generated constructor stub

		this.dataOut = dataOut;
		this.socketIn = new Socket(Server.getServerIp(), port);
		this.socketIn.setKeepAlive(true);
		this.dataIn = new BufferedInputStream(this.socketIn.getInputStream());
	}

	public void run() {
		try {
			download(dataIn, dataOut);
			Message msg=new Message();
			msg.obj="下载成功";
			ConnectActivity.mHandler.sendMessage(msg);
			msg=null;

			System.out.println("下载成功");
			exit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Message msg=new Message();
			msg.obj="下载失败，可能是网络原因";
			ConnectActivity.mHandler.sendMessage(msg);
			msg=null;

			System.out.println("下载失败，可能是网络原因");
			exit();
		}

	}


	public boolean download(BufferedInputStream input, BufferedOutputStream out) throws IOException {

		byte[] buf = new byte[1024];
		int len = 0;
		while ((len = input.read(buf)) != -1) {
			out.write(buf, 0, len);
		}
		out.flush();
		return true;

	}

	/**
	 * 释放资源
	 */
	public void exit() {
		if(dataIn != null) {
			try {
				dataIn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if(dataOut != null) {
			try {
				dataOut.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(socketIn != null) {
			try {
				socketIn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	}

}
