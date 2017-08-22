package com.example.pc_.wangyi.model;

/**
 * Created by pc- on 2017/5/25.
 */
public class TalkItem {


    public String content;
    public String musicName;
    public String  account;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }



    public void setMusicName(String musicName)
    {
        this.musicName=musicName;
    }
    public String getMusicName()
    {
        return this.musicName;
    }
    public void setContent(String content)
    {
        this.content=content;
    }
    public String getContent()
    {
        return this.content;
    }
}
