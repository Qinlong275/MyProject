package com.qinlong275.android.monitor.session;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 用来产生对话Socket
 * @author LCDZhao
 *
 */
public class LCDSession {

	/**
	 * 输入protocol语句，自动建立与客户端的Socket，并返回该Socket
	 * 自动添加分隔符':'
	 * @param protocol
	 * @param portSend
	 * @return
	 * @throws IOException
	 */
	public static Socket serverGetSessionSocket(String protocol,BufferedWriter portSend) throws IOException {
		@SuppressWarnings("resource")
		ServerSocket serverSocket = new ServerSocket(0);
		serverSocket.setSoTimeout(500);			//防止一直阻塞
		portSend.write(protocol + ":" + serverSocket.getLocalPort()+"\r\n");
		portSend.flush();
		Socket socket = serverSocket.accept();
	//	socket.setKeepAlive(true);
		socket.setTcpNoDelay(true);
		return socket;
	}

	/**
	 * 为了对称而添加的该方法，实际上并没有怎么用到
	 * @param ip
	 * @param port
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static Socket clientGetSissionSocket(String ip,int port) throws UnknownHostException, IOException {
		Socket sessionSicket = new Socket(ip, port);
	//	sessionSicket.setKeepAlive(true);
		return sessionSicket;
	}


}
