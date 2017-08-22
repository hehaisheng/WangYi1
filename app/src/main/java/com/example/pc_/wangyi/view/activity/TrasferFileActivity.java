package com.example.pc_.wangyi.view.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.FileInfo;
import com.example.pc_.wangyi.retrofit.ComSchedulers;
import com.example.pc_.wangyi.retrofit.RxBus;
import com.example.pc_.wangyi.transfer.Constant;
import com.example.pc_.wangyi.transfer.FileSender;
import com.example.pc_.wangyi.utils.MyWifiManager;
import com.example.pc_.wangyi.view.MyApplication;
import com.example.pc_.wangyi.view.adapter.TransferFileAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by pc- on 2017/5/23.
 */
public class TrasferFileActivity extends BaseActivity {


    //数据结构

    // List<Map.Entry<String, FileInfo>> mFileInfoMapList;
    List<FileSender> mFileSenderList = new ArrayList<>();
    public List<Map.Entry<String, FileInfo>> fileInfoMapList;


    //关键类
    public TransferFileAdapter transferAdapter;
    public RecyclerView recyclerView;
    public RxBus rxBus1;
    public Subscription subscription;



    //标志
    public static final int REQUEST_CODE_WRITE_FILE = 200;
    public boolean isTransfering = false;



    @BindView(R.id.transfer_mb)
    TextView transferMb;
    @BindView(R.id.transfer_sudu)
    TextView transferSudu;
    @BindView(R.id.transfer_alltime)
    TextView transferAllTime;

    public long allSize;
    public long allTime=0;

    @Override
    public int getLayout() {
        return R.layout.transfer_file_activity;
    }

    //显示发送事件的界面


    @Override
    public void initView() {


        rxBus1=RxBus.newInstance();
        recyclerView = (RecyclerView) findViewById(R.id.transfer_file_recyc);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fileInfoMapList = new ArrayList<>(MyApplication.getNewInstance().getFileInfoMap().entrySet());
        //mFileInfoMapList = fileInfoMapList;
        transferAdapter = new TransferFileAdapter(R.layout.transfer_file_item, MyApplication.getNewInstance().getFileInfos());
        recyclerView.setAdapter(transferAdapter);
        for (Map.Entry<String, FileInfo> entry : fileInfoMapList) {
            final FileInfo fileInfo = entry.getValue();
               allSize+=fileInfo.getSize();

        }

        float f=(float) (allSize*1.00)/1024/1024;
        String s=String.valueOf(f);
//        String[] time1=s.split(".");
//        String mbText=time1[0]+"."+time1[1].substring(0,2);
        transferMb.setText(s+"M");

    }

    @Override
    public void initData() {

        Collections.sort(fileInfoMapList, Constant.DEFAULT_COMPARATOR);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_FILE);
        } else {
            initSendServer(fileInfoMapList);//开启传送文件
        }

    }

    @Override
    public void initEvent() {
        subscription=rxBus1.tObservable(String.class)
                .compose(ComSchedulers.<String>applyIoSchedulers())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {

                        if (s.contains("发送速度")) {
                            String[] tempStr=s.split("-");
                            String suduText=tempStr[1].substring(0,3)+"M/s";
                            transferSudu.setText(suduText);
                        }else if(s.contains("完成时间")){
                            String[]  strings=s.split("-");
                            String comText=strings[1].substring(0,4)+"S";
                            transferAllTime.setText(comText);

                        }

                    }
                });



    }

    private void initSendServer(List<Map.Entry<String, FileInfo>> fileInfoMapList) {
        String serverIp = MyWifiManager.getInstance().setContext(this).getIpAddressFromHotspot();
        for (Map.Entry<String, FileInfo> entry : fileInfoMapList) {
            final FileInfo fileInfo = entry.getValue();
            FileSender fileSender = new FileSender(this, fileInfo, serverIp, Constant.DEFAULT_SERVER_PORT);
            mFileSenderList.add(fileSender);
            fileSender.setOnSendListener(new FileSender.OnSendListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onProgress(long progress, long total) {

                }

                @Override
                public void onSuccess(FileInfo fileInfo) {

                }

                @Override
                public void onFailure(Throwable t, FileInfo fileInfo) {

                }

                @Override
                public void complete(long time) {
                       allTime+=time;
                       float realTime=(float)(allTime*1.00)/1000;
                       String timeStr=String.valueOf(realTime);
                       rxBus1.post("完成时间-"+timeStr);

                }
            });
            MyApplication.FILE_SENDER_EXECUTOR.execute(fileSender);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_WRITE_FILE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initSendServer(fileInfoMapList);//开启传送文件
            } else {
                // Permission Denied
                Toast.makeText(this, "请允许权限才能传输", Toast.LENGTH_SHORT).show();

            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        for (FileSender fileSender : mFileSenderList) {
            if (fileSender.isRunning()) {
                isTransfering = true;
                Toast.makeText(TrasferFileActivity.this, "正在传输音乐", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (!isTransfering) {
            if (MyWifiManager.getInstance().setContext(this).isWifiEnable()) {
                MyWifiManager.getInstance().setContext(this).disconnectCurrentNetwork();
            }
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFileSenderList.size() != 0) {
            for (FileSender fileSender : mFileSenderList) {
                fileSender.stop();
            }
        }
        if(!subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
        if(rxBus1!=null){

            rxBus1=null;
        }

    }



}
