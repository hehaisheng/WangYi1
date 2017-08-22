package com.example.pc_.wangyi.transfer;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.pc_.wangyi.utils.FileUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by pc- on 2017/6/20.
 */
public class NewBodyReceiver implements Runnable {


    public SocketChannel socketChannel;
    public BufferedOutputStream bufferedOutputStream;
    public static final int BYTE_SIZE_DATA= 1024 * 4;
    public Context context;

    public NewBodyReceiver(SocketChannel socketChannel, Context context){
        this.socketChannel=socketChannel;
        this.context=context;

    }
    @Override
    public void run() {

        try {
            File file=openOrCreateDir();
            if(!file.exists()){
                file.mkdirs();
            }
            Log.d("NewBodyReceiver","NewBodyReceiver接受");
            Log.d("NewBodyReceiver","NewBodyReceiver接受");
            Log.d("NewBodyReceiver","NewBodyReceiver接受");
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(FileUtils.gerateLocalFile(file.getPath())));
            byte[] bytes = new byte[BYTE_SIZE_DATA];
            long total = 0;
            int len = 0;
            long sTime = System.currentTimeMillis();
            long eTime = 0;
            while ((len = socketChannel.read(ByteBuffer.wrap(bytes))) != -1) {


                ByteBuffer.wrap(bytes).flip();
                bufferedOutputStream.write(bytes, 0, len);
                total = total + len;
                eTime = System.currentTimeMillis();
                if (eTime - sTime > 200) { //大于500ms 才进行一次监听
                    sTime = eTime;

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }




    public File openOrCreateDir(){
        String cachePath;
        boolean isExternal= Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if(isExternal){
            cachePath=context.getApplicationContext().getExternalCacheDir().getPath();
        }else {
            cachePath=context.getApplicationContext().getCacheDir().getPath();
        }
        return new File(cachePath+File.separator+"haoyinreceive");
    }
}
