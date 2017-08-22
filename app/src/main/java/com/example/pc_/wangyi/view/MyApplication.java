package com.example.pc_.wangyi.view;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pc_.wangyi.model.DouBanNews;
import com.example.pc_.wangyi.model.FileInfo;
import com.example.pc_.wangyi.model.FriendModel;
import com.example.pc_.wangyi.model.GuoQiaoItem;
import com.example.pc_.wangyi.model.MusicDataItem;
import com.example.pc_.wangyi.model.SortItem;
import com.example.pc_.wangyi.model.TalkItem;
import com.example.pc_.wangyi.model.ZhiHuNews;
import com.example.pc_.wangyi.retrofit.RxBus;
import com.example.pc_.wangyi.utils.DataBaseHelper;
import com.squareup.leakcanary.LeakCanary;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * Created by pc- on 2017/5/15.
 */
public class MyApplication extends Application {

    public static final  String TAG="MyApplication";
    //列表显示不出socket发送的数据
    public RxBus  rxBus;
    //Executors.newCachedThreadPool();
    public static Executor MAIN_EXECUTOR =Executors.newCachedThreadPool();
    public static Executor FILE_SENDER_EXECUTOR = Executors.newSingleThreadExecutor();
    public ZhiHuNews zhiHuNews;
    public DouBanNews douBanNewses;
    public String appMusic;


    //知乎
    public ArrayList<ZhiHuNews.Question> xiaoshiList=new ArrayList<>();
    public ArrayList<ZhiHuNews.Question> dawuList=new ArrayList<>();
    public  ArrayList<ZhiHuNews.Question> tucaoList=new ArrayList<>();
    public List<ZhiHuNews> zhiHuNewsList=new ArrayList<>();
    //豆瓣
    public ArrayList<DouBanNews.posts>  postses=new ArrayList<>();
    //果壳
    public List<GuoQiaoItem.result> guoqiaoList=new ArrayList<>();
    //音乐
    public List<MusicDataItem> gedanList=new ArrayList<>();
    public List<MusicDataItem> loveList=new ArrayList<>();
    //聊天
    public List<TalkItem> talkItems=new ArrayList<>();
    public volatile  Map<String,List<TalkItem>> talkMap=new HashMap<>();
    //文件发送方
    Map<String, FileInfo> mFileInfoMap = new HashMap<String, FileInfo>();
    Map<String, FileInfo> mReceiverFileInfoMap = new HashMap<String, FileInfo>();
    //堆排序
    public List<SortItem> sortItems=new ArrayList<>();
    //快排
    public List<SortItem> quickSortItemList=new ArrayList<>();
    public int middleIndex;



    //知乎
    public void setZhiHuNewsList(List<ZhiHuNews> zhiHuNewsList) {
        this.zhiHuNewsList=zhiHuNewsList;
    }
    public List<ZhiHuNews> getZhiHuNewsList()
    {
        return this.zhiHuNewsList;
    }
    public  ArrayList<ZhiHuNews.Question> getTucaoList()
    {
        return this.tucaoList;
    }
    public void setXiaoshiList(ArrayList<ZhiHuNews.Question> xiaoshiList) {
        this.xiaoshiList=xiaoshiList;
    }
    public ArrayList<ZhiHuNews.Question> getXiaoshiList()
    {
        return this.xiaoshiList;
    }
    public  ArrayList<ZhiHuNews.Question> getDaWu()
    {
        return this.dawuList;
    }
    public void setDawuList(ArrayList<ZhiHuNews.Question> dawuList)
    {
        this.dawuList=dawuList;
    }
    public void setTucaoList(ArrayList<ZhiHuNews.Question> tucaoList)
    {
        this.tucaoList=tucaoList;
    }
    public void setZhiHuNews(ZhiHuNews zhiHuNews)
    {
        this.zhiHuNews=zhiHuNews;
    }
    public  ZhiHuNews getZhiHuNews()
    {
        return this.zhiHuNews;
    }
    //豆瓣
    public DouBanNews getDouBanNewses()
    {
        return this.douBanNewses;
    }
    public void setDouBanNewses(DouBanNews douBanNewses)
    {
        this.douBanNewses=douBanNewses;
    }
    public void setDouBanPost(ArrayList<DouBanNews.posts> douBanPost)
    {
        this.postses=douBanPost;
    }
    public ArrayList<DouBanNews.posts> getDouBanPost()
    {
        return this.postses;
    }
    //果壳
    public List<GuoQiaoItem.result> getGuoqiaoList()
    {
        return this.guoqiaoList;
    }
    public void setGuoqiaoList(List<GuoQiaoItem.result> guoqiaoList){
        this.guoqiaoList=guoqiaoList;
    }

    //音乐
    public void setLoveList(List<MusicDataItem> loveList)
    {
        this.loveList=loveList;
    }
    public List<MusicDataItem> getLoveList()
    {
        return this.loveList;
    }
    public void setGedanList(List<MusicDataItem> gedanList) {
        this.gedanList=gedanList;
    }
    public List<MusicDataItem>  getGedanList()
    {
        return this.gedanList;
    }




    //聊天
    public void setTalkMap(Map<String,List<TalkItem>> talkMap)
    {
        this.talkMap=talkMap;
    }
    public Map<String,List<TalkItem>> getTalkMap()
    {
        return this.talkMap;
    }
    public void setTalkItems(List<TalkItem> talkItems)
    {
        this.talkItems=talkItems;
    }
    public List<TalkItem> getTalkItems()
    {
        return this.talkItems;
    }


    //堆排序
    public void setHeapSort(List<SortItem> sorts){
        sortItems.clear();
        this.sortItems=sorts;
    }
    public List<SortItem> getSortItems(){
        return this.sortItems;
    }
    //快排
    public void setQuickSort(List<SortItem> quickSort){

        this.quickSortItemList=quickSort;

    }
    public  List<SortItem>  getQucikItems(){
        return  this.quickSortItemList;
    }
    public void setMiddleIndex(int middleIndex){
        this.middleIndex=middleIndex;
    }
    public int getMiddleIndex(){
        return this.middleIndex;
    }


    public void  setAppMusic(String appMusic){
        this.appMusic=appMusic;
    }
    public String getAppMusic(){
        return this.appMusic;
    }

    public List<FileInfo> fileInfos=new ArrayList<>();







    /**
     * 获取全局变量中的FileInfoMap
     * @return
     */
    public Map<String, FileInfo> getFileInfoMap(){
        return mFileInfoMap;
    }

    /**
     * 添加一个FileInfo
     * @param fileInfo
     */
    public void addFileInfo(FileInfo fileInfo){

        if(!mFileInfoMap.containsKey(fileInfo.getFilePath())){
            mFileInfoMap.put(fileInfo.getFilePath(), fileInfo);
        }
    }





    public void addReceiverFileInfo(FileInfo fileInfo){
        if(!mReceiverFileInfoMap.containsKey(fileInfo.getFilePath())){
            mReceiverFileInfoMap.put(fileInfo.getFilePath(), fileInfo);
        }
    }


    public Map<String, Object> getLruHashMap() {
        return this.lruHashMap;
    }

    public void setLruHashMap(Map<String, Object> lruHashMap) {
        this.lruHashMap = lruHashMap;
    }

    public Map<String,Object>  lruHashMap=new LinkedHashMap<>();





    public void setFileInfos(FileInfo fileInfo)
    {
         fileInfos.add(fileInfo);
    }
    public List<FileInfo> getFileInfos()
    {
        return this.fileInfos;
    }

    public static MyApplication newInstance;
    public static MyApplication getNewInstance()
    {
        if(newInstance==null)
        {
            synchronized (MyApplication.class)
            {
                if(newInstance==null)
                {
                    newInstance=new MyApplication();
                }
            }
        }
        return newInstance;
    }


    public List<ZhiHuNews.Question> getQuestions() {
        return this.questions;
    }

    public void setQuestions(List<ZhiHuNews.Question> questions) {
        this.questions = questions;
    }

    public List<ZhiHuNews.Question>  questions=new ArrayList<>();


    public List<FriendModel> getFriendModels() {
        return friendModels;
    }

    public void setFriendModels(List<FriendModel> friendModels) {
        this.friendModels = friendModels;
    }

    public List<FriendModel> friendModels = new CopyOnWriteArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();

        LeakCanary.install(this);
        rxBus=RxBus.newInstance();
        init();
    }
    public void init()  {

           new Thread(new Runnable() {
               @Override
               public void run() {
                   //做接收数据的

                   final  String Broadcast_Ip="230.0.0.1";
                   final int  Broadcastcast_Port=30000;
                   final int data_len=4096;

                   try {
                       MulticastSocket multicastSocket=new MulticastSocket(Broadcastcast_Port);
                       InetAddress inetAddress=InetAddress.getByName(Broadcast_Ip);
                       byte[] inBuff=new byte[data_len];
                       DatagramPacket inPacket=new DatagramPacket(inBuff,inBuff.length);
                       multicastSocket.joinGroup(inetAddress);
                       multicastSocket.setLoopbackMode(false);
                       while (true)
                       {
                           //因为该线程和application的线程不一样
                           //所以他们保存的数据在不同的位置
                           //这样我们在talkactivity获取数据就没有
                           multicastSocket.receive(inPacket);
                           TalkItem talkItem=new TalkItem();
                           String receiverStr=new String(inBuff,0,inPacket.getLength());
                           String strsplit="讯腾讯";
                         //  musicName+strsplit+account+strsplit+editText.getText().toString();
                           String[] temp=receiverStr.split(strsplit);
                           talkItem.setMusicName(temp[0]);
                           talkItem.setContent(temp[1]);
                           DataBaseHelper dataBaseHelper=DataBaseHelper.newInstance(MyApplication.this);
                           SQLiteDatabase sqLiteDatabase=dataBaseHelper.getWritableDatabase();
                           Cursor cursor=sqLiteDatabase.query("MusicUser",null,"commentmusic=?",new String[]{temp[0]},null,null,null);
                           StringBuilder stringBuilder=new StringBuilder();
                           stringBuilder.append(temp[1]).append(strsplit);
                           if(cursor.getCount()==0)
                           {

                               ContentValues contentValues=new ContentValues();
                               contentValues.put("commentmusic",temp[0]);
                               contentValues.put("textcomment",stringBuilder.toString());
                               sqLiteDatabase.insert("MusicUser",null,contentValues);

                           }
                           else{
                                //多少个cursor，可以获取多少次index

                               //通过查询获取的数据cursor是一个列表，我们想通过cursor
                               //获取数据，必须moveToNext
                               while (cursor.moveToNext())
                               {
                                   StringBuilder stringBuilder1=new StringBuilder();
                                   //这句话出错
                                   String commentStr3=cursor.getString(cursor.getColumnIndex("textcomment"));
                                   stringBuilder1.append(commentStr3).append(temp[1]).append(strsplit);
                                   ContentValues contentValues0=new ContentValues();
                                   contentValues0.put("textcomment",stringBuilder1.toString());
                                   sqLiteDatabase.update("MusicUser",contentValues0,"commentmusic=?",new String[]{temp[0]});
                               }


                           }
                           //这个很好用
                           rxBus.post(talkItem);
                           cursor.close();

                       }
                   }
                   catch (IOException e) {
                       e.printStackTrace();
                   }
               }
           }).start();
        }
    //发送数据的方法
    public  void  sendDataPacket(final String messge)  {
        new Thread(new Runnable() {
            @Override
            public void run() {

                final  String Broadcast_Ip1="230.0.0.1";
                final int  Broadcastcast_Port1=30000;
                byte[]  sendMessge=messge.getBytes();
                DatagramPacket outPacket1;
                MulticastSocket multicastSocket1;
                InetAddress inetAddress1;
                try {
                    multicastSocket1=new MulticastSocket(Broadcastcast_Port1);
                    inetAddress1=InetAddress.getByName(Broadcast_Ip1);
                    multicastSocket1.joinGroup(inetAddress1);
                    multicastSocket1.setLoopbackMode(false);
                    outPacket1=new DatagramPacket(new byte[0],0,inetAddress1,Broadcastcast_Port1);
                    outPacket1.setData(sendMessge);
                    multicastSocket1.send(outPacket1);
                }catch (IOException e)
                {
                    e.printStackTrace();
                }

            }
        }).start();



    }

}
