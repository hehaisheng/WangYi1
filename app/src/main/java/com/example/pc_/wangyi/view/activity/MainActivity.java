package com.example.pc_.wangyi.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.utils.NetConnectUtil;
import com.example.pc_.wangyi.view.MyApplication;
import com.example.pc_.wangyi.view.fragment.ArticleFragment;
import com.example.pc_.wangyi.view.fragment.FriendFragment;
import com.example.pc_.wangyi.view.fragment.MusicFragment;
import com.example.pc_.wangyi.view.fragment.MyDialogFragment;

import java.lang.reflect.Field;

import butterknife.BindView;


/**
 * Created by pc- on 2017/5/14.
 */
public class MainActivity extends BaseActivity {



    public ArticleFragment articleFragment;
    public MusicFragment musicFragment;
    public FriendFragment friendFragment;
    public MyApplication myApplication;
    public MyDialogFragment myDialogFragment;



    @BindView(R.id.main_menu1)
    ImageView mainMenu1;
    @BindView(R.id.main_music)
    ImageView mainMusic;
    @BindView(R.id.main_wen)
    ImageView mainWen;
    @BindView(R.id.main_friend)
    ImageView mainFriend;




    @Override
    public int getLayout() {
        return R.layout.activity_main_main;
    }


    @Override
    public void initView() {

        if(!NetConnectUtil.isNetworkConnected(this)){
            Bundle bundle = new Bundle();
            bundle.putString("dialogTitle", "无网络提示");
            bundle.putString("dialogcontent", "是否去连接网络");
            myDialogFragment = MyDialogFragment.newInstance(bundle);
            myDialogFragment.show(getFragmentManager(),"dialogFragment");
            myDialogFragment.setDialogListener(new MyDialogFragment.IDialogListener() {
                @Override
                public void confirm() {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    startActivity(intent);
                }

                @Override
                public void cancel() {
                    MainActivity.this.finish();
                }
            });
        }
        else{
            SharedPreferences sharedPreferences=getSharedPreferences("Login",MODE_PRIVATE);
            boolean hasLogin=sharedPreferences.getBoolean("hasLogin",false);
            if(!hasLogin){
                Intent intent = new Intent();
                intent.setClass(this, LoginActivity.class);
                startActivity(intent);
                this.finish();
            }

            articleFragment = new ArticleFragment();
            musicFragment = new MusicFragment();
            friendFragment = new FriendFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction showTransaction = fragmentManager.beginTransaction();
            showTransaction.add(R.id.main_content, articleFragment);
            showTransaction.add(R.id.main_content, friendFragment);
            showTransaction.add(R.id.main_content, musicFragment);
            showTransaction.commit();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!NetConnectUtil.isNetworkConnected(this)){
            if(myDialogFragment!=null){
                myDialogFragment.isHidden();
            }
        }


    }

    @Override
    public void initData() {


    }

    @Override
    public void initEvent() {
        mainWen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (articleFragment == null) {
                    articleFragment = new ArticleFragment();
                }
                replaceFragment(0);

            }
        });
        mainMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicFragment == null) {
                    musicFragment = new MusicFragment();
                }
                replaceFragment(1);


            }
        });
        mainFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (friendFragment == null) {
                    friendFragment = new FriendFragment();
                }
                replaceFragment(2);


            }
        });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void replaceFragment(int id) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (id) {
            case 0:
                transaction.hide(musicFragment);
                transaction.hide(friendFragment);
                transaction.show(articleFragment);
                break;
            case 1:
                transaction.hide(articleFragment);
                transaction.hide(friendFragment);
                transaction.show(musicFragment);
                break;
            case 2:
                transaction.hide(musicFragment);
                transaction.hide(articleFragment);
                transaction.show(friendFragment);
                break;

        }

        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fixInputMethodManagerLeak(this);
        if(myDialogFragment!=null){
            myDialogFragment.setDialogListener(null);
            myDialogFragment=null;
        }
    }

    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String [] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f = null;
        Object obj_get = null;
        for (int i = 0;i < arr.length;i ++) {
            String param = arr[i];
            try{
                f = imm.getClass().getDeclaredField(param);
                if (f.isAccessible() == false) {
                    f.setAccessible(true);
                } // author: sodino mail:sodino@qq.com
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    }
                }
            }catch(Throwable t){
                t.printStackTrace();
            }
        }
    }



}
