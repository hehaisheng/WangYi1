package com.example.pc_.wangyi.view.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.HistoryItem;
import com.example.pc_.wangyi.model.ZhiHuNews;
import com.example.pc_.wangyi.retrofit.ComSchedulers;
import com.example.pc_.wangyi.retrofit.FetchManager;
import com.example.pc_.wangyi.retrofit.RxBus;
import com.example.pc_.wangyi.utils.DataBaseHelper;
import com.example.pc_.wangyi.utils.MyLruCache;
import com.example.pc_.wangyi.view.adapter.HistoryAdapter;
import com.example.pc_.wangyi.view.adapter.ZhiHuAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by pc- on 2017/5/17.
 */
public class ZhiHuActivity extends BaseActivity implements View.OnClickListener {



    public int  artModel;
    public int historyType;
    public int start = 20170517;


    public HistoryAdapter historyAdapter;
    public DataBaseHelper dataBaseHelper;
    public SQLiteDatabase sqLiteDatabase;
    public Cursor cursor;
    public RecyclerView listView;
    public ZhiHuAdapter zhiHuAdapter;
    public RxBus rxBus1;


    public List<HistoryItem> historyItems=new ArrayList<>();
    public List<ZhiHuNews.Question> commomList=new ArrayList<>();


    public MyLruCache myLruCache;
    public CompositeSubscription compositeSubscription;
    public FetchManager fetchManager1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        rxBus1=new RxBus();
        rxPreLoad();
        super.onCreate(savedInstanceState);
    }



    public void rxPreLoad(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                fetchManager1=FetchManager.newInstance(ZhiHuActivity.this);
                compositeSubscription=new CompositeSubscription();
                historyType=getIntent().getExtras().getInt("History");
                artModel=getIntent().getExtras().getInt("model",1);
                if(historyType==1) {
                    dataBaseHelper=DataBaseHelper.newInstance(ZhiHuActivity.this);
                    sqLiteDatabase=dataBaseHelper.getWritableDatabase();
                    cursor=sqLiteDatabase.query("ArtContent",null,null,null,null,null,null);
                    if(cursor.getCount()>0){
                      // Toast.makeText(ZhiHuActivity.this,"有数据",Toast.LENGTH_SHORT).show();
                        while (cursor.moveToNext()) {
                            HistoryItem historyItem=new HistoryItem();
                            String title=cursor.getString(cursor.getColumnIndex("title"));
                            int id=cursor.getInt(cursor.getColumnIndex("textId"));
                            historyItem.setTitle(title);
                            historyItem.setTextId(id);
                            if(cursor.getString(cursor.getColumnIndex("imageurl"))==null) {
                                historyItem.setImageUrl("nourl");
                            }
                            else {
                                historyItem.setImageUrl(cursor.getString(cursor.getColumnIndex("imageurl")));
                            }
                            historyItems.add(historyItem);
                        }
                        rxBus1.post("历史界面初始化");
                    }
                    cursor.close();
                } else if(historyType==0) {
                    if(fetchManager1.dawuQuestion!=null){
                        fetchManager1.dawuQuestion.clear();
                    }
                    if(fetchManager1.xiaoshiQuestion!=null){
                        fetchManager1.xiaoshiQuestion.clear();
                    }
                    if(fetchManager1.tucaoQuestion!=null){
                        fetchManager1.tucaoQuestion.clear();
                    }

                    for (int i = 0; i < 10; i++) {
                        compositeSubscription.add(fetchManager1.fetchZhiHuCategoty(start)) ;
                        start--;
                    }
                    fetchManager1.setFetchCompleteListener(new FetchManager.IFetchCompleteListener() {
                        @Override
                        public void loadSuccess() {
                            switch (artModel){
                                case 0:
                                    commomList=myApplication.getDaWu();
                                    break;
                                case 1:
                                    commomList=myApplication.getTucaoList();
                                    break;
                                case 2:
                                    if(myApplication.getQuestions().size()!=0){
                                           commomList=myApplication.getQuestions();
                                   }else{
                                       List<ZhiHuNews.Question> questionList=myApplication.getZhiHuNews().getStories();
                                       Map<String,Object> linkedHashMap=new LinkedHashMap<>();
                                      for(int i=0;i<questionList.size();i++){
                                         linkedHashMap.put(questionList.get(i).getTitle(),questionList.get(i));
                                      }
                                      commomList=questionList;
                                      myApplication.setLruHashMap(linkedHashMap);
                                   }
                                    break;
                                case 3:
                                    commomList= myApplication.getXiaoshiList();
                                    break;
                            }
                            rxBus1.post("知乎界面");

                        }
                    });
                }
            }
        }).start();
    }
    @Override
    public int getLayout() {
        return R.layout.zhihu_activity;
    }


    @Override
    public void initView() {


        myLruCache=new MyLruCache();
    }

    @Override
    public void initData() {

        listView=(RecyclerView) findViewById(R.id.zhihu_actiity_list);
        listView.setLayoutManager(new LinearLayoutManager(this));
        rxBus1.tObservable(String.class)
                .compose(ComSchedulers.<String>applyIoSchedulers())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if(s.equals("历史界面初始化")){
                            historyAdapter=new HistoryAdapter(ZhiHuActivity.this,R.layout.history_item,historyItems);
                            listView.setAdapter(historyAdapter);
                        }else if(s.equals("知乎界面")){
                            zhiHuAdapter = new ZhiHuAdapter(ZhiHuActivity.this,R.layout.zhihu_item, commomList);
                            listView.setAdapter(zhiHuAdapter);
                        }
                    }
                });


    }

    @Override
    public void initEvent() {
        listView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if(historyType==1){
                    HistoryItem historyItem=historyItems.get(i);
                   toContentActivity(historyItem.getTextId(),historyItem.getTitle(),historyItem.getImageUrl());
                }else{
                    ZhiHuNews.Question question=commomList.get(i);
                    if(artModel==2){
                        for(Map.Entry entry:myApplication.getLruHashMap().entrySet()){
                           myLruCache.putObject((String)entry.getKey(),entry.getValue());
                        }
                        myLruCache.putObject(question.getTitle(),question);
                        List<ZhiHuNews.Question> questionList=new ArrayList<>();
                        List<Object> objects=myLruCache.getObject();
                        for(int k=0;k<objects.size();k++){
                            questionList.add((ZhiHuNews.Question) objects.get(i));
                        }
                        myApplication.setQuestions(questionList);
                    }
                    toContentActivity(question.getId(),question.getTitle(),question.getImages().get(0));
                }

            }
        });

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fetchManager1.setFetchCompleteListener(null);
        if(compositeSubscription!=null){
            compositeSubscription.unsubscribe();
            compositeSubscription=null;
        }

        if(rxBus1!=null){
            rxBus1=null;
        }
        if(fetchManager1!=null){
            fetchManager1=null;
        }

    }

    public void  toContentActivity(int id, String title, String imageUrl){
        Intent intent=new Intent(ZhiHuActivity.this, ZhiHuContentActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt("Id",id);
        bundle.putString("ModelType","ZhiHu");
        bundle.putString("Title",title);
        bundle.putString("ImageUrl",imageUrl);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}


