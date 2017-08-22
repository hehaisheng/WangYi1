package com.example.pc_.wangyi.presenter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;

import com.example.pc_.wangyi.model.MusicDataItem;
import com.example.pc_.wangyi.utils.DataBaseHelper;
import com.example.pc_.wangyi.view.MyApplication;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by pc- on 2017/5/15.
 */
public class MusicDataPresenter {

    public static MusicDataPresenter musicDataPresenter;
    public DataBaseHelper dataBaseHelper;
    public    SQLiteDatabase db;
    public  Context context;

    public List<MusicDataItem> dataItems=new ArrayList<>();
    public List<MusicDataItem>  loveData=new ArrayList<>();
    public static  List<MusicDataItem>  temp=new ArrayList<>();
    public static MusicDataPresenter newInstance(Context context)
    {
        if(musicDataPresenter==null)
        {
            synchronized (MusicDataPresenter.class)
            {
                if(musicDataPresenter==null)
                {
                    musicDataPresenter=new MusicDataPresenter(context);
                }
            }
        }
        return musicDataPresenter;
    }
    public MusicDataPresenter(Context context)
    {

       this.context=context.getApplicationContext();
       temp=getMusicData();
    }

    public List<MusicDataItem> getMusicData() {

        MyApplication application=MyApplication.getNewInstance();
        Cursor cursor=context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,null);
        dataBaseHelper=new DataBaseHelper(context);
        db=dataBaseHelper.getWritableDatabase();
        if(cursor!=null) {
            while (cursor.moveToNext()) {
                MusicDataItem musicDataItem=new MusicDataItem();
                String musicName=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String musicArt=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String musicUrl=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                long musicDuration=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                long musicSize=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                if (dataItems.size()==0) {
                    musicDataItem.setMusicName(musicName);
                    musicDataItem.setMusicArt(musicArt);
                    musicDataItem.setMusicUrl(musicUrl);
                    musicDataItem.setMusicDuration(musicDuration);
                    musicDataItem.setSize(musicSize);
                    dataItems.add(musicDataItem);
                }
                for(int i=0;i<dataItems.size();i++)
                {
                    if(dataItems.get(i).getMusicName().equals(musicName))
                    {
                        break;
                    }
                    if(!dataItems.get(i).getMusicName().equals(musicName)&&(i==dataItems.size()-1))
                    {
                        musicDataItem.setMusicName(musicName);
                        musicDataItem.setMusicArt(musicArt);
                        musicDataItem.setMusicUrl(musicUrl);
                        musicDataItem.setMusicDuration(musicDuration);
                        musicDataItem.setSize(musicSize);
                        dataItems.add(musicDataItem);
                    }
                }

            }
        }
        if (cursor!=null) {
            cursor.close();
        }

        int index=0;
        Cursor cursor1=db.query("MusicCount ",new String[]{"musiccountname","count"},null,null,null,null,null);
        if(cursor1!=null&&cursor1.getCount()>0) {
            while (cursor1.moveToNext()) {
                //对于出现的警告要处理
                int count=cursor1.getInt(cursor1.getColumnIndex("count"));
                String musicName=cursor1.getString(cursor1.getColumnIndex("musiccountname"));
                for(int i=0;i<dataItems.size();i++){
                    if(musicName.equals(dataItems.get(i).getMusicName())){
                        dataItems.get(i).setMusicCount(count);
                    }
                }
            }
            for(int i=0;i<dataItems.size();i++){
                if(dataItems.get(i).getMusicCount()<=0){
                    dataItems.get(i).setMusicCount(0);
                }
            }
        }else {
            for(int i=0;i<dataItems.size();i++){
                    dataItems.get(i).setMusicCount(0);
            }
        }
        if (cursor1!=null) {
            cursor1.close();
        }

        application.setGedanList(dataItems);
        return   dataItems;

    }

    public List<MusicDataItem>  getMusicItem(){
        return this.temp;
    }
    public List<MusicDataItem> getLoveList() {
        Cursor cursor1=db.query("MusicLove",new String[]{"musiclovename","love"},null,null,null,null,null);
        if(cursor1!=null&&cursor1.getCount()>0) {
            while (cursor1.moveToNext()) {
                //对于出现的警告要处理
                int loveIndex=cursor1.getInt(cursor1.getColumnIndex("love"));
                String musicName=cursor1.getString(cursor1.getColumnIndex("musiclovename"));
                //是喜欢的
                if(loveIndex==1){
                      for(int i=0;i<dataItems.size();i++){
                          if(musicName.equals(dataItems.get(i).getMusicName())){
                              loveData.add(dataItems.get(i));
                          }
                      }
                }
            }
        }
        if (cursor1!=null) {
            cursor1.close();
        }
        return loveData;

    }


}
