package com.qinlong275.android.monitor.client.cmd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import com.qinlong275.android.monitor.client.connection.Server;

/**
 * 服务器要进行CMD操作时开启此线程
 * linux稍微不同
 * @author Administrator
 *
 */
public class ControlOperationThread extends Thread {

	private BufferedReader commandIn;
	private OutputStream resultOut;
	private Socket CMDSocket;
	private int port;

	public ControlOperationThread(int port) {
		this.port = port;
	}

	private String listen() throws IOException {
		return commandIn.readLine();
	}

	public void run() {

		try {
			this.CMDSocket = new Socket(Server.getServerIp(), port);
			this.CMDSocket.setTcpNoDelay(true);
		//	this.CMDSocket.setKeepAlive(true);
			this.commandIn = new BufferedReader(new InputStreamReader(CMDSocket.getInputStream(), "UTf-8"));
			this.resultOut = CMDSocket.getOutputStream();

			Process p = Runtime.getRuntime().exec("/system/bin/sh");
			Thread errOutThread = new Thread(new SyncPipe(p.getErrorStream(), resultOut));
			Thread resultOutThread = new Thread(new SyncPipe(p.getInputStream(), resultOut));
			errOutThread.start();
			resultOutThread.start();
			// PrintWriter stdin = new PrintWriter(p.getOutputStream());
			BufferedWriter stdin = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
			/** 以下可以输入自己想输入的cmd命令 */
			while (true) {
				String command = this.listen();
				// System.out.println(command); //测试
				if (command.equals("exit")) {
					// System.out.println("退出CMD"); //测试
					// errOutThread.stop();
					// resultOutThread.stop();
					commandIn.close();
					resultOut.close();
					CMDSocket.close();
					break;
				} else {
					stdin.write(command + "\r\n");
					stdin.flush();
				}

			}

			stdin.close();
		} catch (Exception e) {
			try {
				commandIn.close();
				resultOut.close();
				CMDSocket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
			}

			// System.err.println("连接出现异常，已退出"); //测试用
		}
	}

	/**
	 * 转发流
	 * @author Administrator
	 *
	 */
	private static class SyncPipe implements Runnable {
		private OutputStream ostrm_;
		private InputStream istrm_;

		public SyncPipe(InputStream istrm, OutputStream ostrm) {
			istrm_ = istrm;
			ostrm_ = ostrm;
		}

		public void run() {
			try {
				final byte[] buffer = new byte[1024];
				for (int length = 0; (length = istrm_.read(buffer)) != -1;) {
					ostrm_.write(buffer, 0, length);
				}
			} catch (Exception e) {
				throw new RuntimeException("处理命令出现错误：" + e.getMessage());
			}
		}

	}

}