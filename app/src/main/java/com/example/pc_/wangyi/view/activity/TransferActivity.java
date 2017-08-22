package com.example.pc_.wangyi.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.FileInfo;
import com.example.pc_.wangyi.model.MusicDataItem;
import com.example.pc_.wangyi.presenter.MusicDataPresenter;
import com.example.pc_.wangyi.view.adapter.TransferAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc- on 2017/5/23.
 */
public class TransferActivity extends BaseActivity {


    //选择需要传输的数据


    public static final int  REQUEST_CODE_WRITE_FILE = 200;

    public TransferAdapter transferAdapter;
    public RecyclerView recyclerView;
    public MusicDataPresenter musicDataPresenter;

    public TextView nextText;

    public List<FileInfo> fileInfoList=new ArrayList<>();
    public List<MusicDataItem> musicDataItems=new ArrayList<>();


    @Override
    public int getLayout() {
        return R.layout.transfer_activity;
    }


    @Override
    public void initView() {

        nextText=(TextView) findViewById(R.id.to_fengxiang);
        musicDataItems=myApplication.getGedanList();
        if(musicDataItems.size()==0){
            musicDataPresenter= MusicDataPresenter.newInstance(this);
            musicDataItems= musicDataPresenter.getMusicData();
        }
        recyclerView=(RecyclerView) findViewById(R.id.fengxiang_activity_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        transferAdapter=new TransferAdapter(R.layout.fengxiang_item,musicDataItems);
        recyclerView.setAdapter(transferAdapter);

    }

    @Override
    public void initData() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_FILE);
        }else{
            //初始化
            Toast.makeText(this,"版本可使用快传分享音乐了",Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void initEvent() {

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                   MusicDataItem musicDataItem=musicDataItems.get(i);
                   FileInfo fileInfo=new FileInfo();
                   fileInfo.setFilePath(musicDataItem.getMusicUrl());
                   fileInfo.setSize(musicDataItem.getSize());
                   fileInfo.setName(musicDataItem.getMusicName());
                   fileInfo.setMusicArt(musicDataItem.getMusicArt());
                   fileInfo.setFileType(3);
                   myApplication.addFileInfo(fileInfo);
                   fileInfoList.add(fileInfo);
                   myApplication.setFileInfos(fileInfo);
                  Toast.makeText(TransferActivity.this,"添加数据",Toast.LENGTH_SHORT).show();


            }
        });
        nextText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到查找文件接收者的界面
                Intent findIntent=new Intent(TransferActivity.this,NewFindApActivity.class);
                startActivity(findIntent);
            }
        });

    }

    //申请回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_WRITE_FILE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //成功的话，显示给用户,使用的是这里的
                Toast.makeText(this,"可使用快传分享音乐了",Toast.LENGTH_SHORT).show();
            } else {
                // Permission Denied
                Toast.makeText(this,"请允许权限才能传输",Toast.LENGTH_SHORT).show();
                finish();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
