package com.example.pc_.wangyi.model;

import android.graphics.Bitmap;

/**
 * Created by pc- on 2017/5/23.
 */
public class TitleAndBitmap {


    public Bitmap bitmap;
    public String comTitle;
    public void  setBitmap(Bitmap bitmap)
    {
        this.bitmap=bitmap;
    }
    public Bitmap getBitmap()
    {
        return this.bitmap;
    }
    public void setComTitle(String title)
    {

        this.comTitle=title;
    }

}
