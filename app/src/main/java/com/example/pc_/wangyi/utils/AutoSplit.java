package com.example.pc_.wangyi.utils;

/**
 * Created by pc- on 2017/7/4.
 */
public class AutoSplit {


    public String  autoSplit(String recommandData){
        String[]  strings1=recommandData.split("好音好");
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=0;i<strings1.length;i++){
            if(i!=strings1.length-1){
                stringBuilder.append(strings1[i]).append("\n");
            }else{
                stringBuilder.append(strings1[i]);
            }
        }
        return stringBuilder.toString();

    }
}
