package com.example.pc_.wangyi.model;

/**
 * Created by pc- on 2017/6/10.
 */
public class SortItem {

    public int index;
    public String musicName;
    public void setPlayIndex(int index){
        this.index=index;
    }
    public int getPlayIndex(){
        return  this.index;
    }
    public void setMusicName(String musicName){
        this.musicName=musicName;
    }
    public String getMusicName(){
        return  this.musicName;
    }
}
