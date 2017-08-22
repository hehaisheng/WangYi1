package com.example.pc_.wangyi.transfer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.pc_.wangyi.model.FileInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by pc- on 2017/6/20.
 */
public class NewHeaderReceiver implements Runnable{


    public static final String UTF_8 = "UTF-8";
    public static final int BYTE_SIZE_HEADER    = 1024 * 10;
    public static final int BYTE_SIZE_SCREENSHOT    = 1024 * 40;
    public static final String SPERATOR = "::";

    public Socket socket;
    public InputStream inputStream;
    public FileInfo fileInfo;
    public NewHeaderReceiver(Socket socket){
        this.socket=socket;
    }
    @Override
    public void run() {
        if(this.socket!= null){
            try {
                Log.d("服务端开始接受头部数据","NewHeaderReceiver接受");
                Log.d("服务端开始接受头部数据","NewHeaderReceiver接受");
                Log.d("服务端开始接受头部数据","NewHeaderReceiver接受");
                inputStream = socket.getInputStream();
                //Are you sure can read the 1024 byte accurately?
                //读取header部分
                byte[] headerBytes = new byte[BYTE_SIZE_HEADER];
                int headTotal = 0;
                int readByte = -1;
                //开始读取header
                while((readByte =inputStream.read()) != -1){
                    headerBytes[headTotal] = (byte) readByte;
                    headTotal ++;
                    if(headTotal == headerBytes.length){
                        break;
                    }
                }

                //读取缩略图部分
                byte[] screenshotBytes = new byte[BYTE_SIZE_SCREENSHOT];
                int screenshotTotal = 0;
                int sreadByte = -1;
                //开始读取缩略图
                while((sreadByte =inputStream.read()) != -1){
                    screenshotBytes[screenshotTotal] = (byte) sreadByte;

                    screenshotTotal ++;
                    if(screenshotTotal == screenshotBytes.length){
                        break;
                    }
                }

                Bitmap bitmap = BitmapFactory.decodeByteArray(screenshotBytes, 0, screenshotBytes.length);
                //解析header
                String jsonStr = new String(headerBytes, UTF_8);
                String[] strArray = jsonStr.split(SPERATOR);
                jsonStr = strArray[1].trim();
                fileInfo= FileInfo.toObject(jsonStr);
                fileInfo.setBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
