package com.example.pc_.wangyi.utils;

import android.util.Log;

import com.example.pc_.wangyi.retrofit.RxBus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by pc- on 2017/5/26.
 */
public class DownMusicManager  {

    //可使用非阻塞模式创建单例

    //存放下载call的map，方便管理
    public Map<String, Call> callMap=new HashMap<>();
    //用来创建下载的线程
    public OkHttpClient okHttpClient;

    public  File file;
    public long start;
    public long end;
    public String musicUrl;
    public RxBus rxBus;

    public DownMusicManager(File saveFile, String musicUrl, long  start, long  end)
    {
        callMap=new HashMap<>();
        okHttpClient=new OkHttpClient.Builder().build();
        rxBus=RxBus.newInstance();
        this.start=start;
        this.end=end;
        this.file=saveFile;
        this.musicUrl=musicUrl;
    }
    //这里边下载，边保存下载的数据

    public void startDownMusic()
    {


        long startmusic=0;
        //创建一个请求，其实就是封装了我们平时写的请求
        Request request=new Request.Builder().addHeader("RANGE","bytes="+start+"-"+end)
                .url(musicUrl)
                .build();
        //创建一个执行者
        Call call=okHttpClient.newCall(request);
        //Response response = call.execute();同步请求
        //异步请求，response的数据，是一次请求获取的，比如一次20k,不是一次request
        try
        {
            Log.d("在下载管理", Thread.currentThread().getName()+end);
            Log.d("在下载管理", Thread.currentThread().getName()+end);
            Log.d("在下载管理", Thread.currentThread().getName()+end);
            Log.d("在下载管理","正在下载");
            Log.d("在下载管理","正在下载");
            Log.d("在下载管理","正在下载");
            Response response=call.execute();
            InputStream inputStream;
            RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rwd");
            randomAccessFile.seek(start);
            inputStream=response.body().byteStream();
            byte[] buff=new byte[4096];
            int len;
            while ((len=inputStream.read(buff))!=-1)
            {
                startmusic+=len;
                if(startmusic==end)
                {
                    Log.d("在下载管理","正在下载成功");
                    Log.d("在下载管理","正在下载成功");
                    Log.d("在下载管理","正在下载成功");
                }
                Log.d("在下载管理","正在下载"+len);
                Log.d("在下载管理","正在下载"+len);
                Log.d("在下载管理","正在下载"+len);
                Log.d("在下载管理","正在下载"+len);
               randomAccessFile.write(buff,0,len);
                rxBus.post(String.valueOf(len));

            }
           randomAccessFile.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }


    }

}
