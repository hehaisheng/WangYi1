package com.example.pc_.wangyi.view.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.NetMusic;
import com.example.pc_.wangyi.model.NetMusicItem;
import com.example.pc_.wangyi.view.adapter.NetAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc- on 2017/5/26.
 */
public class NetMusicActivity extends BaseActivity{


    public RecyclerView recyclerView;
    public NetAdapter netAdapter;
    public NetMusicItem netMusicItem;

    public List<NetMusic>  netMusics=new ArrayList<>();


    @Override
    public int getLayout() {
        return R.layout.net_music_activtiy;
    }


    @Override
    public void initView() {
        recyclerView=(RecyclerView) findViewById(R.id.net_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        netMusicItem=new NetMusicItem();
        for(int i=0;i<netMusicItem.musicName.length;i++)
        {
            NetMusic netMusic=new NetMusic();
            netMusic.setMusicName(netMusicItem.musicName[i]);
            netMusic.setMusicArt(netMusicItem.musicArt[i]);
            netMusic.setMusicUrl(netMusicItem.musicUrl[i]);
            netMusics.add(netMusic);
        }
        netAdapter=new NetAdapter(R.layout.net_music_item,netMusics);
        recyclerView.setAdapter(netAdapter);

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {


                Toast.makeText(NetMusicActivity.this,"因版权原因，不能正常播放网络歌曲",Toast.LENGTH_SHORT).show();

//                Intent playActivityIntent=new Intent(NetMusicActivity.this,MusicPlayActivity.class);
//                playActivityIntent.putExtra("MusicName","泡沫");
//                playActivityIntent.putExtra("isNet",true);
//                startActivity(playActivityIntent);

            }
        });
        recyclerView.addOnItemTouchListener(new OnItemLongClickListener() {
            @Override
            public void SimpleOnItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                Toast.makeText(NetMusicActivity.this,"因版权原因，不能正常下载网络歌曲",Toast.LENGTH_SHORT).show();
//                String downUrl="http://dl.stream.qqmusic.qq.com/C400003uB0Lh0pexs8.m4a?vkey=25F21E7F4390871B0BA65BF75CA381BB2BB5985D731A19A2B751E06F6C18B7254090A282E432EDA99B255106E2346A745B6C1643CDB8FBEA&guid=688731706&uin=0&fromtag=66";
//                Intent playIntent1=new Intent(NetMusicActivity.this, MusicDownService.class);
//                playIntent1.putExtra("DownUrl",downUrl);
//                startService(playIntent1);

            }
        });


    }
}
