package com.example.pc_.wangyi.view.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.DouBanNews;
import com.example.pc_.wangyi.retrofit.FetchManager;
import com.example.pc_.wangyi.view.adapter.DouBanAdapter;

import java.util.ArrayList;

/**
 * Created by pc- on 2017/5/19.
 */
public class DouBanActivity extends BaseActivity{


    public DouBanAdapter douBanAdapter;



    public ArrayList<DouBanNews.posts> jixueList=new ArrayList<>();
    public  ArrayList<DouBanNews.posts> commomDouBan=new ArrayList<>();

    public RecyclerView listView;

    public String type;
    public boolean isSuccess=false;



    @Override
    public int getLayout() {
        return R.layout.douban_activity;
    }


    @Override
    public void initView() {


        type=getIntent().getExtras().getString("DouBanType");
        listView=(RecyclerView) findViewById(R.id.douban_actiity_list);
        listView.setLayoutManager(new LinearLayoutManager(this));




    }

    @Override
    public void initData() {
        if(type.equals("SuiWen")) {
            douBanAdapter=new DouBanAdapter(this,R.layout.douban_item,myApplication.getDouBanNewses().getPosts());
            commomDouBan=myApplication.getDouBanNewses().getPosts();
            listView.setAdapter(douBanAdapter);
            isSuccess=true;
        }
        else if(type.equals("JiXue")) {
            if(myApplication.getDouBanPost().size()==0){
                String date="2017-05-";
                int  start=16;
                for(int i=0;i<14;i++) {
                    start--;
                    String temp=date;
                    temp=temp+start+"";
                    if(i==13){
                        isSuccess=true;
                    }
                    compositeSubscription.add(fetchManager.fetchDouBanCategory(temp));
                    fetchManager.setiFetchInstanceCompleteListener(new FetchManager.IFetchInstanceCompleteListener() {
                        @Override
                        public void loadSuccess(Object posts) {
                            jixueList.add((DouBanNews.posts) posts);
                            douBanAdapter=new DouBanAdapter(DouBanActivity.this,R.layout.douban_item,commomDouBan);
                            listView.setAdapter(douBanAdapter);

                        }
                    });

                }

                myApplication.setDouBanPost(jixueList);
                commomDouBan=jixueList;


            }
            else {
                jixueList=myApplication.getDouBanPost();
                commomDouBan=jixueList;
                douBanAdapter=new DouBanAdapter(this,R.layout.douban_item,jixueList);
                listView.setAdapter(douBanAdapter);
                isSuccess=true;
            }

        }

    }

    @Override
    public void initEvent() {

        listView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent toContentIntent=new Intent(DouBanActivity.this, ZhiHuContentActivity.class);
                toContentIntent.putExtra("Id",commomDouBan.get(i).getId());
                toContentIntent.putExtra("ModelType","DouBan");
                startActivity(toContentIntent);
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(isSuccess){
                this.finish();
            }else {
                Toast.makeText(this,"数据加载中",Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        fetchManager.setiFetchInstanceCompleteListener(null);
        super.onDestroy();




    }
}
