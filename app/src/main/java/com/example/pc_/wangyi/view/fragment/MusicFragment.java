package com.example.pc_.wangyi.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.view.activity.MusicListActivity;
import com.example.pc_.wangyi.view.activity.MusicLoveActivity;
import com.example.pc_.wangyi.view.activity.NetMusicActivity;
import com.example.pc_.wangyi.view.activity.ReceiverActivity;
import com.example.pc_.wangyi.view.activity.TransferActivity;

import butterknife.BindView;


/**
 * Created by pc- on 2017/5/17.
 */
public class MusicFragment extends BaseFragment implements View.OnClickListener {






    @BindView(R.id.bendi_text)
    TextView bendiText;
    @BindView(R.id.bendi_online_text)
    TextView bendiOnlineText;
    @BindView(R.id.bendi_love_text)
    TextView bendiLoveText;
    @BindView(R.id.bendi_fengxiang_text)
    TextView bendiFengxiangText;
    @BindView(R.id.bendi_receiver_text)
    TextView bendiReceiverText;
    @BindView(R.id.bendi_yinxiao_text)
    TextView bendiYinxiaoText;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void rxPreLoad() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.musiclist_new_activity;
    }


    @Override
    public void initView() {


    }

    @Override
    public void initData() {

    }



    @Override
    public void initEvent() {

        bendiText.setOnClickListener(this);
        bendiOnlineText.setOnClickListener(this);
        bendiLoveText.setOnClickListener(this);
        bendiFengxiangText.setOnClickListener(this);
        bendiReceiverText.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bendi_text:
                Intent intent = new Intent(getActivity(), MusicListActivity.class);
                startActivity(intent);
                break;
            case R.id.bendi_love_text:
                Intent loveIntent = new Intent(getActivity(), MusicLoveActivity.class);
                startActivity(loveIntent);
                break;
            case R.id.bendi_fengxiang_text:
                Intent trasferIntent = new Intent(getActivity(), TransferActivity.class);
                startActivity(trasferIntent);
                break;
            case R.id.bendi_receiver_text:
                Intent receiverIntent = new Intent(getActivity(), ReceiverActivity.class);
                startActivity(receiverIntent);
                break;
            case R.id.bendi_online_text:
                Intent onlineIntent = new Intent(getActivity(), NetMusicActivity.class);
                startActivity(onlineIntent);

        }

    }




}
