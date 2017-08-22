package com.example.pc_.wangyi.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.view.MyHorizontalScrollView;
import com.example.pc_.wangyi.view.activity.AboutActivity;
import com.example.pc_.wangyi.view.activity.DownActivity;
import com.example.pc_.wangyi.view.activity.FriendActivity;
import com.example.pc_.wangyi.view.activity.TestImageActivity;
import com.example.pc_.wangyi.view.activity.ZhiHuActivity;

import butterknife.BindView;


/**
 * Created by pc- on 2017/5/17.
 */
public class FriendFragment extends BaseFragment implements View.OnClickListener {


    public MyHorizontalScrollView myHorizontalScrollView;


    //第一个手指右移，变为3
    //第三个左移变为1

    public int[] drawablesId = new int[]{R.drawable.roundback, R.drawable.earth, R.drawable.headback, R.drawable.roundback};
    @BindView(R.id.friend_xiaoqi)
    Button friendXiaoqi;
    @BindView(R.id.friend_lishi)
    Button friendLishi;
    @BindView(R.id.friend_down)
    Button friendDown;
    @BindView(R.id.friend_circle)
    Button friendCircle;
    @BindView(R.id.friend_about)
    Button friendAbout;
    @BindView(R.id.friend_unLogin)
    Button friendUnLogin;
    @BindView(R.id.friend_imageLoader)
    Button  imageLoaderButton;



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void rxPreLoad() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.friend_fragment;
    }


    @Override
    public void initView() {


        myHorizontalScrollView = (MyHorizontalScrollView) getActivity().findViewById(R.id.myhorscrview);
        // myHorizontalScrollView.setDrawablesId(drawablesId);
        for (int aDrawablesId : drawablesId) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageResource(aDrawablesId);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            myHorizontalScrollView.addView(imageView);

        }


    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

        friendLishi.setOnClickListener(this);
        friendXiaoqi.setOnClickListener(this);
        friendDown.setOnClickListener(this);
        friendCircle.setOnClickListener(this);
        friendAbout.setOnClickListener(this);
        friendUnLogin.setOnClickListener(this);
        imageLoaderButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.friend_lishi:
                Intent hisIntent = new Intent(getActivity(), ZhiHuActivity.class);
                hisIntent.putExtra("History", 1);
                hisIntent.putExtra("model", 1);
                startActivity(hisIntent);
                break;
            case R.id.friend_xiaoqi:
                break;
            case R.id.friend_down:
                Intent toDownIntent = new Intent(getActivity(), DownActivity.class);
                startActivity(toDownIntent);
                break;
            case  R.id.friend_circle:
                Intent intent=new Intent(getActivity(), FriendActivity.class);
                startActivity(intent);
                break;
            case R.id.friend_about:
                Intent aboutIntent=new Intent(getActivity(), AboutActivity.class);
                startActivity(aboutIntent);
                break;
            case R.id.friend_imageLoader:
                Intent  imageIntent=new Intent(getActivity(), TestImageActivity.class);
                startActivity(imageIntent);
                break;
            case R.id.friend_unLogin:

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
                if(sharedPreferences.getBoolean("hasLogin",false)){
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putBoolean("hasLogin",false);
                    editor.apply();
                   // iNotification.inform("退出当前账号");
                    Toast.makeText(getActivity(),"退出当前账号",Toast.LENGTH_SHORT).show();
                }else {
                    //iNotification.inform("未登录");
                  Toast.makeText(getActivity(),"未登录",Toast.LENGTH_SHORT).show();
                }

                break;
        }

    }


    public  INotification iNotification;
    public void setiNotification(INotification iNotification){
        this.iNotification=iNotification;
    }
    public  interface  INotification{
        void  inform(String message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
