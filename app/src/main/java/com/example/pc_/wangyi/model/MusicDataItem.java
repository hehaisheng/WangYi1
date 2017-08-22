package com.example.pc_.wangyi.model;

/**
 * Created by pc- on 2017/5/15.
 */
public class MusicDataItem {

    public String musicName;
    public String musicArt;
    public long musicDuration;
    public String musicUrl;
    public int musicCount;
    public long size;
    public String getMusicName()
    {
       return  this.musicName;
    }
    public void setMusicName(String musicName)
    {
        this.musicName=musicName;
    }
    public String getMusicArt()
    {
        return this.musicArt;
    }
    public void setMusicArt(String musicArt)
    {
        this.musicArt=musicArt;
    }
    public long getMusicDuration()
    {
        return this.musicDuration;
    }
    public void setMusicDuration(long musicDuration)
    {
        this.musicDuration=musicDuration;
    }
    public String getMusicUrl()
    {
        return this.musicUrl;
    }
    public void setMusicUrl(String musicUrl)
    {
        this.musicUrl=musicUrl;
    }
    public int getMusicCount()
    {
        return this.musicCount;
    }
    public void setMusicCount(int musicCount)
    {
        this.musicCount=musicCount;
    }
    public void setSize(long size)
    {
        this.size=size;
    }
    public long getSize()
    {
        return this.size;
    }


}
