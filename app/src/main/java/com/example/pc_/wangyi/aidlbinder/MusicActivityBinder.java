package com.example.pc_.wangyi.aidlbinder;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.TextView;

import com.example.pc_.wangyi.IMusicCompleteListener;
import com.example.pc_.wangyi.IMusicNext;
import com.example.pc_.wangyi.IMusicPlay;
import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.MusicDataItem;
import com.example.pc_.wangyi.presenter.MusicDataPresenter;
import com.example.pc_.wangyi.retrofit.RxBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc- on 2017/6/2.
 */
public class MusicActivityBinder extends Activity {



    public TextView playText,nextText;


    public BinderPool binderPool;
    public IMusicPlay iMusicPlay;
    public IMusicNext iMusicNext;
    public MusicDataPresenter musicDataPresenter;


    public List<MusicDataItem> musicDataItems=new ArrayList<>();

    public String processName;
    public RxBus rxBus;


    //标志  music.163.com/outchain/player?type=2&id=28315670&auto=1



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.binder);
        initView();
        initEvent();
    }
    public void initView(){

        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) this
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {

                processName = appProcess.processName;
            }
        }

        rxBus=RxBus.newInstance();
        playText=(TextView)  findViewById(R.id.play_1);
        nextText=(TextView)  findViewById(R.id.next_1);
        musicDataPresenter=MusicDataPresenter.newInstance(this);
        musicDataItems=musicDataPresenter.getMusicData();
        playMusic();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                binderPool=BinderPool.newInstance(MusicActivityBinder.this);
//                binderPool.registerListener(iMusicCompleteListener);
//                rxBus.tObservable(Integer.class)
//                        .compose(ComSchedulers.<Integer>applyIoSchedulers())
//                        .subscribe(new Action1<Integer>() {
//                            @Override
//                            public void call(final Integer currentIndex) {
//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        IBinder iBinder=binderPool.queryBinder(0);
//                                        iMusicPlay= MusicPlayServiceBinder.MusicPlayImpl.asInterface(iBinder);
//                                        try {
//
//                                            iMusicPlay.play(musicDataItems.get(currentIndex+1).getMusicUrl(),currentIndex+1);
//                                        }catch (RemoteException e){
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }).start();
//                            }
//                        });
//            }
//        }).start();






    }
    public void initEvent(){
       playText.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {
                playMusic();


           }
       });
       nextText.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                   @Override
                   public void run() {
                         binderPool=BinderPool.newInstance(MusicActivityBinder.this);
                        iMusicNext=MusicPlayServiceBinder.MusicNextImpl.asInterface(binderPool.queryBinder(1));
                       try {
                            iMusicNext.next(musicDataItems.get(1).getMusicUrl(),1);
                     }
                       catch (RemoteException e){
                         e.printStackTrace();
                      }

                 }
               }).start();

          }
      });
   }


    public  void playMusic(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                binderPool=BinderPool.newInstance(MusicActivityBinder.this);
                IBinder iBinder=binderPool.queryBinder(0);
                iMusicPlay= MusicPlayServiceBinder.MusicPlayImpl.asInterface(iBinder);
                try {

                    iMusicPlay.play(musicDataItems.get(6).getMusicUrl(),7);
                }catch (RemoteException e){
                    e.printStackTrace();
                }

            }
        }).start();
    }

  public IMusicCompleteListener iMusicCompleteListener=new IMusicCompleteListener.Stub() {
      @Override
      public void complete(int currentIndex) throws RemoteException {
            rxBus.post(currentIndex);
      }

  };

    @Override
    protected void onDestroy() {
        super.onDestroy();



    }
}
