package com.example.pc_.wangyi.view.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.pc_.wangyi.database.FriendCommentModel;
import com.example.pc_.wangyi.model.FriendModel;
import com.example.pc_.wangyi.retrofit.RxBus;
import com.example.pc_.wangyi.utils.LiteOrmManager;
import com.example.pc_.wangyi.view.MyApplication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by pc- on 2017/7/4.
 */
public class FriendCommentService extends Service {

    public RxBus rxBus;
    public MulticastSocket multicastSocket;
    public DatagramPacket inPacket;
    public InetAddress inetAddress;
    public LiteOrmManager liteOrmManager;
    public List<FriendModel> friendModels = new CopyOnWriteArrayList<>();
    public  List<FriendCommentModel>  friendCommentModels=new ArrayList<>();


    final  String Broadcast_Ip="230.0.0.1";
    final int  Broadcastcast_Port=30001;
    final int data_len=4096;
    public  byte[]  inBuff;
    public MyApplication myApplication;
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
    public int onStartCommand(Intent intent, int flags, int startId) {

        myApplication=MyApplication.getNewInstance();
        liteOrmManager=LiteOrmManager.newInstance(this);
        rxBus=RxBus.newInstance();
        if(multicastSocket==null){
            initReceive();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void initReceive(){
         new Thread(new Runnable() {
             @Override
             public void run() {
                 try {
                     multicastSocket = new MulticastSocket(Broadcastcast_Port);
                     inetAddress = InetAddress.getByName(Broadcast_Ip);
                     inBuff = new byte[data_len];
                     inPacket = new DatagramPacket(inBuff, inBuff.length);
                     multicastSocket.joinGroup(inetAddress);
                     multicastSocket.setLoopbackMode(false);
                     while (true) {
                         //"NewCreate"+"好音好"+account+"好音好"+time+"好音好"+message;
                         //"评论好音好"+position+"好音好"+createTime
                         multicastSocket.receive(inPacket);
                         String receiverStr=new String(inBuff,0,inPacket.getLength());
                         String[] receiveString=receiverStr.split("好音好");
                         FriendCommentModel friendCommentModel=new FriendCommentModel();
                         //"NewCreate"+"好音好"+account+"好音好"+time+"好音好"+message;
                         if(receiveString[0].equals("NewCreate")){
                             friendCommentModel.setUserName(receiveString[1]);
                             friendCommentModel.setCreateTime(receiveString[2]);
                             friendCommentModel.setMessageContent(receiveString[3]);
                             friendCommentModel.setZanUserName("赞:");
                             friendCommentModel.setCommentText("评论:");
                             liteOrmManager.save(friendCommentModel);
                         }

                         rxBus.post(receiverStr);
                     }
                 }catch (IOException  e){
                     e.printStackTrace();
                 }
             }
         }).start();

    }


}
