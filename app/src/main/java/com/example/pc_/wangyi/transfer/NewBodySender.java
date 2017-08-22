package com.example.pc_.wangyi.transfer;

import android.content.Context;
import android.util.Log;

import com.example.pc_.wangyi.model.FileInfo;
import com.example.pc_.wangyi.retrofit.RxBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by pc- on 2017/6/20.
 */
public class NewBodySender implements Runnable {




    public Context context;
    public FileInfo fileInfo;
    public SocketChannel socketChannel=null;
    public byte[] bytes;
    public RxBus rxBus;
    public static final String UTF_8 = "UTF-8";

    public static final int BYTE_SIZE_DATA      = 1024 * 4;








    public NewBodySender(SocketChannel socketChannel,FileInfo fileInfo){
        this.fileInfo=fileInfo;
        this.socketChannel=socketChannel;
    }

    @Override
    public void run() {


        try {
            parseBody();
        } catch (Exception e) {
            e.printStackTrace();
        }

      finish();

    }
    public void parseBody() throws Exception {

        rxBus=RxBus.newInstance();
        //将该路径的文件转成输入流
        InputStream fis = new FileInputStream(new File(fileInfo.getFilePath()));
        //一次4k
        bytes = new byte[BYTE_SIZE_DATA];
        long total = 0;
        int len = 0;
        long sendsudu=0;
        long sTime = System.currentTimeMillis();
        long eTime = 0;
        //一直循环输送数据
        while((len=fis.read(bytes)) != -1){

            Log.d("发送界面", "NewBodySender这里发送实体数据");
            Log.d("发送界面", "NewBodySender这里发送实体数据");
            Log.d("发送界面", "NewBodySender这里发送实体数据");
            socketChannel.write(new ByteBuffer[]{ByteBuffer.wrap(bytes)}, 0, len);
            sendsudu=sendsudu+len;
            //已经传输的数据
            total = total + len;
            eTime = System.currentTimeMillis();
            if(eTime - sTime > 200){ //大于200ms 才进行一次监听
                float sendsd=(float) (sendsudu*1.00*1000)/(eTime-sTime)/1024/1024;
                rxBus.post("发送速度"+sendsd+"");
                sTime = eTime;
            }
        }

        //每一次socket连接就是一个通信，如果当前OutputStream不关闭的话。FileReceiver端会阻塞在那里
        socketChannel.close();

    }


    public void finish()  {
        if(socketChannel!=null){
            try {
                socketChannel.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }

        }


    }
}
