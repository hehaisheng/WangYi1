package com.example.pc_.wangyi.model;

/**
 * Created by pc- on 2017/5/19.
 */
public class HistoryItem {




    public int textId;
    public String title;
    public String imageUrl;
    public void setTitle(String title)
    {
        this.title=title;
    }
    public String getTitle()
    {
        return this.title;
    }
    public void setImageUrl(String imageUrl)
    {
        this.imageUrl=imageUrl;
    }
    public String getImageUrl()
    {
        return this.imageUrl;
    }
    public int getTextId() {
        return textId;
    }
    public void setTextId(int textId) {
        this.textId = textId;
    }
}
