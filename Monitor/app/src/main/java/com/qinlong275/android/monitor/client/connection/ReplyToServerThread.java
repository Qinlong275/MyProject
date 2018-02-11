package com.qinlong275.android.monitor.client.connection;

import java.io.IOException;

import com.qinlong275.android.monitor.client.cmd.ControlOperationThread;
import com.qinlong275.android.monitor.client.fileOperation.FileOperationThread;


/**
 * 对服务器的响应做出应答的线程，在其中完成对客户端的各种操作
 *
 * @author Administrator
 *
 */
public class ReplyToServerThread extends Thread {
	TalkWithOther talkWithServer;


	public ReplyToServerThread() {
		// TODO Auto-generated constructor stub

		this.talkWithServer = Client.talkConnectWithServer;
	}


	public void run() {
		String response;
		while (true) {
			try {
				response = talkWithServer.listen();
				if (!response.equals("Your password is wrong!")
						&&!response.equals("Your name is used by others!")) {		//如果密码正确，或用户名没有被使用时继续执行
					String[] responseParts = response.split(":");
					//将response分解，时response能容纳更多的消息，尽可能的将其打造成一个无状态的协议
					//分隔符为'_'
					switch (responseParts[0]) {


						case "Are you alive?":
							talkWithServer.say("Yes,I,m alive!");
							break;


						case "I want to control you!My cmd port is":
							//对服务CMD操作的响应，开启新线程
							ControlOperationThread controlOperationThread =
									new ControlOperationThread(Integer.parseInt(responseParts[1]));
							controlOperationThread.start();
							//System.out.println("CMD is ok");   //测试
							break;
						case "I want to handle your files.My port is":
							//对服务器文件操作的响应，开启新线程
							FileOperationThread fileOperationThread =
									new FileOperationThread(Integer.parseInt(responseParts[1]));
							fileOperationThread.start();
							break;
						default:
							System.out.println(response);
							//System.out.println("服务器命令输入错误，请重新输入！");
							break;
					}
				} else {
					System.err.println(response);
					Client.isApplicateSucceed = false;
					Client.isSuperClient = false;
					Client.isApplicating = false;
					System.err.println("已断开连接");
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("网络出现问题，请检查网络设置");
				Client.isApplicateSucceed = false;
				Client.isApplicating = false;
				exit();
				break;
			}

		}

	}

	public void exit() {
			talkWithServer.close();
	}

}
