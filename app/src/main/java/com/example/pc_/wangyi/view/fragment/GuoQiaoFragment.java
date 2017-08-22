package com.example.pc_.wangyi.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.GuoQiaoItem;
import com.example.pc_.wangyi.presenter.impl.IFetchCompleteImpl;
import com.example.pc_.wangyi.retrofit.FetchManager;
import com.example.pc_.wangyi.utils.NetConnectUtil;
import com.example.pc_.wangyi.view.activity.ZhiHuContentActivity;
import com.example.pc_.wangyi.view.adapter.GuoQiaoAdapter;

import java.util.List;

import rx.Subscription;

/**
 * Created by pc- on 2017/5/14.
 */
public class GuoQiaoFragment extends BaseFragment implements IFetchCompleteImpl{



    public ListView listView;
    public FetchManager fetchManager;

    public GuoQiaoAdapter guoQiaoAdapter;

    public Subscription subscription;

    public int guoqiaoIndex;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void rxPreLoad() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.guoqiao_fragment;
    }


    @Override
    public void initView() {

        listView=(ListView) getActivity().findViewById(R.id.guoqiao_list);
        guoQiaoAdapter=new GuoQiaoAdapter(getActivity());
        fetchManager=FetchManager.newInstance(getActivity());

    }

    @Override
    public void initData() {
        if(myApplication.getGuoqiaoList()!=null&&myApplication.getGuoqiaoList().size()!=0){
            guoQiaoAdapter.setItem(myApplication.getGuoqiaoList());
            listView.setAdapter(guoQiaoAdapter);
        }else{
            if(NetConnectUtil.isNetworkConnected(getActivity())) {
                subscription=fetchManager.fetch("GuoQiao",this);
            }
        }

    }

    @Override
    public void initEvent() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent guoqiaoIntent = new Intent(getActivity(), ZhiHuContentActivity.class);
                                    guoqiaoIndex = myApplication.getGuoqiaoList().get(position).getId();
                                    guoqiaoIntent.putExtra("Id", guoqiaoIndex);
                                    guoqiaoIntent.putExtra("ModelType", "GuoQiao");
                                    startActivity(guoqiaoIntent);
                                }
                            });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(subscription!=null){
            if(!subscription.isUnsubscribed()){
                subscription.unsubscribe();
            }
        }

        if(fetchManager!=null){
            fetchManager.setiFetchComplete(null);

        }
    }

    @Override
    public void loadSuccess(Object object) {
        List<GuoQiaoItem.result> results=((GuoQiaoItem) object).getResult();
        guoQiaoAdapter.setItem(results);
        listView.setAdapter(guoQiaoAdapter);

    }
}
