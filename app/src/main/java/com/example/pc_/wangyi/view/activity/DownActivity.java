package com.example.pc_.wangyi.view.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.DownItem;
import com.example.pc_.wangyi.utils.DataBaseHelper;
import com.example.pc_.wangyi.view.adapter.DownAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc- on 2017/5/20.
 */
public class DownActivity extends BaseActivity{



    public DownAdapter downAdapter;
    public DataBaseHelper dataBaseHelper;
    public SQLiteDatabase sqLiteDatabase;
    public Cursor cursor;

    public List<DownItem> downItemList=new ArrayList<>();

    public ListView listView;
    public TextView xianshiText;


    @Override
    public int getLayout() {
        return R.layout.down_activity;
    }


    @Override
    public void initView() {


        listView=(ListView) findViewById(R.id.down_activity_list);
        xianshiText=(TextView) findViewById(R.id.down_xianshi_text);

    }

    @Override
    public void initData() {

        dataBaseHelper=DataBaseHelper.newInstance(this);
        sqLiteDatabase=dataBaseHelper.getWritableDatabase();
        cursor=sqLiteDatabase.query("artDownContent",null,null,null,null,null,null);
        if(cursor.getCount()>0)
        {

            downAdapter=new DownAdapter(this);
            while (cursor.moveToNext())
            {
                DownItem downItem=new DownItem();
                downItem.setTitle(cursor.getString(cursor.getColumnIndex("titleDown")));
                downItem.setTextId(cursor.getInt(cursor.getColumnIndex("textDownId")));
                if(cursor.getString(cursor.getColumnIndex("imageurlDown")).equals("NoUrl"))
                {
                    downItem.setImageUrl("NoUrl");
                }
                else
                {
                    downItem.setImageUrl(cursor.getString(cursor.getColumnIndex("imageurlDown")));
                }
                downItemList.add(downItem);

            }
            downAdapter.setDownItems(downItemList);
            listView.setAdapter(downAdapter);

        }
        else {
            listView.setVisibility(View.GONE);
            xianshiText.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void initEvent() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(DownActivity.this, ZhiHuContentActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("Id",downItemList.get(position).getTextId());
                bundle.putString("ModelType","ZhiHu");
                bundle.putString("Title",downItemList.get(position).getTitle());
                bundle.putString("ImageUrl",downItemList.get(position).getImageUrl());
                intent.putExtras(bundle);
                DownActivity.this.startActivity(intent);
            }
        });

    }
}
