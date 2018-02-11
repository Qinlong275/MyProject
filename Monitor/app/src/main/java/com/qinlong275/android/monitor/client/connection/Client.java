package com.qinlong275.android.monitor.client.connection;

import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.qinlong275.android.monitor.ConnectActivity;
import com.qinlong275.android.monitor.FileActivity;
import com.qinlong275.android.monitor.MyApplication;
import com.qinlong275.android.monitor.client.fileOperation.FileControlMode;
import com.qinlong275.android.monitor.session.LCDSession;

/**
 * 客户端类
 *
 * @author Administrator
 *
 */
public class Client {
	public static String name;
	public static boolean isApplicating = false; // 是否正在申请
	public static boolean isApplicateSucceed = false; // 是否申请成功
	public static boolean isSuperClient = false; // 是否为超级客户端，默认为否
	private static Socket connectSocket; // 连接的Socket
	private static Socket getCMDControlSocket; // 获得CMD操作权限的Socket
	public static ReplyToServerThread replyToServerThread; // 对服务器命令做出响应的线程
	public static TalkWithOther talkCommandWithServer; // 和server进行命令及权限申请对话
	public static TalkWithOther talkConnectWithServer; // 对server进行响应，進行对话
	public static ClientOperations clientOperations; // 对服务的操作
	private static String controlPassword;
	private static BufferedReader readerFromKey = new BufferedReader(new InputStreamReader(System.in));
	private static String TAG="Client";

	/**
	 * 构造方法，在其中开启对服务器响应的线程，其中当服务器接受client的申请后， client的剩余初始化工作在
	 * replyToServerThread中完成
	 *
	 * @param temName
	 * @param serverIp
	 * @param serverPort
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public Client(String temName, String serverIp, int serverPort, String password) {

		try {
			Client.name = "an_" + temName;
			Server.setServerIp(serverIp);
			Server.setServerPort(serverPort);
			Server.setPassword(password);

			Client.connectSocket = new Socket(serverIp, serverPort);
		//	Client.connectSocket.setKeepAlive(true);
			Client.connectSocket.setTcpNoDelay(true);
			Client.isApplicating = true;
			Client.talkConnectWithServer = new TalkWithOther(connectSocket,false,true);
			// 开启对服务器响应的线程，其中当服务器接受client的申请后，client的剩余初始化工作在
			// replyToServerThread中完成
			Client.replyToServerThread = new ReplyToServerThread();

			Client.clientOperations = new ClientOperations();
			String response = talkConnectWithServer.listen();
			if (response.equals("What,s your name and password?")) {
				talkConnectWithServer.say(Client.name + " " + Server.getPassword());

				response = talkConnectWithServer.listen();
				String[] responseParts = response.split(":");
				if (responseParts[0].equals("Your application is accepted,my permissonListendPort is")) {
					// 服务器接受连接请求,完成最后的初始化
					int port = Integer.parseInt(responseParts[1]);
					isAllowedToConnect(port);
					replyToServerThread.start();
				} else {
					System.out.println(response);
				}
			} else {
				System.out.println(response);
			}
		} catch (UnknownHostException e) {
			Log.d(TAG,"服务器地址输入错误，请重新输入");
		} catch (IOException e) {
			Log.d(TAG,"网络连接错误，正在重连");
			/*
			 * try { Thread.sleep(10000); } catch (InterruptedException e1) { // TODO
			 * Auto-generated catch block e1.printStackTrace(); }
			 */

		}
	}

	/**
	 * 获得控制密码
	 *
	 * @return
	 */
	public static String getControlPassword() {
		return controlPassword;
	}

	/**
	 * 设置控制密码
	 *
	 * @param controlPassword
	 */
	public static void setControlPassword(String controlPassword) {
		Client.controlPassword = controlPassword;
	}

	/**
	 * 获取从键盘输入
	 *
	 * @return
	 */
	public static BufferedReader getCommandInFromKey() {
		return readerFromKey;
	}

	/**
	 * 获取操作权限
	 */
	public static void toBeSuperClient(String controlPassword) {
		if (isApplicateSucceed) {
			try {
				if (isSuperClient == false) {
					Client.setControlPassword(controlPassword);
					controlMode();
					Client.isSuperClient = true;
				} else {
					Log.d(TAG,"您已获得控制权限");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Client.isSuperClient = false;
				Log.d(TAG,"网络连接错误，正在重连");

				Message msg=new Message();
				msg.obj="升级出现错误";
				ConnectActivity.mHandler.sendMessage(msg);
				msg=null;
			}
		} else {
			Log.d(TAG,"您的连接申请还未被接受！");
		}

	}

	/**
	 * 进入超级客户端模式
	 *
	 * @throws IOException
	 */
	public static void controlMode() throws IOException {
		// TODO Auto-generated constructor stub
		talkCommandWithServer.say("I want to control!My password is:" + Client.getControlPassword());
		final String response = talkCommandWithServer.listen();
		if (response.equals("OK,you are allow to control!")) {

			Message msg=new Message();
			msg.obj="现在是超级客户端状态，请进行进行操作";
			ConnectActivity.mHandler.sendMessage(msg);
			msg=null;

			Log.d(TAG,"现在是超级客户端状态，请进行进行操作");
		} else {
			Log.d(TAG,response);
			// throw new IOException();
		}
	}

	/**
	 * 取消操作权限
	 */
	public static void exitSuperClient() {

		try {
			if (isApplicateSucceed == true) {
				if (isSuperClient) {
					talkCommandWithServer.say("I will exit superClient mode!");
					// 向服务器发送退出请求
					String response = talkCommandWithServer.listen();
					if (response.equals("Ok,I know that!")) {
						Client.isSuperClient = false;
						Message msg=new Message();
						msg.obj="已退出";
						ConnectActivity.mHandler.sendMessage(msg);
						msg=null;
						Log.d(TAG,"已退出");
					} else {
						// System.err.println("网络出现问题，已退出SuperClient模式");
						Client.isSuperClient = false;
						Log.d(TAG,"出现未知错误Client 124");
					}
				} else {
					Log.d(TAG,"您没有控制权限，无需退出！");
				}
			} else {
				Log.d(TAG,"您的连接申请还未被同意，请等待同意");
			}
		} catch (IOException e) {
			Log.d(TAG,"网络出现问题，已退出SuperClient模式");
			// TODO Auto-generated catch block
			Client.isSuperClient = false;
		}

	}

	/**
	 * 操作otherClientName的文件，返回result数组，为Object类型，其中0为响应信息，1为操作动作集合
	 *
	 * @param otherClientName
	 * @return
	 * @throws IOException
	 */
	public static Object[] fileControl(String otherClientName) throws IOException {
		Object[] result = new Object[2];
		talkCommandWithServer.say("I want to handle file in:" + otherClientName);
		String response = talkCommandWithServer.listen();

		String[] responseParts = response.split(":", 2);
		if (responseParts[0].equals("OK,You can handle.Port is") && responseParts.length == 2) {
			result[0] = "success";
			FileControlMode fileControlMode = new FileControlMode(Integer.parseInt(responseParts[1]));
			result[1] = fileControlMode;
			return result;
		} else {
			// 将sysout改成安卓的提示
			result[0] = response;
			result[1] = null;
			return result;
		}

	}

	/**
	 * 展示所有在线的客户端，并把结果作为Arraylist返回
	 *
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<String> showClients() throws IOException {
		if (talkCommandWithServer!=null){
			talkCommandWithServer.say("Show me clients!");
			ArrayList<String> clientsName = new ArrayList<>();
			String name;
			if (talkCommandWithServer.listen().equals("OK,I will show you!"))
				System.out.println("正在show");
			while (!(name = talkCommandWithServer.listen()).equals("That,s all.")) {
				clientsName.add(name);
				System.out.println(name); // 非图形化界面需要
			}
			return clientsName;
		}else {

			Message msg=new Message();
			msg.obj="网络异常，请稍后重试";
			ConnectActivity.mHandler.sendMessage(msg);
			msg=null;
		}
		return null;
	}

	/**
	 * 服务器同意申请后，RreplyToServerThread调用其完成Client的剩余初始化
	 *
	 * @param port
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static void isAllowedToConnect(int port) throws UnknownHostException, IOException {
		Client.getCMDControlSocket = LCDSession.clientGetSissionSocket(Server.getServerIp(), port);
		Client.talkCommandWithServer = new TalkWithOther(getCMDControlSocket,true,false);
		Client.isApplicateSucceed = true;
		Client.isApplicating = false;
	}

	public static void exit(){
		if(talkCommandWithServer != null)
			talkCommandWithServer.close();
		if(talkConnectWithServer != null)
			talkCommandWithServer.close();
	}

	public static void resurgence() {
		while (true) {
			if (name != null) {
				System.out.println("正在重连");
				try {
					Client.connectSocket = new Socket(Server.getServerIp(), Server.getServerPort());
					// Client.replyToServerSocket.setKeepAlive(true);
					Client.isApplicating = true;
					Client.talkConnectWithServer = new TalkWithOther(connectSocket, false, true);
					// 开启对服务器响应的线程，其中当服务器接受client的申请后，client的剩余初始化工作在
					// replyToServerThread中完成
					Client.replyToServerThread = new ReplyToServerThread();

					Client.clientOperations = new ClientOperations();



                    String response = Client.talkConnectWithServer.listen();
                    if (response.equals("What,s your name and password?")) {
                        Client.talkConnectWithServer.say(Client.name + " " + Server.getPassword());

                        response = Client.talkConnectWithServer.listen();
                        String[] responseParts = response.split(":");
                        if (responseParts[0].equals("Your application is accepted,my permissonListendPort is")) {
                            // 服务器接受连接请求,完成最后的初始化
                            int port = Integer.parseInt(responseParts[1]);
                            Client.isAllowedToConnect(port);
                            replyToServerThread.start();
                        } else {
                            System.out.println(response);
                        }
						if (isSuperClient) {
							isSuperClient = false;
							Client.toBeSuperClient("weizhao333");
						}
                    }



					Client.isApplicateSucceed = true;

				} catch (UnknownHostException e) {
					try {
						Log.d(TAG,"重连失败，3秒钟后继续重连");
						Thread.sleep(3000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					try {
						Log.d(TAG,"重连失败，3秒钟后继续重连");
						Thread.sleep(3000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				break;
			}

		}
	}


}
