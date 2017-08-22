package com.example.pc_.wangyi.aidlbinder;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.example.pc_.wangyi.IMusicCompleteListener;
import com.example.pc_.wangyi.IMusicLast;
import com.example.pc_.wangyi.IMusicNext;
import com.example.pc_.wangyi.IMusicPlay;
import com.example.pc_.wangyi.IMusicPuase;
import com.example.pc_.wangyi.IMusicServiceBinders;

import java.io.IOException;

/**
 * Created by pc- on 2017/6/2.
 */
public class MusicPlayServiceBinder extends Service {



    public MediaPlayer mediaPlayer;
    public IMusicCompleteListener iMusicCompleteListener;

    public String musicUrl;
    public int currentIndex1;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    public MusicPlayServiceBinder(){
        init();
    }

    public void init(){


        if(mediaPlayer==null){
            mediaPlayer=new MediaPlayer();
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //监听告诉客户端

                    nextSong(musicUrl,currentIndex1);
                   // iMusicCompleteListener.complete(currentIndex1);

              //  nextSong(musicUrl);

            }
        });
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public Binder binder=new IMusicServiceBinders.Stub(){
        @Override
        public IBinder queryBinder(int code) throws RemoteException {
            if(code==0){
                return new MusicPlayImpl();
            }
            else if(code==1){
                return  new MusicNextImpl();
            }else if(code==2){
                return new MusicLastImpl();
            }else if(code==3){
                return  new MusicPauseImpl();
            }
            return null;
        }

        @Override
        public void registerListener(IMusicCompleteListener iMusicCompleteListener1) throws RemoteException{

            MusicPlayServiceBinder.this.iMusicCompleteListener=iMusicCompleteListener1;
        }

        @Override
        public void unregisterListener() throws RemoteException {
            MusicPlayServiceBinder.this.iMusicCompleteListener=null;

        }

    };


    public void playSong(String url,int  currentIndex){

        currentIndex1=currentIndex;
        mediaPlayer.reset();
        try {
            musicUrl=url;
            mediaPlayer.setDataSource(url);
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
    public void nextSong(String url,int currentIndex){

        playSong(url, currentIndex);

    }



    public class MusicPlayImpl  extends IMusicPlay.Stub{

        @Override
        public void play(String url,int currentIndex) throws RemoteException {
            playSong(url,currentIndex);

        }
    }
    public class MusicNextImpl extends IMusicNext.Stub{

        @Override
        public void next(String url,int currentIndex) throws RemoteException {
            nextSong(url,currentIndex);

        }
    }
    public class MusicLastImpl extends IMusicLast.Stub{

        @Override
        public void last(String url, int currentIndex) throws RemoteException {
            playSong(url,currentIndex);

        }
    }
    public class MusicPauseImpl  extends IMusicPuase.Stub{

        @Override
        public void pause() throws RemoteException {

            if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
                mediaPlayer.pause();
            }

        }
    }


}
