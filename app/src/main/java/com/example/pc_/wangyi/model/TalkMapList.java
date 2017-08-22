package com.example.pc_.wangyi.model;

import java.util.List;

/**
 * Created by pc- on 2017/5/25.
 */
public class TalkMapList {

    public String musicName;
    public List<TalkItem>  talkItems;
    public void setMusicName(String musicName)
    {
        this.musicName=musicName;
    }
    public String getMusicName()
    {
        return this.musicName;
    }
}
