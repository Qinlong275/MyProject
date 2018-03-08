package com.qinlong275.android.monitor.client.connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;


/**
 * 和服务器的对话类 所有和服务器的对话，都可以直接使用这个类，让对话简单了不少
 */
public class TalkWithOther {
    private BufferedReader reader;
    private BufferedWriter writer;
    private Socket talkSocket;
    private static final int HEARTBEAT_PERIOD = 10000;
    private static final int TEST_ALIVE_TIME = 13000;
    public boolean isAlive = true;

    public TalkWithOther(Socket talkSocket, boolean heatbeat,boolean listenHeatbeat) throws IOException {
        // TODO Auto-generated constructor stub
        this.reader = new BufferedReader(new InputStreamReader(talkSocket.getInputStream(), "UTF-8"));
        this.writer = new BufferedWriter(new OutputStreamWriter(talkSocket.getOutputStream(), "UTF-8"));
        this.talkSocket = talkSocket;
        if (heatbeat == true) {
            Thread sendHeartbeatThread = new sendHeartbeatThread(this);
            sendHeartbeatThread.start();
        }
        if(listenHeatbeat == true) {
            TestAliveThread testAliveThread = new TestAliveThread(this);
            testAliveThread.start();
        }
    }

    /**
     * 给服务器发送消息
     *
     * @param request
     * @throws IOException
     */
    public void say(String request) throws IOException {
        writer.write(request + "\r\n");
        writer.flush();
    }

    /**
     * 接受服务器端的消息 服务器断开连接时，此时读取的为null则抛出异常
     *
     * @return
     * @throws IOException
     */
    public String listen() throws IOException {
        String response = reader.readLine();
        //接收到beng beng不管，继续接受下一行
        while((response != null) && "beng beng".equals(response)) {
            isAlive = true;
        //    System.out.println("听到心跳");//防止在读消息的过程中突然
            response = reader.readLine();								//出现beng beng导致崩溃
        }
        if (response != null) {
            isAlive = true;
            return response;
        } else {
            throw new IOException();
        }

    }

    public BufferedReader getReader() {
        return reader;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    public void setWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    public void close() {
        try {
            talkSocket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private class sendHeartbeatThread extends Thread {
        private TalkWithOther talkWithServer;

        public sendHeartbeatThread(TalkWithOther talkWithServer) {
            // TODO Auto-generated constructor stub
            this.talkWithServer = talkWithServer;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                while (true) {
                    try {
                        Thread.sleep(HEARTBEAT_PERIOD);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        break;
                    }
                    this.talkWithServer.say("beng beng");
          //          System.out.println("蹦");
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                this.talkWithServer.close();
            }
        }

    }

    /**
     * 检测对话是否死亡，若死亡，则关闭对话
     * @author lcdzh
     *
     */
    private class TestAliveThread extends Thread {
        private TalkWithOther talkWithServer;

        public TestAliveThread(TalkWithOther talkWithServer) {
            // TODO Auto-generated constructor stub
            this.talkWithServer = talkWithServer;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (true) {
                try {
                    Thread.sleep(TEST_ALIVE_TIME);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //每隔一段时间检测一次，
                if (talkWithServer.isAlive == true) {
                    talkWithServer.isAlive = false;
                } else {
              //      System.out.print("听不到心跳,死亡");
                    talkWithServer.close();
                    break;
                }
            }
        }

    }

}