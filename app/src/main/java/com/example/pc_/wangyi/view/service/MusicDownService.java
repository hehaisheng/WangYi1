package com.example.pc_.wangyi.view.service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.pc_.wangyi.retrofit.ComSchedulers;
import com.example.pc_.wangyi.retrofit.RxBus;
import com.example.pc_.wangyi.utils.DataBaseHelper;
import com.example.pc_.wangyi.utils.DownMusicManager;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.functions.Action1;

/**
 * Created by pc- on 2017/5/26.
 */
public class MusicDownService extends Service {


    //new Handler(Looper.getMainLooper());非主线程的使用
    public DownMusicManager downMusicManager;
    public DataBaseHelper dataBaseHelper;
    public SQLiteDatabase sqLiteDatabase;
    public Cursor cursor;
    public RxBus rxBus;
    public File file,musicFile;

    public String musicUrl;


    //每块的长度
    public long aveCount,lastCount;
    public  long allMusic;
    public volatile  long currentLength=0;
    public long allLength,firstPosition,secondPosition,thridPosition;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        rxBus=RxBus.newInstance();
        new Thread(new Runnable() {
            @Override
            public void run() {
                musicUrl=intent.getStringExtra("DownUrl");

                String filename = "kaixin"+".mp3";//默认取一个文件名
                Log.d("在下载服务",filename);
                Log.d("在下载服务",filename);
                Log.d("在下载服务",filename);
                Log.d("在下载服务",filename);
                String path = Environment.getExternalStorageDirectory()
                        + "/haoyindownload/";
                File file = new File(path);
                // 如果SD卡目录不存在创建
                if (!file.exists()) {
                    file.mkdir();
                }
                String filepath=path+filename;
                musicFile= new File(filepath);
                dataBaseHelper=DataBaseHelper.newInstance(MusicDownService.this);
                sqLiteDatabase=dataBaseHelper.getWritableDatabase();
                cursor=sqLiteDatabase.query("DownPosition",new String[]{"fisrtposition","secondposition","thirdposition","allCount","hasdone"},"downurl=?",new String[]{musicUrl},null,null,null);
                //没有下载过

                if(cursor.getCount()==0) {
                    getMusicLength();
                    if(allLength>0) {
                        //如果没有下载过， firstPosition作为每块的长度
                        firstPosition=allLength/3;
                        secondPosition=firstPosition;
                        if(allLength%3!=0){
                            thridPosition=allLength-firstPosition*2;
                        }
                        else {
                            thridPosition=firstPosition;
                        }
                        startToDown(allLength);

                        allMusic=0;

                    }
                    else {
                        Log.d("在下载服务","获取文件长度开始请求,没有长度数据");
                        Log.d("在下载服务","获取文件长度开始请求,没有长度数据");
                        Log.d("在下载服务","获取文件长度开始请求,没有长度数据");
                    }
                }
                //下载过
                else{
                    while (cursor.moveToNext()) {
                        int hasdone=cursor.getInt(cursor.getColumnIndex("hasdone"));
                        //没有下载完成
                        if(hasdone==0){
                            firstPosition=Long.valueOf(cursor.getString(cursor.getColumnIndex("fisrtposition")));
                            secondPosition=Long.valueOf(cursor.getString(cursor.getColumnIndex("secondposition")));
                            thridPosition=Long.valueOf(cursor.getString(cursor.getColumnIndex("thirdposition")));
                            allLength=Long.valueOf(cursor.getString(cursor.getColumnIndex("allCount")));
                            aveCount=allLength/3;
                            lastCount=allLength-aveCount*2;
                            if(firstPosition!=aveCount){
                                //说明已经下载完成firstPosition已经下载的长度
                                startToDownMusic(1);}
                            if(secondPosition!=aveCount) {
                                //说明下载完成
                                startToDownMusic(2);}
                            if(thridPosition!=lastCount) {
                                //说明下载完成
                                startToDownMusic(3);}}}}}
        }).start();

        rxBus.tObservable(String.class)
                .compose(ComSchedulers.<String>applyIoSchedulers())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        currentLength+=Long.valueOf(s);
                        if(currentLength==allLength)
                        {
                            Log.d("判断","下载成功");
                            Log.d("判断","下载成功");
                            Log.d("判断","下载成功");
                            Log.d("判断","下载成功");
                        }

                    }
                });
        return 1;
    }

    public void getMusicLength() {
        Log.d("在下载服务","获取文件长度");
        Log.d("在下载服务","获取文件长度");
        Log.d("在下载服务","获取文件长度");

        Log.d("在下载服务","获取文件长度开始请求");
        Log.d("在下载服务","获取文件长度开始请求");
        Request request=new Request.Builder().url(musicUrl).build();
        try {
            OkHttpClient okHttpClient=new OkHttpClient.Builder().build();
            Call call=okHttpClient.newCall(request);
            Response response=call.execute();
            if(response!=null&&response.isSuccessful()) {
                allLength=response.body().contentLength();
                Log.d("在下载服务","获取文件长度"+allLength);
                Log.d("在下载服务","获取文件长度"+allLength);
                Log.d("在下载服务","获取文件长度"+allLength);
                response.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void startToDown(long le)
    {
        Log.d("在下载服务","开始下载，在没有下载的情况");
        Log.d("在下载服务","开始下载，在没有下载的情况");
        Log.d("在下载服务","开始下载，在没有下载的情况");
        Log.d("在下载服务","开始下载，在没有下载的情况");

        new Thread(new Runnable() {
            @Override
            public void run() {
                new DownMusicManager(musicFile,musicUrl,0,firstPosition-1).startDownMusic();
            }
        }).start();
       new Thread(new Runnable() {
           @Override
           public void run() {
               new DownMusicManager(musicFile,musicUrl,firstPosition,firstPosition*2-1).startDownMusic();
           }
       }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                new DownMusicManager(musicFile, musicUrl, firstPosition * 2, allLength - 1).startDownMusic();
            }
        }).start();


    }
    public void startToDownMusic(final int position)
   {
//        Log.d("在下载服务","开始下载，有下载的情况");
//        Log.d("在下载服务","开始下载，有下载的情况");
//        Log.d("在下载服务","开始下载，有下载的情况");
//        Log.d("在下载服务","开始下载，有下载的情况");
//        Log.d("在下载服务","开始下载，有下载的情况");
//
//        if(position==1)
//        {
//            downMusicManager.startDownMusic(file,musicUrl,firstPosition,aveCount-1);
//        }
//        if(position==2)
//        {
//            downMusicManager.startDownMusic(file,musicUrl,secondPosition,aveCount-1);
//        }
//        if(position==3)
//        {
//            downMusicManager.startDownMusic(file,musicUrl,thridPosition,lastCount-1);
//        }


    }




}
