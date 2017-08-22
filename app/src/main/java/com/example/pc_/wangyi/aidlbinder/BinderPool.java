package com.example.pc_.wangyi.aidlbinder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.pc_.wangyi.IMusicCompleteListener;
import com.example.pc_.wangyi.IMusicServiceBinders;

import java.util.concurrent.CountDownLatch;

/**
 * Created by pc- on 2017/6/2.
 */
public class BinderPool {



    public Context mContext;
    public  IMusicServiceBinders iMusicServiceBinders;
    public static volatile BinderPool binderPool;
    public CountDownLatch countDownLatch;
    public static BinderPool newInstance(Context context){
        if(binderPool==null){
            synchronized (BinderPool.class){
                if(binderPool==null){
                    binderPool=new BinderPool(context);
                }
            }
        }
        return binderPool;
    }
    public BinderPool(Context context){
        mContext=context.getApplicationContext();
       // mContext=context;
        connectPlayService();

    }
    public synchronized void connectPlayService()  {

        countDownLatch=new CountDownLatch(1);
        Intent intent=new Intent(mContext,MusicPlayServiceBinder.class);
        mContext.bindService(intent,serviceConnection,Context.BIND_AUTO_CREATE);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
    public  ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iMusicServiceBinders= IMusicServiceBinders.Stub.asInterface(service);
            try {
                iMusicServiceBinders.asBinder().linkToDeath(mBinderPoolDeathRecipent,0);
            }
            catch (RemoteException e){
                e.printStackTrace();
            }
            countDownLatch.countDown();


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public  IBinder.DeathRecipient mBinderPoolDeathRecipent=new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            iMusicServiceBinders.asBinder().unlinkToDeath(mBinderPoolDeathRecipent,0);
            iMusicServiceBinders=null;
            connectPlayService();
        }
    };
    public IBinder queryBinder(int binderCode){
        IBinder iBinder=null;
        try {
            if(iMusicServiceBinders!=null){
                iBinder=iMusicServiceBinders.queryBinder(binderCode);

            }
        }catch (RemoteException e){
            e.printStackTrace();
        }
        return  iBinder;
    }
    public  void registerListener(IMusicCompleteListener iMusicCompleteListener){
        try {
            if(iMusicServiceBinders!=null){
               iMusicServiceBinders.registerListener(iMusicCompleteListener);
            }
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }
    public  void unregisterListener(){
        try {
            if(iMusicServiceBinders!=null){
                iMusicServiceBinders.unregisterListener();
            }
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public void unbind(){

        mContext.unbindService(serviceConnection);
    }


}
