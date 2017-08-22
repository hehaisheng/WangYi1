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
import com.example.pc_.wangyi.utils.QuickSort;
import com.example.pc_.wangyi.view.adapter.MusicListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc- on 2017/5/15.
 */
public class  MusicListActivity extends BaseActivity {



    public RecyclerView listView;
    public MusicListAdapter musicListAdapter;
    public MusicDataPresenter musicDataPresenter;
    public QuickSort quickSort;



    public List<MusicDataItem> musicDataItemList=new ArrayList<>();



    @Override
    public int getLayout() {
        return R.layout.musiclist_activity;
    }


    @Override
    public void initView() {


        listView=(RecyclerView) findViewById(R.id.activity_musiclist);
        listView.setLayoutManager(new LinearLayoutManager(this));
        quickSort=QuickSort.newInstance();

    }



    @Override
    public void initData() {


        musicDataPresenter=MusicDataPresenter.newInstance(this);
        musicDataItemList=musicDataPresenter.getMusicData();
        quickSort.quickSort(musicDataItemList,0,musicDataItemList.size()-1);
        musicDataItemList=myApplication.getGedanList();
        musicListAdapter=new MusicListAdapter(R.layout.musiclist_item,musicDataItemList);
        listView.setAdapter(musicListAdapter);



    }



    @Override
    public void initEvent() {

        listView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                Toast.makeText(MusicListActivity.this,"点击",Toast.LENGTH_SHORT).show();
                Intent intent1=new Intent(MusicListActivity.this,BinderPlayActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("current",i);
                intent1.putExtras(bundle);
                MusicListActivity.this.startActivity(intent1);
            }
        });


    }


}
