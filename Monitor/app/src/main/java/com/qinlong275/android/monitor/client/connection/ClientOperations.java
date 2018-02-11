package com.qinlong275.android.monitor.client.connection;

import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import com.qinlong275.android.monitor.client.connection.Server;
import com.qinlong275.android.monitor.client.controlMode.ControlOtherMode;

/**
 * 客户端的各种操作封装类
 * 必须在服务器端接受后才能运行
 * @author Administrator
 *
 */
public class ClientOperations{

	public ClientOperations() {

	}

	/**
	 * 升级为超级客户端，输入密码
	 * @param password
	 */
	public void toSuper(String password) {
		Client.toBeSuperClient(password);
	}

	/**
	 *
	 * @param otherClientName
	 */
	public Object[] fileMode(String otherClientName) {

		try {
			return Client.fileControl(otherClientName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			// 这里网络断开连接
			exit();
			return null;
		}

	}


	/**
	 *
	 * @return
	 */
	public ArrayList<String> getClients() {
		try {
			return Client.showClients();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			// 这里网络连接断开
			//System.out.println("网络连接断开");
			exit();
			return null;
		}
	}


	/**
	 *
	 * @param otherClientName
	 * @return
	 */
	public Object[] controlOther(String otherClientName) {
		Object[] result = new Object[2];
		if (Client.isSuperClient == true) {
			try {
				System.out.println("正在获取");
				ControlOtherMode controlMode = new ControlOtherMode(otherClientName, Client.talkCommandWithServer);
				result[0] =new String("success");
				result[1] = controlMode;
				return result;
			} catch (Exception e) {
				//这里连接断开
				result[0] =new String("连接断开");
				result[1] = null;
				return result;
			}

		} else {
			result[0] =new String("请升级为超级客户端！");
			result[1] = null;
			exit();
			return result;
		}

	}

	/**
	 *
	 */
	public void exitSuper() {
		Client.exitSuperClient();
	}


	/**
	 *
	 */
	public  void exit() {
		//用安卓提示连接断开，并让另一个线程停止
		Client.replyToServerThread.exit();
		Client.exitSuperClient();
	}
}
