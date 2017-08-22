package com.example.pc_.wangyi.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.FileInfo;
import com.example.pc_.wangyi.model.IpPortInfo;
import com.example.pc_.wangyi.retrofit.ComSchedulers;
import com.example.pc_.wangyi.retrofit.RxBus;
import com.example.pc_.wangyi.transfer.BaseTransfer;
import com.example.pc_.wangyi.transfer.Constant;
import com.example.pc_.wangyi.transfer.FileReceiver;
import com.example.pc_.wangyi.utils.ApManager;
import com.example.pc_.wangyi.utils.MyWifiManager;
import com.example.pc_.wangyi.utils.TextUtils;
import com.example.pc_.wangyi.view.MyApplication;
import com.example.pc_.wangyi.view.adapter.ReceiverAdapter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by pc- on 2017/5/23.
 */
public class ReceiverActivity  extends BaseActivity {




    public List<FileInfo>  fileInfos=new ArrayList<>();


    public RecyclerView recyclerView;
    public ReceiverAdapter  receiverAdapter;
    public Runnable mUdpServerRuannable;
    public  DatagramSocket mDatagramSocket1;
    public DatagramSocket mDatagramSocket2;
    public RxBus rxBus1;
    public Subscription subscription;
    public  MyThread myThread;



    public static final int REQUEST_CODE_WRITE_SETTINGS = 7879;





    @Override
    public int getLayout() {
        return R.layout.receiver_activity;
    }


    @Override
    public void initView() {

        recyclerView=(RecyclerView)  findViewById(R.id.receiver_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rxBus1=RxBus.newInstance();


    }

    @Override
    public void initData() {
        boolean permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permission = Settings.System.canWrite(this);
        } else {
            permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
        }
        if (permission) {
            init();
        }  else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                startActivityForResult(intent, REQUEST_CODE_WRITE_SETTINGS);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_SETTINGS}, REQUEST_CODE_WRITE_SETTINGS);
            }
        }

    }

    @Override
    public void initEvent() {

        subscription=rxBus1.tObservable(FileInfo.class)
                .compose(ComSchedulers.<FileInfo>applyIoSchedulers())
                .subscribe(new Action1<FileInfo>() {
                    @Override
                    public void call(FileInfo fileInfo) {
                        fileInfos.add(fileInfo);
                        receiverAdapter=new ReceiverAdapter(R.layout.receiver_item,fileInfos);
                        recyclerView.setAdapter(receiverAdapter);
                    }
                });
    }
    private void init(){


        MyWifiManager.getInstance().setContext(this).disableWifi();
        if(ApManager.isApOn(this)){
            ApManager.disableAp(this);
        }

        ApManager.isApOn(this);
        String ssid = TextUtils.isNullOrBlank(android.os.Build.DEVICE) ? Constant.DEFAULT_SSID : android.os.Build.DEVICE;
        ApManager.configApState(this, ssid);
        mUdpServerRuannable = createSendMsgToFileSenderRunnable();
        MyApplication.MAIN_EXECUTOR.execute(mUdpServerRuannable);

    }
    private Runnable createSendMsgToFileSenderRunnable(){
        return new Runnable() {
            @Override
            public void run() {
                try {
                    startFileReceiverServer(Constant.DEFAULT_SERVER_COM_PORT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void startFileReceiverServer(int serverPort) throws Exception{


        int count = 0;
        String localAddress = MyWifiManager.getInstance().setContext(this).getHotspotLocalIpAddress();
        while(localAddress.equals(Constant.DEFAULT_UNKOWN_IP) && count <  Constant.DEFAULT_TRY_TIME){
            Thread.sleep(1000);
            localAddress = MyWifiManager.getInstance().setContext(this).getHotspotLocalIpAddress();
            count ++;
        }

        mDatagramSocket1 = new DatagramSocket(serverPort);
        byte[] receiveData = new byte[1024];
        boolean has_data=true;
        while(has_data) {
            //不断接受数据
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            mDatagramSocket1.receive(receivePacket);
            String msg = new String(receivePacket.getData()).trim();
            InetAddress inetAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            //如果是发送端初始化成功
            if(msg.startsWith(Constant.MSG_FILE_RECEIVER_INIT)) {
                has_data = false;
                sendFileReceiverInitSuccessMsgToFileSender(new IpPortInfo(inetAddress, port));
                toReceiveData();
            }

//            }else{
//                if(msg.length()>0){
//
//                    //解析发送来的数据
//                    parseFileInfo(msg);
//                }
//            }

        }
    }

    public void sendFileReceiverInitSuccessMsgToFileSender(IpPortInfo ipPortInfo) throws Exception{

        mDatagramSocket2 = new DatagramSocket(ipPortInfo.getPort() +1);
        byte[] sendData ;
        InetAddress ipAddress = ipPortInfo.getInetAddress();
        sendData = Constant.MSG_FILE_RECEIVER_INIT_SUCCESS.getBytes(BaseTransfer.UTF_8);
        DatagramPacket sendPacket =
                new DatagramPacket(sendData, sendData.length, ipAddress, ipPortInfo.getPort());
        mDatagramSocket2.send(sendPacket);

    }



    private void parseFileInfo(String msg) {
        FileInfo fileInfo = FileInfo.toObject(msg);
        if(fileInfo != null){
            rxBus1.post(fileInfo);

        }
    }

    public  void toReceiveData()
    {

        myThread=new MyThread();
        myThread.start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                ServerSocket serverSocket;
//                try {
//                    serverSocket = new ServerSocket(Constant.DEFAULT_SERVER_PORT);
//                    while (!Thread.currentThread().isInterrupted()){
//                        Socket socket = serverSocket.accept();
//                        FileReceiver fileReceiver = new FileReceiver(socket);
//                        MyApplication.MAIN_EXECUTOR.execute(fileReceiver);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();}}}).start();
    }

    public  static class  MyThread extends Thread{

        private boolean mRunning = false;
        @Override
        public void run() {
            mRunning = true;
            while (mRunning) {
                ServerSocket serverSocket;
                try {
                    serverSocket = new ServerSocket(Constant.DEFAULT_SERVER_PORT);
                    while (!Thread.currentThread().isInterrupted()){
                        Socket socket = serverSocket.accept();
                        FileReceiver fileReceiver = new FileReceiver(socket);
                        MyApplication.MAIN_EXECUTOR.execute(fileReceiver);
                    }
                } catch (IOException e) {
                    e.printStackTrace();}
            }
        }
        public void close() {
            mRunning = false;
        }
    }
    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_WRITE_SETTINGS && Settings.System.canWrite(this)){
            init();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_WRITE_SETTINGS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            init();
        } else {

            Toast.makeText(this,"请允许权限才能传输",Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mDatagramSocket1!=null){
            mDatagramSocket1.close();
        }
        if(mDatagramSocket2!=null){
            mDatagramSocket2.close();
        }
        if(ApManager.isApOn(this)){
            ApManager.disableAp(this);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myThread.close();
        if(!subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
        if(rxBus1!=null){
            rxBus1=null;
        }

    }
}
