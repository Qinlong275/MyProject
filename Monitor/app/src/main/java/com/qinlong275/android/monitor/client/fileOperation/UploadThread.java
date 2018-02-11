package com.qinlong275.android.monitor.client.fileOperation;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.qinlong275.android.monitor.client.connection.Server;

/**
 * 被操作端的文件上传线程
 *
 * @author Administrator
 *
 */
public class UploadThread extends Thread {
	private int port;
	private Socket uploadSocket = null;
	private BufferedInputStream dataIn = null;
	private BufferedOutputStream fileOut = null;

	public UploadThread(int port, BufferedOutputStream fileOut) {
		// TODO Auto-generated constructor stub
		this.port = port;
		this.fileOut = fileOut;
	}

	public void run() {
		try {
			uploadSocket = new Socket(Server.getServerIp(), port);
			uploadSocket.setKeepAlive(true);
			dataIn = new BufferedInputStream(uploadSocket.getInputStream());
			upload(fileOut, dataIn);
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

	public void upload(BufferedOutputStream fileOut, BufferedInputStream input) throws IOException {

		byte[] buf = new byte[1024];
		int len = 0;
		while ((len = input.read(buf)) != -1) {
			fileOut.write(buf, 0, len);

		}
		fileOut.flush();
		fileOut.close();

	}

	private void exit() {
		if (fileOut != null) {
			try {
				fileOut.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}
		if (uploadSocket != null) {
			try {
				uploadSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}
		if (dataIn != null) {
			try {
				dataIn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}

	}

}
