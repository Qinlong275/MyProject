package com.qinlong275.android.monitor.client.connection;
/**
 * 服务端封装类
 * @author Administrator
 *
 */
public class Server {
	private static String serverIp;
	private static int serverPort;
	private static String password;
	public static boolean isPasswordRight = false;




	public static String getPassword() {
		return password;
	}
	public static void setPassword(String password) {
		Server.password = password;
	}
	public static String getServerIp() {
		return serverIp;
	}
	public static void setServerIp(String serverIp) {
		Server.serverIp = serverIp;
	}
	public static int getServerPort() {
		return serverPort;
	}
	public static void setServerPort(int serverPort) {
		Server.serverPort = serverPort;
	}


}
