package com.example.pc_.wangyi.view.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.pc_.wangyi.model.MusicDataItem;
import com.example.pc_.wangyi.model.NetMusicItem;
import com.example.pc_.wangyi.presenter.MusicDataPresenter;
import com.example.pc_.wangyi.retrofit.ComSchedulers;
import com.example.pc_.wangyi.retrofit.RxBus;
import com.example.pc_.wangyi.utils.DataBaseHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;


/**
 * Created by pc- on 2017/5/15.
 */
public class MusicPlayService  extends Service {





    public List<MusicDataItem> musicDataItems=new ArrayList<>();

    public MediaPlayer mediaPlayer;
    public DataBaseHelper dataBaseHelper;
    public MusicDataPresenter musicDataPresenter;
    public SQLiteDatabase db;
    public NetMusicItem netMusicItem;
    public RxBus rxBus;


    public boolean isNet=false;
    public int currentMusicIndex;
    public String activityModel;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        Log.d("绑定成功", "onStartCommand: ");
        Log.d("绑定成功", "onStartCommand: ");
        Log.d("绑定成功", "onStartCommand: ");

        if(mediaPlayer==null){
            mediaPlayer=new MediaPlayer();
        }
        new  Thread(new Runnable() {
            @Override
            public void run() {

                rxBus=RxBus.newInstance();
                musicDataPresenter=MusicDataPresenter.newInstance(MusicPlayService.this);
                dataBaseHelper=new DataBaseHelper(MusicPlayService.this);

                currentMusicIndex=intent.getIntExtra("current",0);
                activityModel=intent.getStringExtra("ActivityModel");
                if(activityModel.equals("LoveModel")){
                    musicDataItems=musicDataPresenter.getLoveList();
                }else{
                    musicDataItems=musicDataPresenter.getMusicData();
                }
                isNet=intent.getBooleanExtra("NetMusic",false);
                if(isNet){
                    netMusicItem=new NetMusicItem();
                    if(currentMusicIndex<netMusicItem.musicName.length) {
                        play(currentMusicIndex);
                    }
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                    {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            nextSong();
                        }
                    });
                }
                else {
                    db=dataBaseHelper.getWritableDatabase();
                    if(currentMusicIndex<musicDataItems.size()) {
                        play(currentMusicIndex);
                    }
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {

                            String postStr=musicDataItems.get(currentMusicIndex+1).getMusicName()+"-"+musicDataItems.get(currentMusicIndex+1).getMusicArt();
                            rxBus.post("Complete-"+currentMusicIndex);
                            rxBus.post("ChangeNameAndArt-"+postStr);
                            String name=musicDataItems.get(currentMusicIndex).getMusicName();
                            Cursor cursor2=db.query("MusicCount",new String[]{"count"},"musiccountname=?",new String[]{name},null,null,null);

                            Log.d("开始保存的逻辑","开始");
                            Log.d("开始保存的逻辑","开始");
                            Log.d("开始保存的逻辑","开始");
                            if(cursor2.getCount()==0) {
                                Log.d("没有数据时保存","结束");
                                Log.d("没有数据时保存","结束");
                                Log.d("没有数据时保存","结束");
                                ContentValues contentValues=new ContentValues();
                                contentValues.put("musiccountname",name);
                                contentValues.put("count",1);
                                db.insert("MusicCount",null,contentValues);
                                cursor2.close();

                            }
                            else {
                                while (cursor2.moveToNext()) {
                                    Log.d("保存","开始");
                                    Log.d("保存","开始");
                                    Log.d("保存","开始");
                                    ContentValues contentValues1=new ContentValues();
                                    int count=cursor2.getInt(cursor2.getColumnIndex("count"));
                                    contentValues1.put("count",count+1);
                                    db.update("MusicCount",contentValues1,"musiccountname=?",new String[]{name});
                                    Log.d("保存","结束");
                                    Log.d("保存","结束");
                                    Log.d("保存","结束");

                                }
                                cursor2.close();
                            }
                            nextSong();
                        }});
                }
                rxBus.tObservable(String.class).compose(ComSchedulers.<String>applyIoSchedulers())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                if(s.equals("Pause")){
                                    pause();
                                }else  if(s.equals("Playing")){
                                    play(currentMusicIndex);
                                }else if(s.contains("Play-")){
                                    String[] strings=s.split("-");
                                    play(Integer.valueOf(strings[1]));
                                }
                            }
                        });
            }
        }).start();

        return super.onStartCommand(intent,flags,  startId);
    }



    public void play(int musicIndex) {
          currentMusicIndex=musicIndex;
          mediaPlayer.reset();
        try {
            if(isNet)
            {

                mediaPlayer.setDataSource("http://dl.stream.qqmusic.qq.com/C400003uB0Lh0pexs8.m4a?vkey=25F21E7F4390871B0BA65BF75CA381BB2BB5985D731A19A2B751E06F6C18B7254090A282E432EDA99B255106E2346A745B6C1643CDB8FBEA&guid=688731706&uin=0&fromtag=66");
                //mediaPlayer.setDataSource(netMusicItem.musicUrl[currentMusicIndex]);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }
            else {
              mediaPlayer.setDataSource(musicDataItems.get(musicIndex).getMusicUrl());
            }
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


    }

    public void nextSong() {

        if(isNet){
             if(currentMusicIndex==netMusicItem.musicArt.length-1) {
                 currentMusicIndex=0;
             }
            else{
                 currentMusicIndex++;
             }
        }
        else {
            if(currentMusicIndex==musicDataItems.size()-1) {
                currentMusicIndex=0;
            }
            else {
                currentMusicIndex++;
            }
        }

        Log.d("下一首","开始");
        Log.d("下一首","开始");
        Log.d("下一首","开始");
        play(currentMusicIndex);

    }
    public void pause(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (rxBus!=null){
            rxBus=null;
        }
    }
}
