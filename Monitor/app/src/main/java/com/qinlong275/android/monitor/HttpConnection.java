package com.qinlong275.android.monitor;

import android.nfc.Tag;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 秦龙 on 2017/10/16.
 */

public class HttpConnection {

    public byte[] getUrlBytes(String urlSpec) throws IOException{
        URL url=new URL(urlSpec);
        HttpURLConnection connection=(HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out=new ByteArrayOutputStream();
            InputStream in=connection.getInputStream();

            if (connection.getResponseCode()!=HttpURLConnection.HTTP_OK){
                Log.i(CommandActivity.TAG,"连接有错误，请检查");
                throw new IOException(connection.getResponseMessage()+": with "+urlSpec);
            }
            int byteRead=0;
            byte[]buffer=new byte[1024];
            while ((byteRead=in.read(buffer))>0){
                out.write(buffer,0,byteRead);
            }
            out.close();
            return  out.toByteArray();
        }finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException{
        return new String(getUrlBytes(urlSpec));
    }
}
