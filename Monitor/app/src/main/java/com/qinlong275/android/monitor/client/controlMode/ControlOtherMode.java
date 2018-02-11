package com.qinlong275.android.monitor.client.controlMode;

import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Serializable;
import java.net.Socket;

import com.qinlong275.android.monitor.CommandActivity;
import com.qinlong275.android.monitor.client.connection.Server;
import com.qinlong275.android.monitor.client.connection.TalkWithOther;

/**
 * 进入CMD控制模式时进入
 * @author Administrator
 *
 */
public class ControlOtherMode{

	private String name;
	private BufferedWriter commandWriter;
	private Socket cmdSocket;
	public PipedInputStream resultTranIn = new PipedInputStream();


	/**
	 * 构造函数
	 * @param name
	 * @param talkCommandWithServer
	 * @throws IOException
	 */
	public ControlOtherMode(String name,  TalkWithOther talkCommandWithServer)
			throws IOException {
		// TODO Auto-generated constructor stub
		this.name = name;

		talkCommandWithServer.say("I want to control:" + name);
		String response = talkCommandWithServer.listen();
		String[] responseParts = response.split(":");

		if (responseParts[0].equals("OK,You can control.Port is")) {
			if (responseParts.length == 2) {
				System.out.println(response);
				int cmdPort = Integer.parseInt(responseParts[1]);
				this.cmdSocket = new Socket(Server.getServerIp(), cmdPort);
				this.cmdSocket.setTcpNoDelay(true);
			//	this.cmdSocket.setKeepAlive(true);
				this.commandWriter = new BufferedWriter(new OutputStreamWriter(cmdSocket.getOutputStream(), "UTF-8"));
				System.out.println("control准备就绪");
				makeResultOutTo();
			} else {
				System.out.println(response);
				System.out.println(name + ":control失败！");
				throw new IOException();
			}
		} else {
			System.out.println(response);
			System.out.println(name + ":control失败！");
			throw new IOException();
		}

	}

	/**
	 * 要改成text。。。
	 */
	public void makeResultOutTo() {
		try {
			//这里还需要改
			Thread resultThread = new Thread(new SyncPipe(cmdSocket.getInputStream(), this.resultTranIn));
			//将服务器发来的消息转发到控制台
			resultThread.start();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
		}
	}

	/**
	 * 发送指令
	 * @param command
	 * @throws IOException
	 */
	public void sendCommand(String command) throws IOException {
		commandWriter.write(command + "\r\n");
		commandWriter.flush();
	}



	/**
	 * 打印结果的类
	 * @author Administrator
	 *
	 */
	private static class SyncPipe implements Runnable {
		private PipedInputStream resultTranIn = null;
		private PipedOutputStream resultTranOut = null;
		private InputStream istrm_;

		public SyncPipe(InputStream istrm, PipedInputStream In) {
			PipedOutputStream Out = new PipedOutputStream();
			this.istrm_ = istrm;
			try {
				In.connect(Out);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.resultTranIn = In;
			this.resultTranOut = Out;

		}

		public void run() {
			try {

				final byte[] buffer = new byte[1024];
				//System.out.println("3123");
				for (int length = 0; (length = istrm_.read(buffer)) != -1;) {
					//System.out.println("11123123");
					resultTranOut.write(buffer,0,length);
					//resultTranOut.write(buffer,0,length);
					//ostrm_.write(buffer, 0, length);
				}
			} catch (Exception e) {
				try {
					e.printStackTrace();
					istrm_.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					// e1.printStackTrace();
				}
			}
		}

	}

	public void exit() throws IOException {
		sendCommand("exit");
		commandWriter.close();
		cmdSocket.close();

	}
}
