package com.qinlong275.android.monitor.client.fileOperation;

import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import com.qinlong275.android.monitor.ConnectActivity;
import com.qinlong275.android.monitor.FileActivity;
import com.qinlong275.android.monitor.FileFragment;
import com.qinlong275.android.monitor.MyApplication;
import com.qinlong275.android.monitor.client.connection.Server;
import com.qinlong275.android.monitor.client.connection.TalkWithOther;

/**
 * 文件操作模式
 *
 * @author Administrator
 *
 */
public class FileControlMode {

	private Socket filesSocket = null;
	private TalkWithOther talkFilesWithServer = null;
	private static boolean isAlive = false;
	private static String TAG="FileControlMode";

	public FileControlMode(int port) {
		// TODO Auto-generated constructor stub
		try {
			//f服务端问自己获取的filesocket
			this.filesSocket = new Socket(Server.getServerIp(), port);
		//	this.filesSocket.setKeepAlive(true);
			this.isAlive = true;
			this.filesSocket.setTcpNoDelay(true);
			this.talkFilesWithServer = new TalkWithOther(filesSocket,true,false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			exit();
		}
	}



	/**
	 * 释放各种资源,当不再操作该文件时调用
	 */
	public void exit() {
		try {
			isAlive = false;
			sendCommand("exit");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}

		if (talkFilesWithServer!=null){
			talkFilesWithServer.close();
		}




	}

	public boolean isAlive() {
		testAlive();
		return isAlive;
	}

	public void testAlive() {
		try {
			talkFilesWithServer.getWriter().write("beng beng\r\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			exit();
		}
	}

	/**
	 * 发送命令
	 *
	 * @param command
	 * @throws IOException
	 */
	public void sendCommand(String command) throws IOException {
		if (talkFilesWithServer!=null){
			talkFilesWithServer.say(command);
		}
	}



	/**
	 * 获取path路径下的目录，以Arraylist返回
	 * 第一次点击 "path root:"
	 * "path "+path
	 *
	 */
	public ArrayList<String> getDir(String path) throws IOException {

		sendCommand(path);
		ArrayList<String> files = getFilesFromServer();
		if (files != null) {
			return files;
		} else {
			//System.out.println("您输入的路径有误，或文件不存在");
			return null;
		}

	}

	/**
	 * 从服务器获取目录，以以Arratlist返回
	 * 这里将所有的sysout最后要改成android的表达形式
	 * @return
	 * @throws IOException
	 */
	public ArrayList<String> getFilesFromServer() throws IOException {
		String result = talkFilesWithServer.listen();
		if (result.equals("I will give you this list.")) {
			ArrayList<String> files = new ArrayList<>();
			while (!(result = talkFilesWithServer.listen()).equals("That,s all.")) {
				files.add(result);
			}
			return files;
		} else {
			System.out.println(result);
			return null;
		}
	}

	/**
	 * 下载
	 * download 。。。 to 。。。
	 * 这里将所有的sysout最后要改成android的表达形式
	 * @param command
	 * @throws IOException
	 */
	public void download(String command) throws IOException {

		try {
			String[] commandParts = command.split(" to ");
			if (commandParts.length == 2) {
				String localPath = commandParts[1];
				BufferedOutputStream fileOutput = new BufferedOutputStream(new FileOutputStream(localPath));
				String shouldSentCommand = commandParts[0];
				sendCommand(shouldSentCommand);
				final String response = talkFilesWithServer.listen();
				// System.out.println(response); //测试

				String[] responsePorts = response.split(":");
				if (responsePorts[0].equals("OK,You can download.My port is")) {
					ControlDownloadThread controlDownloadThread = new ControlDownloadThread(
							Integer.parseInt(responsePorts[1]), fileOutput);
					controlDownloadThread.start();
				} else {
					Message msg=new Message();
					msg.obj=response;
					ConnectActivity.mHandler.sendMessage(msg);
					msg=null;

					System.out.println(response);
				}

			} else {
				Log.d(TAG,"下载指令错误，download path to local path");
			}
		} catch (FileNotFoundException e) {

			Message msg=new Message();
			msg.obj="本地路径无法访问";
			ConnectActivity.mHandler.sendMessage(msg);
			msg=null;
			System.out.println("本地路径无法访问");
		}

	}

	/**
	 * 上传
	 * 拥有权限后才能上传
	 * upload 。。。 to 。。。
	 * 这里将所有的sysout最后要改成android的表达形式
	 * @param command
	 * @throws IOException
	 */
	public void upload(String command) throws IOException {
		try {

			String[] commandParts = command.split(" to "); // 从to分割
			if (commandParts.length == 2) {
				String[] localPathParts = commandParts[0].split(" ", 2); // 为分割本地路径做准备
				if (localPathParts.length == 2) {
					String localPath = commandParts[0].split(" ")[1]; // 分割出本地路径
					BufferedInputStream fileInput = new BufferedInputStream(new FileInputStream(localPath));
					String shouldSentCommand = "upload " + commandParts[1];
					sendCommand(shouldSentCommand);
					final String response = talkFilesWithServer.listen();
					// System.out.println(response); //测试

					String[] responsePorts = response.split(":");
					if (responsePorts[0].equals("OK,You can upload.My port is")) {
						ControlUploadThread controlUploadThread = new ControlUploadThread(
								Integer.parseInt(responsePorts[1]), fileInput);
						controlUploadThread.start();
					} else {
						Message msg=new Message();
						msg.obj=response;
						ConnectActivity.mHandler.sendMessage(msg);
						msg=null;
						System.out.println(response);
					}
				} else {
					Log.d(TAG,"请输入正确指令");
				}

			} else {
				Log.d(TAG,"下载指令错误，upload path to local path");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Message msg=new Message();
			msg.obj="本地路径无法访问";
			ConnectActivity.mHandler.sendMessage(msg);
			msg=null;
			System.out.println("本地路径无法访问");
		}

	}

}

