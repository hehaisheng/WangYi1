package com.example.pc_.wangyi.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.LruM;
import com.example.pc_.wangyi.utils.MyLruCache;
import com.example.pc_.wangyi.view.adapter.LruItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc- on 2017/7/2.
 */
public class MyLruAct extends Activity {

    public MyLruCache myLruCache;
    public List<Object> integerList=new ArrayList<>();
    public List<Object> integerList1=new ArrayList<>();
    public RecyclerView recyclerView;
    public TextView textView;
    public LruItem lruItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_lru);
        recyclerView=(RecyclerView) findViewById(R.id.lru_test);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        textView=(TextView) findViewById(R.id.lru_dianj);
        myLruCache=new MyLruCache();
        for(int i=0;i<30;i++){
            LruM lruM=new LruM();
            lruM.setValue(i+"添加");
            myLruCache.putObject(i+"",lruM);


        }
        integerList=myLruCache.getObject();
        lruItem=new LruItem(R.layout.test_lur_item,integerList);
        recyclerView.setAdapter(lruItem);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=29;i>10;i--){
                    LruM lruM=new LruM();
                    lruM.setValue(i+"反序添加");
                    myLruCache.putObject(i+"",lruM);
                    integerList1=myLruCache.getObject();
                    lruItem=new LruItem(R.layout.test_lur_item,integerList1);
                    recyclerView.setAdapter(lruItem);
                    lruItem.notifyDataSetChanged();


                }
            }
        });



    }

}
