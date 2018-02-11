package com.qinlong275.android.monitor.client.fileOperation;

import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.qinlong275.android.monitor.ConnectActivity;
import com.qinlong275.android.monitor.FileActivity;
import com.qinlong275.android.monitor.MyApplication;
import com.qinlong275.android.monitor.client.connection.Server;

/**
 * 控制端上传线程
 * @author Administrator
 *
 */
public class ControlUploadThread extends Thread {
	private Socket socketOut = null;
	private BufferedInputStream dataIn = null;
	private BufferedOutputStream dataOut = null;

	public ControlUploadThread(int port, BufferedInputStream fileInput) throws UnknownHostException, IOException {
		// TODO Auto-generated constructor stub
		this.socketOut = new Socket(Server.getServerIp(), port);
		this.socketOut.setKeepAlive(true);
		this.dataOut = new BufferedOutputStream(this.socketOut.getOutputStream());
		this.dataIn = fileInput;
	}

	public void run() {
		try {
			upload(dataIn, dataOut);
			exit();
			Message msg=new Message();
			msg.obj="上传成功";
			ConnectActivity.mHandler.sendMessage(msg);
			msg=null;
			System.out.println("上传成功");
			//反馈给通知栏

		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();

			Message msg=new Message();
			msg.obj="上传失败，可能是网络原因";
			ConnectActivity.mHandler.sendMessage(msg);
			msg=null;
			System.out.println("上传失败，可能是网络原因");
			exit();
		}

	}

	public boolean upload(BufferedInputStream input, BufferedOutputStream out) throws IOException {

		byte[] buf = new byte[1024];
		int len = 0;
		while ((len = input.read(buf)) != -1) {
			out.write(buf, 0, len);
		}
		out.flush();
		return true;

	}

	public void exit() {
		if (dataIn != null) {
			try {
				dataIn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (dataOut != null) {
			try {
				dataOut.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(socketOut != null)
		{
			try {
				socketOut.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}