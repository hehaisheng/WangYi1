package com.example.pc_.wangyi.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.FileInfo;
import com.example.pc_.wangyi.retrofit.ComSchedulers;
import com.example.pc_.wangyi.retrofit.RxBus;
import com.example.pc_.wangyi.transfer.Constant;
import com.example.pc_.wangyi.utils.ListUtils;
import com.example.pc_.wangyi.utils.MyWifiManager;
import com.example.pc_.wangyi.utils.NetUtils;
import com.example.pc_.wangyi.view.MyApplication;
import com.example.pc_.wangyi.view.adapter.FindApAdapter;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by pc- on 2017/6/20.
 */
public class NewFindApActivity extends BaseActivity {


    public FindApAdapter findApAdapter;
    public  Runnable runnable;
    public DatagramSocket datagramSocket;
    public RxBus rxBus1;
    public Subscription subscription;
    public static final String UTF_8 = "UTF-8";
    public InetAddress inetAddress;



    List<ScanResult> scanResults = new ArrayList<>();




    public static final int REQUEST_CODE_OPEN_GPS = 205;
    public String ipStr;
    public int currentCount=0;




    @BindView(R.id.find_ap_list)
    RecyclerView findApList;

    @Override
    public int getLayout() {
        return R.layout.find_ap_activity;

    }

    @Override
    public void initView() {
        findApList.setLayoutManager(new LinearLayoutManager(this));
        rxBus1=RxBus.newInstance();
        if(!MyWifiManager.getInstance().setContext(this).isWifiEnable()) {
            MyWifiManager.getInstance().setContext(this).openWifi();
        }
        if (Build.VERSION.SDK_INT >= 23 ) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.LOCATION)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_WIFI_STATE,
                }, REQUEST_CODE_OPEN_GPS);
            }
        }else{
            startWifiScan();
            compositeSubscription.add(intervalScan());
        }

    }

    @Override
    public void initData() {

        subscription=rxBus1.tObservable(String.class)
                .compose(ComSchedulers.<String>applyIoSchedulers())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                         if(s.equals("Find-To-Transfer")){
                             Log.d("跳转","开始");
                             Log.d("跳转并进入","开始");
                             Log.d("跳转并进入","开始");
                             Toast.makeText(NewFindApActivity.this,"进入文件发送列表",Toast.LENGTH_SHORT).show();
                              Intent toFileIntent=new Intent(NewFindApActivity.this,TrasferFileActivity.class);
                              startActivity(toFileIntent);
                         }

                    }
                });
    }

    @Override
    public void initEvent() {

        findApList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                ScanResult scanResult1 = scanResults.get(i);
                String  ssid = scanResult1.SSID;
                MyWifiManager.getInstance().setContext(NewFindApActivity.this).openWifi();
                MyWifiManager.getInstance().setContext(NewFindApActivity.this).addNetwork(MyWifiManager.createWifiCfg(ssid, null, MyWifiManager.WIFICIPHER_NOPASS));
                ipStr=MyWifiManager.getInstance().setContext(NewFindApActivity.this).getIpAddressFromHotspot();

               //发送信息给接受端
                runnable= sendMessageToServerRunnable();
                MyApplication.MAIN_EXECUTOR.execute(runnable);
            }
        });
    }


   public void startWifiScan(){

        MyWifiManager.getInstance().setContext(this).startScan();
       scanResults = MyWifiManager.getInstance().setContext(this).getScanResultList();
       currentCount=scanResults.size();
       scanResults = ListUtils.filterWithNoPassword(scanResults);
       if (scanResults != null) {
           findApAdapter = new FindApAdapter(R.layout.find_ap_item, scanResults);
           findApList.setAdapter(findApAdapter);
       }

   }
    public Subscription intervalScan(){

        return  Observable.interval(0, 2000, TimeUnit.MILLISECONDS)
                .compose(ComSchedulers.<Long>applyIoSchedulers())
                .flatMap(new Func1<Long, Observable<ScanResult>>() {
                    @Override
                    public Observable<ScanResult> call(Long aLong) {
                        MyWifiManager.getInstance().setContext(NewFindApActivity.this).startScan();
                        List<ScanResult> scanResults1 = MyWifiManager.getInstance().setContext(NewFindApActivity.this).getScanResultList();
                        return Observable.from(ListUtils.filterWithNoPassword(scanResults1));
                    }
                })
                .subscribe(new Action1<ScanResult>() {
                    @Override
                    public void call(ScanResult scanResult) {
                        if(scanResult!=null){
                           for(int i=0;i<scanResults.size();i++){
                               if((!scanResult.BSSID.equals(scanResults.get(i).BSSID))&&i==currentCount-1){
                                   scanResults.add(scanResult);
                                   currentCount=scanResults.size();
                                   findApAdapter = new FindApAdapter(R.layout.find_ap_item, scanResults);
                                   findApList.setAdapter(findApAdapter);
                               }
                           }


                        }


                    }
                });
    }
    public Runnable sendMessageToServerRunnable(){
        return new Runnable(){
            @Override
            public void run() {
                try {

                    int count = 0;
                    while(ipStr.equals(Constant.DEFAULT_UNKOWN_IP) && count < Constant.DEFAULT_TRY_TIME){
                        Thread.sleep(1000);
                        ipStr=MyWifiManager.getInstance().setContext(NewFindApActivity.this).getIpAddressFromHotspot();
                        count ++;
                    }
                    count = 0;
                    while(!NetUtils.pingIpAddress(ipStr) && count < Constant.DEFAULT_TRY_TIME){
                        Thread.sleep(500);
                        count ++;
                    }
                    while(!NetUtils.pingIpAddress(ipStr) && count < Constant.DEFAULT_TRY_TIME){
                        Thread.sleep(500);
                        count ++;
                    }
                  //  rxBus.post("Find-To-Transfer");
                    datagramSocket = new DatagramSocket(Constant.DEFAULT_SERVER_COM_PORT);
                    byte[] receiveData = new byte[1024];
                    byte[] sendData ;
                    inetAddress = InetAddress.getByName(ipStr);

                   // sendFileInfoToServer();
                    //初始化成功的信息
                    sendData = Constant.MSG_FILE_RECEIVER_INIT.getBytes(UTF_8);
                    DatagramPacket sendPacket =
                            new DatagramPacket(sendData, sendData.length,inetAddress,Constant.DEFAULT_SERVER_COM_PORT);

                    //初始化好信息

                   datagramSocket.send(sendPacket);

                    boolean has_back=false;
                    while(!has_back) {

                        //等待接收方初始化成功

                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        datagramSocket.receive(receivePacket);
                        String response = new String(receivePacket.getData(), UTF_8).trim();
                        if(response.equals(Constant.MSG_FILE_RECEIVER_INIT_SUCCESS)){


                            has_back=true;

                            rxBus1.post("Find-To-Transfer");

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
    public void sendFileInfoToServer(){


        //发送文件信息给接受端
        Map<String, FileInfo> sendFileInfoMap = MyApplication.getNewInstance().getFileInfoMap();
        List<Map.Entry<String, FileInfo>> fileInfoMapList = new ArrayList<>(sendFileInfoMap.entrySet());
        Collections.sort(fileInfoMapList, Constant.DEFAULT_COMPARATOR);
        for(Map.Entry<String, FileInfo> entry : fileInfoMapList){
            if(entry.getValue() != null ){
                FileInfo fileInfo = entry.getValue();
                String fileInfoStr = FileInfo.toJsonStr(fileInfo);
                DatagramPacket sendFileInfoListPacket =
                        new DatagramPacket(fileInfoStr.getBytes(), fileInfoStr.getBytes().length, inetAddress,Constant.DEFAULT_SERVER_COM_PORT);
                try{
                   datagramSocket.send(sendFileInfoListPacket);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_OPEN_GPS) {
            if (grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
               startWifiScan();
            } else {
                Toast.makeText(NewFindApActivity.this,"权限不被允许",Toast.LENGTH_SHORT).show();

            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void closeSocket(){
        if(datagramSocket!= null){
            datagramSocket.disconnect();
            datagramSocket.close();
            datagramSocket = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
        if(rxBus1!=null){
            rxBus1=null;
        }
        closeSocket();

    }
}
