package com.example.pc_.wangyi.view.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.pc_.wangyi.IMusicLast;
import com.example.pc_.wangyi.IMusicNext;
import com.example.pc_.wangyi.IMusicPlay;
import com.example.pc_.wangyi.IMusicPuase;
import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.aidlbinder.BinderPool;
import com.example.pc_.wangyi.aidlbinder.MusicPlayServiceBinder;
import com.example.pc_.wangyi.model.MusicDataItem;
import com.example.pc_.wangyi.presenter.MusicDataPresenter;
import com.example.pc_.wangyi.retrofit.RxBus;
import com.example.pc_.wangyi.utils.DataBaseHelper;
import com.example.pc_.wangyi.view.adapter.MusicListAdapter;
import com.example.pc_.wangyi.view.service.MusicPlayService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by pc- on 2017/7/8.
 */
public class BinderPlayActivity extends BaseActivity  implements View.OnClickListener  {



    @BindView(R.id.binder_music_play_text1)
    TextView musicPlayText1;
    @BindView(R.id.binder_music_play_imageView1)
    ImageView musicPlayImageView1;
    @BindView(R.id.binder_music_play_talk)
    ImageView musicPlayTalk;
    @BindView(R.id.binder_play_down)
    ImageView playDown;
    @BindView(R.id.binder_play_list_icon)
    ImageView playListIcon;
    @BindView(R.id.binder_play_seek)
    SeekBar playSeek;
    @BindView(R.id.binder_play_tiem_start)
    TextView playTiemStart;
    @BindView(R.id.binder_play_tiem_end)
    TextView playTiemEnd;
    @BindView(R.id.binder_music_play_imageView2)
    ImageView musicPlayImageView2;
    @BindView(R.id.binder_music_play_imageView3)
    ImageView musicPlayImageView3;
    @BindView(R.id.binder_music_play_imageView4)
    ImageView musicPlayImageView4;
    @BindView(R.id.binder_music_play_imageView5)
    ImageView musicPlayImageView5;
    @BindView(R.id.binder_music_play_imageView6)
    ImageView musicPlayImageView6;
    @BindView(R.id.binder_music_play_more_list)
    RecyclerView musicPlayMoreList;


    public MusicDataPresenter musicDataPresenter;
    public DataBaseHelper dataBaseHelper;
    public SQLiteDatabase db;
    public MusicListAdapter musicListAdapter;
    public SharedPreferences sharedPreferences;
    public Cursor loveCursor;
    public Animation animation;
    public RxBus rxBus1;

    public BinderPool binderPool;
    public IMusicPlay iMusicPlay;
    public IMusicNext iMusicNext;
    public Bundle bundle;
    public IMusicPuase iMusicPuase;
    public IMusicLast iMusicLast;



    //全局当前音乐下标
    public int currentMusicIndex;
    public String musicNameStr;
    public boolean isNet = false;
    //默认1，列表；2，循环；3，单首；
    public int playMode;
    public boolean isPlaying = false;
    public boolean isLove = false;
    //用来判断是否设置过喜欢
    public boolean hasLoveHistory = false;
    public String currentMusicName;
    public String activityModel;


    public List<MusicDataItem> musicDataItems = new ArrayList<>();








    @Override
    public int getLayout() {
        return R.layout.binder_activity;
    }


    @Override
    public void initView() {

        bundle = getIntent().getExtras();
        currentMusicIndex = bundle.getInt("current", 0);
        activityModel=bundle.getString("ActivityModel","ListModel");
        musicDataItems = myApplication.getGedanList();
        playMusic();

        rxBus1=RxBus.newInstance();
        sharedPreferences = getSharedPreferences("PlaySetting", 0);
        playMode = sharedPreferences.getInt("PlayMode", 1);
        if (playMode == 2) {
            musicPlayImageView2.setImageResource(R.drawable.ic_play_mode_loop);
        } else if (playMode == 3) {
            musicPlayImageView2.setImageResource(R.drawable.ic_play_mode_single);
        } else {
            musicPlayImageView2.setImageResource(R.drawable.ic_play_mode_list);
        }






        animation = AnimationUtils.loadAnimation(this, R.anim.translate);
        musicPlayImageView1.startAnimation(animation);
        musicPlayMoreList.setLayoutManager(new LinearLayoutManager(this));
        dataBaseHelper = DataBaseHelper.newInstance(this);
        db = dataBaseHelper.getWritableDatabase();


    }

    @Override
    public void initData() {





        if(activityModel.equals("LoveModel")){
            musicDataPresenter=new MusicDataPresenter(this);
            musicDataItems=musicDataPresenter.getLoveList();
            // toService("LoveModel");
          //  playMusic();
        }else{
            musicDataItems = myApplication.getGedanList();
            //toService("ListModel");
           // playMusic();
        }

        currentMusicName = musicDataItems.get(currentMusicIndex).getMusicName();
        musicNameStr = bundle.getString("MusicName", "泡沫");
        isNet = bundle.getBoolean("isNet", false);
        musicDataPresenter = MusicDataPresenter.newInstance(this);
        //musicname varchar(20), count INTEGER,love INTEGER)"MusicFile
        loveCursor = db.query("MusicLove", new String[]{"love"}, "musiclovename=?", new String[]{currentMusicName}, null, null, null);
        //由数据库的数据判断是否设置了喜欢
        if (loveCursor.getCount() == 0) {
            isLove = false;
            hasLoveHistory = false;
            musicPlayImageView6.setImageResource(R.drawable.ic_favorite_yes);
        } else {
            while (loveCursor.moveToNext()) {

                hasLoveHistory = true;
                int loveIndex = loveCursor.getInt(loveCursor.getColumnIndex("love"));
                if (loveIndex == 0) {
                    isLove = false;
                    musicPlayImageView6.setImageResource(R.drawable.ic_favorite_yes);
                } else if (loveIndex == 1) {
                    isLove = true;
                    musicPlayImageView6.setImageResource(R.drawable.love_red);
                }
            }
        }

        playTiemEnd.setText(getTime());
        musicPlayText1.setText(musicDataItems.get(currentMusicIndex).getMusicName());

    }

    @Override
    public void initEvent() {
        musicPlayImageView2.setOnClickListener(this);
        musicPlayImageView3.setOnClickListener(this);
        musicPlayImageView4.setOnClickListener(this);
        musicPlayImageView5.setOnClickListener(this);
        musicPlayImageView6.setOnClickListener(this);
        musicPlayTalk.setOnClickListener(this);
        playDown.setOnClickListener(this);
        playListIcon.setOnClickListener(this);
        musicPlayMoreList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                currentMusicName=musicDataItems.get(i).getMusicName();
                currentMusicIndex=i;
                BinderPlayActivity.this.musicPlayText1.setText(currentMusicName);
                musicPlayMoreList.setVisibility(View.GONE);
                //rxBus1.post("Play-"+i);
                playMusic();
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int y = (int) event.getY();
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                int max_y = dm.heightPixels;
                if (y < max_y * 60 / 100) {
                    if (musicPlayMoreList.getVisibility() == View.VISIBLE) {
                        musicPlayMoreList.setVisibility(View.GONE);
                    }
                }

                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.binder_play_list_icon:
                //跳出播放列表
                if (musicDataItems.size() > 0) {
                    Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.open_play_list);
                    musicListAdapter = new MusicListAdapter(R.layout.musiclist_item, musicDataItems);
                    musicPlayMoreList.setAdapter(musicListAdapter);
                    musicPlayMoreList.setVisibility(View.VISIBLE);
                    musicPlayMoreList.startAnimation(animation1);
                }
                break;
            case R.id.binder_music_play_imageView2:
                if (playMode == 1) {
                    playMode = 2;
                    musicPlayImageView2.setImageResource(R.drawable.ic_play_mode_loop);
                } else if (playMode == 2) {
                    playMode = 3;
                    musicPlayImageView2.setImageResource(R.drawable.ic_play_mode_single);
                } else if (playMode == 3) {
                    playMode = 1;
                    musicPlayImageView2.setImageResource(R.drawable.ic_play_mode_list);
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("PlayMode", playMode);
                editor.apply();

                //改变播放模式
                break;
            case R.id.binder_play_down:
                //判断是否开始下载
                if (isNet) {
                    //判断是否下载过，再开始去下载
                } else {
                    Toast.makeText(this, "已下载过", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.binder_music_play_talk:
                String musicTitle = musicDataItems.get(currentMusicIndex).getMusicName();
                Intent talkIntent = new Intent(this, TalkActivity.class);
                talkIntent.putExtra("TalkMusicName", musicTitle);
                startActivity(talkIntent);
                break;
            case R.id.binder_music_play_imageView3:
                if (currentMusicIndex == 0) {
                    currentMusicIndex = musicDataItems.size() - 1;
                } else {
                    currentMusicIndex--;

                }
                if(!isPlaying){
                    isPlaying = true;
                    musicPlayImageView4.setImageResource(R.drawable.ic_play);
                }

                playTiemEnd.setText(getTime());
                currentMusicName=musicDataItems.get(currentMusicIndex).getMusicName();
                musicPlayText1.setText(currentMusicName);
               //rxBus1.post("Play-"+currentMusicIndex);
                playLastMusic();

                String appMusic=currentMusicName+"-"+musicDataItems.get(currentMusicIndex).getMusicArt();
                myApplication.setAppMusic(appMusic);

                break;
            case R.id.binder_music_play_imageView4:
                if (isPlaying) {
                    isPlaying = false;
                    musicPlayImageView4.setImageResource(R.drawable.ic_pause);
                    //rxBus1.post("Pause");
                    playPauseMusic();
                } else {
                    isPlaying = true;
                    musicPlayImageView4.setImageResource(R.drawable.ic_play);
                   // rxBus1.post("Playing");
                    playMusic();
                }

                break;
            case R.id.binder_music_play_imageView5:
                if (currentMusicIndex == musicDataItems.size() - 1) {
                    currentMusicIndex = 0;
                } else {
                    currentMusicIndex++;

                }
                if(!isPlaying){
                    isPlaying = true;
                    musicPlayImageView4.setImageResource(R.drawable.ic_play);
                }
                playTiemEnd.setText(getTime());
                currentMusicName=musicDataItems.get(currentMusicIndex).getMusicName();
                musicPlayText1.setText(currentMusicName);
                //rxBus1.post("Play-"+currentMusicIndex);
                playNextMusic();
                String appMusic1=currentMusicName+"-"+musicDataItems.get(currentMusicIndex).getMusicArt();
                myApplication.setAppMusic(appMusic1);
                break;
            case R.id.binder_music_play_imageView6:
                if (isLove) {
                    //设置成不喜欢
                    isLove = false;
                    musicPlayImageView6.setImageResource(R.drawable.ic_favorite_yes);
                    if (hasLoveHistory) {
                        //有历史，更新,要确保当前的音乐名要改变
                        update(0);
                    } else {
                        //没历史，直接插入
                        insert(0);
                    }
                } else {
                    //设置成喜欢
                    isLove = true;
                    musicPlayImageView6.setImageResource(R.drawable.love_red);
                    if (hasLoveHistory) {
                        //更新
                        update(1);
                    } else {
                        //直接插入
                        insert(1);
                    }
                }

                if (loveCursor != null) {
                    loveCursor.close();
                }
                break;

        }


    }

    public void playLastMusic(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                binderPool=BinderPool.newInstance(BinderPlayActivity.this);
                iMusicLast=MusicPlayServiceBinder.MusicLastImpl.asInterface(binderPool.queryBinder(2));
                try {
                    iMusicLast.last(musicDataItems.get(currentMusicIndex).getMusicUrl(),currentMusicIndex);
                }
                catch (RemoteException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void playPauseMusic(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                binderPool=BinderPool.newInstance(BinderPlayActivity.this);
               iMusicPuase=MusicPlayServiceBinder.MusicPauseImpl.asInterface(binderPool.queryBinder(3));
                try {
                    iMusicPuase.pause();
                }
                catch (RemoteException e){
                    e.printStackTrace();
                }
            }
        }).start();

    }
    public void playNextMusic(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                binderPool=BinderPool.newInstance(BinderPlayActivity.this);
                iMusicNext=MusicPlayServiceBinder.MusicNextImpl.asInterface(binderPool.queryBinder(1));
                try {
                    iMusicNext.next(musicDataItems.get(currentMusicIndex).getMusicUrl(),currentMusicIndex);
                }
                catch (RemoteException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public  void playMusic(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                binderPool= BinderPool.newInstance(BinderPlayActivity.this);
                IBinder iBinder=binderPool.queryBinder(0);
                iMusicPlay= MusicPlayServiceBinder.MusicPlayImpl.asInterface(iBinder);
                try {

                    iMusicPlay.play(musicDataItems.get(currentMusicIndex).getMusicUrl(),currentMusicIndex);
                }catch (RemoteException e){
                    e.printStackTrace();
                }

            }
        }).start();
    }

    //根据名字更新的
    public void insert(int isLoveInt) {
        ContentValues loveValues = new ContentValues();
        loveValues.put("love", isLoveInt);
        loveValues.put("musiclovename", currentMusicName);
        db.insert("MusicLove", null, loveValues);
    }

    public void update(int isLoveInt) {
        ContentValues  loveValues1 = new ContentValues();
        loveValues1.put("love", isLoveInt);
        db.update("Musiclove", loveValues1, "musiclovename=?", new String[]{currentMusicName});
    }

    public void toService(String activityModel){
        Intent intent=new Intent(this, MusicPlayService.class);
        intent.putExtra("current",currentMusicIndex);
        intent.putExtra("ActivityModel",activityModel);
        startService(intent);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (animation != null) {
            animation.cancel();
            animation = null;
        }
        if(rxBus1!=null){
            rxBus1=null;
        }


    }

    public String getTime(){
        long time=musicDataItems.get(currentMusicIndex).getMusicDuration()/1000;
        int minute=(int) time/60;
        int  second=(int) time-minute*60;
        return  minute+":"+second;

    }




}
