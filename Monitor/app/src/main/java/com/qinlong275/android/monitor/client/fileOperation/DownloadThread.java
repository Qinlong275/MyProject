package com.qinlong275.android.monitor.client.fileOperation;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.qinlong275.android.monitor.client.connection.Server;


/**
 * 被控制端的下载线程
 * @author Administrator
 *
 */
public class DownloadThread extends Thread {
	private int port;
	private Socket downloadSocket = null;
	private BufferedOutputStream dataOut = null;		//将我的文件写到这个流中发出去
	private BufferedInputStream fileIn = null;		//要下载的我的文件


	public DownloadThread(String path, int port,BufferedInputStream fileIn) {
		// TODO Auto-generated constructor stub

		this.port = port;
		this.fileIn = fileIn;
	}

	public void run() {
		try {
			downloadSocket = new Socket(Server.getServerIp(), port);
			downloadSocket.setKeepAlive(true);
			dataOut = new BufferedOutputStream(downloadSocket.getOutputStream());
			download(fileIn, dataOut);
			exit();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			exit();
		}
	}

	public void exit() {
		if(fileIn != null)
			try {
				fileIn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		if(dataOut != null)
			try {
				dataOut.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		if(downloadSocket != null)
			try {
				downloadSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
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
}