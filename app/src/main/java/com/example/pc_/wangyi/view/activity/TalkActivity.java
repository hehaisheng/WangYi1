package com.example.pc_.wangyi.view.activity;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.TalkItem;
import com.example.pc_.wangyi.retrofit.ComSchedulers;
import com.example.pc_.wangyi.retrofit.RxBus;
import com.example.pc_.wangyi.utils.DataBaseHelper;
import com.example.pc_.wangyi.view.adapter.TalkAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by pc- on 2017/5/25.
 */
public class TalkActivity extends BaseActivity {



    public RecyclerView recyclerView;
    public TalkAdapter talkAdapter;
    public DataBaseHelper dataBaseHelper;
    public SQLiteDatabase sqLiteDatabase;
    public Cursor cursor;
    public RxBus rxBus1;
    public Subscription subscription;

    public TextView talk_send;
    public EditText editText;


    public StringBuilder stringBuilder=new StringBuilder();
    public String musicName;
    public String[] comments;
    public String strsplit="讯腾讯";
    public String commonContent;
    public SharedPreferences sharedPreferences;
    public  String account;


    public List<TalkItem>  baocunTalk=new ArrayList<>();
    public List<TalkItem>  xianshiTalk=new ArrayList<>();





    @Override
    public int getLayout() {
        return R.layout.talk_activity;
    }


    @Override
    public void initView() {
        sharedPreferences = getSharedPreferences("Login",MODE_PRIVATE);
        account=sharedPreferences.getString("account","0");
        rxBus1=RxBus.newInstance();
        editText=(EditText) findViewById(R.id.talk_edit);
        talk_send=(TextView) findViewById(R.id.talk_send);
        musicName=getIntent().getExtras().getString("TalkMusicName");
        recyclerView=(RecyclerView) findViewById(R.id.talk_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataBaseHelper=DataBaseHelper.newInstance(this);
        sqLiteDatabase=dataBaseHelper.getWritableDatabase();
        cursor=sqLiteDatabase.query("MusicUser",null,"commentmusic=?",new String[]{musicName},null,null,null);
       if(cursor.getCount()>0){

             while (cursor.moveToNext())
             {
                 commonContent=cursor.getString(cursor.getColumnIndex("textcomment"));
                 comments=commonContent.split(strsplit);
             }

            //将内存的数据获取出来
            //List<TalkItem> talkItems3=new ArrayList<>();
            for (String comment : comments) {
                TalkItem talkItem = new TalkItem();
                talkItem.setContent(comment);
                talkItem.setMusicName(musicName);
                //加入内存的数据
                xianshiTalk.add(talkItem);
                }
                talkAdapter=new TalkAdapter(R.layout.talk_item,xianshiTalk);
                recyclerView.setAdapter(talkAdapter);
                talkAdapter.notifyDataSetChanged();



            }

    }

    @Override
    public void initData() {
        //开启activity后，这里会接受新的数据，需要保存
        subscription=rxBus1.tObservable(TalkItem.class)
                .compose(ComSchedulers.<TalkItem>applyIoSchedulers())
                .subscribe(new Action1<TalkItem>() {
                    @Override
                    public void call(TalkItem talkItem) {
                        //如果发送的歌曲名和现在activity打开的歌曲一样
                        if(talkItem.getMusicName().equals(musicName))
                        {
                            xianshiTalk.add(talkItem);
                            talkAdapter=new TalkAdapter(R.layout.talk_item,xianshiTalk);
                            recyclerView.setAdapter(talkAdapter);
                           // talkAdapter.notifyItemRangeChanged(xianshiTalk.size()-3,xianshiTalk.size()-1);
                            //对新的数据，需要保存的
                            baocunTalk.add(talkItem);
                        }

                    }
                });

    }

    @Override
    public void initEvent() {
        talk_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().length()>0)
                {

                    String send=musicName+strsplit+account+"分割分"+editText.getText().toString();
                    myApplication.sendDataPacket(send);
                    editText.setText("");
                }
                else
                {
                    Toast.makeText(TalkActivity.this,"请输入文字",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(cursor.getCount()==0)
        {
            StringBuilder stringBuilder=new StringBuilder();
            for(TalkItem talkItem:baocunTalk)
            {
                stringBuilder.append(talkItem.getContent()).append(strsplit);
            }
            ContentValues contentValues2=new ContentValues();
            //table xxx has no column named xxxx
            contentValues2.put("commentmusic",musicName);
            contentValues2.put("textcomment",stringBuilder.toString());
            sqLiteDatabase.insert("MusicUser",null,contentValues2);
        }
        else
        {

            //请求但是没有更新，就是初始化了，没有设置新的数据
            StringBuilder stringBuilder2=new StringBuilder();
            stringBuilder2.append(commonContent);
            for(TalkItem talkItem:baocunTalk)
            {
                stringBuilder2.append(talkItem.getContent()).append(strsplit);
            }

            ContentValues contentValues0=new ContentValues();
            contentValues0.put("textcomment",stringBuilder2.toString());
            //sqLiteDatabase.insert("MusicUser",null,contentValues0);
            sqLiteDatabase.update("MusicUser",contentValues0,"commentmusic=?",new String[]{musicName});
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(cursor!=null)
        {
            cursor.close();
        }
        if(!subscription.isUnsubscribed()){
            subscription.unsubscribe();
            subscription=null;
        }
        if(rxBus1!=null){
            rxBus1=null;
        }


    }
}
