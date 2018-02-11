package com.qinlong275.android.monitor.client.fileOperation;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.qinlong275.android.monitor.client.connection.Server;
import com.qinlong275.android.monitor.client.connection.TalkWithOther;

/**
 * 服务器对文件进行操作时，开其此线程，即被操作端的文件被操作线程
 *
 * @author Administrator
 *
 */
public class FileOperationThread extends Thread {
	private int port;
	private Socket filesSocket = null;
	private TalkWithOther talkFilesWithServer = null;

	public FileOperationThread(int port) {
		// TODO Auto-generated constructor stub
		this.port = port;
	}

	public void run() {
		try {

			this.filesSocket = new Socket(Server.getServerIp(), port); // 连接Socket相当于告诉服务器可以对其进行文件操作
		//	this.filesSocket.setKeepAlive(true);
			filesSocket.setTcpNoDelay(true);
			this.talkFilesWithServer = new TalkWithOther(filesSocket,false,true);
			while (true) {
				String request = talkFilesWithServer.listen();
				String[] requestParts = request.split(" ", 2);
				if (requestParts[0].equals("exit")) {
					exit();
					break;
				}
				if (requestParts.length == 2) {
					switch (requestParts[0]) {
						case "path":
							sendFilesName(requestParts[1], talkFilesWithServer.getWriter());
							break;
						case "download":
							download(requestParts[1]);
							break;
						case "upload":
							upload(requestParts[1]);
							break;
						default:
							talkFilesWithServer.say("You give me a wrong command!");
							break;
					}

				}
			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			exit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			exit();
		}

	}

	/**
	 * 服务器退出文件操作时执行
	 */
	public void exit() {

		if (talkFilesWithServer != null) {
			try {
				talkFilesWithServer.getReader().close();
				talkFilesWithServer.getWriter().close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if (filesSocket != null)
			try {
				filesSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	/**
	 * 获取path目录下的文件和文件夹
	 *
	 * @param path
	 * @return
	 */
	public String[] getFiles(String path) {
		File dir = new File(path);
		if ((!dir.isDirectory()) || (!dir.exists())) {
			return null;
		}
		String[] files = dir.list();
		if (files != null) {

			String[] filesPath = new String[files.length];
			for (int i = 0; i < filesPath.length; i++)
				filesPath[i] = path + "/" + files[i];
			return filesPath;
		}
		return null;
	}

	/**
	 * 发送path文件夹下面的文件和文件夹，这里没有直接用到say()方法，是为了加快速度
	 *
	 * @param out
	 * @throws IOException
	 */
	public void sendFilesName(String path, BufferedWriter out) throws IOException {

		if (path.equals("root:")) {
			out.write("I will give you this list.\r\n");
			out.flush();
			File[] root = File.listRoots();
			for (File file : root) {
				String filePath = file.getAbsolutePath().replace('\\', '/');
				out.write(filePath + "\r\n");
			}
			out.write("That,s all.\r\n");

		} else {
			File dir = new File(path);
			if ((!dir.isDirectory()) || (!dir.exists())) {
				out.write("It don,t exit or not a dir." + "\r\n");
				out.flush();
			} else {

				String[] files = getFiles(path);

				if (files != null) {
					out.write("I will give you this list.\r\n");
					out.flush();
					for (int i = 0; i < files.length; i++) {
						out.write(files[i] + "\r\n");
					}
					out.write("That,s all.\r\n");
				} else {
					out.write("You don,t have permission to touch \r\n");
				}

			}
		}
		out.write("This operation is over.\r\n");
		out.flush();

	}


	/**
	 * 服务器要下载路径为path文件时执行，并开启新的下载线程防止阻塞
	 *
	 * @param path
	 * @throws IOException
	 */
	public void download(String path) throws IOException {
		BufferedInputStream fileIn;
		try {
			fileIn = new BufferedInputStream(new FileInputStream(path));
			talkFilesWithServer.say("OK,You can download.");
			String portResponse = talkFilesWithServer.listen();
			String[] portResponseParts = portResponse.split(":");
			if ((portResponseParts[0].equals("Let me download.My port is")) && (portResponseParts.length == 2)) {
				int downloadPort = Integer.parseInt(portResponseParts[1]);
				DownloadThread downloadThread = new DownloadThread(path, downloadPort, fileIn);
				downloadThread.start();

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			talkFilesWithServer.say("Sorry,The file is not exit or is a dir.");
		}

	}

	/***
	 * 服务器要上传为path文件时执行，并开启新的上传线程防止阻塞
	 *
	 * @param path
	 * @throws IOException
	 */
	public void upload(String path) throws IOException {
		try {
			BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(path));
			talkFilesWithServer.say("OK,You can upload.");
			String portResponse = talkFilesWithServer.listen();
			String[] portResponseParts = portResponse.split(":");
			if ((portResponseParts[0].equals("Let me upload.My port is")) && (portResponseParts.length == 2)) {
				int uploadPort = Integer.parseInt(portResponseParts[1]);
				UploadThread uploadThread = new UploadThread(uploadPort, fileOut);
				uploadThread.start();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			talkFilesWithServer.say("Sorry,You give me a wrong path to upload.");
		}

	}

}
