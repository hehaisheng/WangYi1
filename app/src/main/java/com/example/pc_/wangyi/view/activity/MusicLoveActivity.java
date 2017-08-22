package com.example.pc_.wangyi.view.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.MusicDataItem;
import com.example.pc_.wangyi.presenter.MusicDataPresenter;
import com.example.pc_.wangyi.view.adapter.MusicLoveAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc- on 2017/5/16.
 */
public class MusicLoveActivity   extends BaseActivity{



    public RecyclerView listView;
    public MusicLoveAdapter musicListAdapter;
    public MusicDataPresenter musicDataPresenter;




    public List<MusicDataItem> loveData=new ArrayList<>();


    @Override
    public int getLayout() {
        return R.layout.musiclove_list;
    }


    @Override
    public void initView() {


        listView=(RecyclerView) findViewById(R.id.love_activity_musiclist);
        listView.setLayoutManager(new LinearLayoutManager(this));

    }



    @Override
    public void initData() {


        new Thread(new Runnable() {
            @Override
            public void run() {
                musicDataPresenter=new MusicDataPresenter(MusicLoveActivity.this);
                loveData=musicDataPresenter.getLoveList();
                if(loveData.size()>0){
                    musicListAdapter=new MusicLoveAdapter(R.layout.musiclove_item,loveData);
                    listView.setAdapter(musicListAdapter);

                }
            }
        }).start();


    }

    @Override
    public void initEvent() {

        listView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                Toast.makeText(MusicLoveActivity.this,"点击",Toast.LENGTH_SHORT).show();
                Intent intent1=new Intent(MusicLoveActivity.this,MusicPlayActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("current",i);
                bundle.putString("ActivityModel","LoveModel");
                intent1.putExtras(bundle);
                MusicLoveActivity.this.startActivity(intent1);
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
