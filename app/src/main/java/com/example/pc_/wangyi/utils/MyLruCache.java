package com.example.pc_.wangyi.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc- on 2017/7/2.
 */
public class MyLruCache {




    //存贮数据
    public Map<String,Object>  linkedHashMap=new LinkedHashMap<>();
    //临时存储
    public  Map<String,Object> linkedHashMap1  =new LinkedHashMap<>();



    public MyLruCache(){



    }



    public List<Object> getObject(){

        List<Object>  historyItems=new ArrayList<>();
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            Object history =  entry.getValue();
            historyItems.add(history);
        }
        return historyItems;

    }

    public void putObject(String key,Object value) {
        if (linkedHashMap.size() < 20||linkedHashMap==null) {
            Log.d("数量是小于20或空","打印出来");
            Log.d("数量是小于20或空","打印出来");
            Log.d("数量是小于20或空","打印出来");
            if(linkedHashMap==null||linkedHashMap.size()==0){
                linkedHashMap.put(key,value);
            }else if(linkedHashMap.get(key)!=null){
                antitoneLinked(key, value);
            }else {
                linkedHashMap.put(key,value);
            }


        }else if(linkedHashMap.size()==20){
            Log.d("数量是20","打印出来");
            Log.d("数量是20","打印出来");
            Log.d("数量是20","打印出来");
            if(linkedHashMap.get(key)!=null){
                antitoneLinked(key, value);
                Log.d("数量是20","存在该key");
                Log.d("数量是20","存在该key");
                Log.d("数量是20","存在该key");
            }
            else{
                Log.d("数量是20","不存在该key");
                Iterator it = linkedHashMap.entrySet().iterator();
                boolean isFirst=true;
                String firstKey=null;
                while (it.hasNext()&&isFirst) {
                    Map.Entry e = (Map.Entry) it.next();
                    firstKey=(String) e.getKey();
                    isFirst=false;
                }
                linkedHashMap.remove(firstKey);
                linkedHashMap.put(key,value);
            }


        }


    }

    public void antitoneLinked(String key,Object value){
        Log.d("开始反序","反序");
        Log.d("开始反序","反序");
        Log.d("开始反序","反序");
        for(Map.Entry entry:linkedHashMap.entrySet()){
            if(!entry.getKey().equals(key)){
                linkedHashMap1.put((String)entry.getKey(),entry.getValue());
            }
        }
        linkedHashMap1.put(key,value);
        linkedHashMap=linkedHashMap1;
        linkedHashMap1.clear();
    }



}
