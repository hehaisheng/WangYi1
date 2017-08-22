package com.example.pc_.wangyi.transfer;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.pc_.wangyi.model.FileInfo;
import com.example.pc_.wangyi.retrofit.RxBus;
import com.example.pc_.wangyi.utils.ApkUtils;
import com.example.pc_.wangyi.utils.FileUtils;
import com.example.pc_.wangyi.utils.ScreenshotUtils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by pc- on 2017/6/20.
 */
public class NewHeaderSender  implements Runnable {


    public static final String SPERATOR = "::";
    public static final int BYTE_SIZE_HEADER    = 1024 * 10;
    public static final int BYTE_SIZE_SCREENSHOT    = 1024 * 40;
    public static final int TYPE_FILE = 1; //文件类型
    public static final String UTF_8 = "UTF-8";


    Context mContext;
    public RxBus rxBus;
    private FileInfo mFileInfo;
    private Socket mSocket;
    private OutputStream mOutputStream;


    private String mServerIpAddress;
    private int mPort;








    public NewHeaderSender (Context context, FileInfo mFileInfo, String mServerIpAddress, int mPort) {
        this.mContext = context;
        this.mFileInfo = mFileInfo;
        this.mServerIpAddress = mServerIpAddress;
        this.mPort = mPort;
    }



    @Override
    public void run() {

        //初始化
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //解析头部
        try {
            //先把传送的文件的头部数据传送
            parseHeader();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //结束
        try {
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() throws Exception {
        this.mSocket = new Socket(mServerIpAddress, mPort);
        OutputStream os = this.mSocket.getOutputStream();
        mOutputStream = new BufferedOutputStream(os);
    }


    public void parseHeader() throws Exception {

        StringBuilder headerSb = new StringBuilder();
        String jsonStr = FileInfo.toJsonStr(mFileInfo);
        jsonStr = TYPE_FILE + SPERATOR + jsonStr;
        headerSb.append(jsonStr);
        int leftLen = BYTE_SIZE_HEADER - jsonStr.getBytes(UTF_8).length;
        for(int i=0; i < leftLen; i++){
            headerSb.append(" ");
        }
        byte[] headbytes = headerSb.toString().getBytes(UTF_8);
        mOutputStream.write(headbytes);
        StringBuilder screenshotSb = new StringBuilder();
        int ssByteArraySize = 0;
        if(mFileInfo != null){
            Bitmap screenshot = null;
            byte[] bytes ;
            if(FileUtils.isApkFile(mFileInfo.getFilePath())){ //apk 缩略图处理
                Bitmap bitmap = ApkUtils.drawableToBitmap(ApkUtils.getApkThumbnail(mContext, mFileInfo.getFilePath()));
                screenshot = ScreenshotUtils.extractThumbnail(bitmap, 96, 96);
            }else if(FileUtils.isJpgFile(mFileInfo.getFilePath())) { //jpg 缩略图处理
                screenshot = FileUtils.getScreenshotBitmap(mContext, mFileInfo.getFilePath(), FileInfo.TYPE_JPG);
                screenshot = ScreenshotUtils.extractThumbnail(screenshot, 96, 96);
            }else if(FileUtils.isMp3File(mFileInfo.getFilePath())) { //mp3 缩略图处理
                //DO NOTHING mp3文件可以没有缩略图 可指定
                screenshot = FileUtils.getScreenshotBitmap(mContext, mFileInfo.getFilePath(), FileInfo.TYPE_MP3);
                screenshot = ScreenshotUtils.extractThumbnail(screenshot, 96, 96);
            }else if(FileUtils.isMp4File(mFileInfo.getFilePath())) { //mp4 缩略图处理
                screenshot = FileUtils.getScreenshotBitmap(mContext, mFileInfo.getFilePath(), FileInfo.TYPE_MP4);
                screenshot = ScreenshotUtils.extractThumbnail(screenshot, 96, 96);
            }

            if(screenshot != null){
                bytes = FileUtils.bitmapToByteArray(screenshot);
                ssByteArraySize = bytes.length;
                mOutputStream.write(bytes);
            }
        }

        int ssLeftLen = BYTE_SIZE_SCREENSHOT - ssByteArraySize; //缩略图剩余的字节数
        for(int i=0; i < ssLeftLen; i++){
            screenshotSb.append(" ");
        }
        byte[] screenshotBytes = screenshotSb.toString().getBytes(UTF_8);
        mOutputStream.write(screenshotBytes);


    }

    public void finish() {
        if(mOutputStream != null){
            try {
                mOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(mSocket != null && mSocket.isConnected()){
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
