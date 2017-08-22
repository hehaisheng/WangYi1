package com.example.pc_.wangyi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc_.wangyi.retrofit.RxBus;
import com.example.pc_.wangyi.view.MyApplication;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by pc- on 2017/5/22.
 */
public abstract class BaseFragment extends Fragment {


    public Unbinder unbinder;
    public RxBus rxBus;
    public MyApplication myApplication;
    public CompositeSubscription compositeSubscription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(), container, false);
        unbinder=ButterKnife.bind(this, rootView);
        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        myApplication=MyApplication.getNewInstance();
        compositeSubscription=new CompositeSubscription();
        rxBus=RxBus.newInstance();
        rxPreLoad();
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        initEvent();


    }

    public abstract void  rxPreLoad();
    public abstract int getLayoutId();
    public abstract void initView();
    public  abstract void initData();
    public  abstract void initEvent();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(unbinder!=null){
            unbinder.unbind();
        }

        if(compositeSubscription!=null){
            compositeSubscription.unsubscribe();
            compositeSubscription=null;
        }
        if(rxBus!=null){
            rxBus=null;
        }
    }
}
