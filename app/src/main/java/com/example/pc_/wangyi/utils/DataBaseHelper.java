package com.example.pc_.wangyi.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by pc- on 2017/5/15.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String name = "haoyin_music_file"; //数据库名称

    private static final int version = 1; //数据库版本


    public static DataBaseHelper dataBaseHelper;
    public static DataBaseHelper newInstance(Context context)
    {
        if(dataBaseHelper==null)
        {
            synchronized (DataBaseHelper.class)
            {
                if(dataBaseHelper==null)
                {
                    dataBaseHelper=new DataBaseHelper(context);
                }
            }
        }
        return dataBaseHelper;
    }
    public DataBaseHelper(Context context)
    {
        super(context.getApplicationContext(), name, null, version);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //1表示点击喜欢按钮,保存是否喜欢和播放的次数
        db.execSQL("CREATE TABLE IF NOT EXISTS MusicLove (musicid integer primary key autoincrement, musiclovename varchar(20), love INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS MusicCount (musiccountid integer primary key autoincrement, musiccountname varchar(20), count INTEGER)");

        //保存文章标题和图片url
        db.execSQL("CREATE TABLE IF NOT EXISTS ArtContent (artid integer primary key autoincrement,textId INTEGER ,indexCount varchar(10),type varchar(20), title varchar(60), imageurl varchar(60),html TEXT)");
        //保存文章内容
        db.execSQL("CREATE TABLE IF NOT EXISTS ArtDownContent (downid integer primary key autoincrement,textDownId INTEGER,typeDown varchar(20), titleDown varchar(60), imageurlDown varchar(60),htmlDown TEXT)");
       //保存聊天记录
        db.execSQL("CREATE TABLE IF NOT EXISTS MusicUser (userid integer primary key autoincrement,commentmusic varchar(20),textcomment TEXT)");
        //保存下载记录
        db.execSQL("CREATE TABLE IF NOT EXISTS DownPosition (positionid integer primary key autoincrement,downurl varchar(20),fisrtposition varchar(10),secondposition varchar(10),thirdposition varchar(10),allCount varchar(10),hasdone INTEGER)");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        

    }
}
