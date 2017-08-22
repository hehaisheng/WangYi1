package com.example.pc_.wangyi.view.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.TestModel;
import com.example.pc_.wangyi.presenter.TestPresenter;
import com.example.pc_.wangyi.presenter.impl.TestImpl;
import com.example.pc_.wangyi.retrofit.RxBus;
import com.example.pc_.wangyi.view.adapter.TestAdapter;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by pc- on 2017/5/21.
 */
public class TestActivity  extends  BaseActivity implements TestImpl {


    //mSwipeRefreshLayout.setOnRefreshListener(this);
    //mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));

    public TestPresenter testPresenter;
    public Subscription subscription;

    public TestImpl testImple;
    public SwipeRefreshLayout swipeRefreshLayout;
    public TestAdapter testAdapter;
    public RecyclerView recyclerView;
    private AVLoadingIndicatorView avLoadingIndicatorView;
    public RxBus rxBus1;



    @Override
    public int getLayout() {
        return R.layout.test_activity;
    }


    public void fecthData(){
        testPresenter.fecthStudentData(testImple);

    }



    @Override
    public void initView() {

        testImple= this;
        recyclerView=(RecyclerView) findViewById(R.id.main_recycler);
        avLoadingIndicatorView=(AVLoadingIndicatorView) findViewById(R.id.load_anim);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        testPresenter= TestPresenter.newInstance();
        rxBus1=RxBus.newInstance();



    }

    @Override
    public void initData() {

        subscription=rxBus1.tObservable(TestModel.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<TestModel>() {
                    @Override
                    public void call(TestModel s) {
                        List<TestModel> testModels=new ArrayList<TestModel>();
                        for(int i=0;i<10;i++)
                        {
                            TestModel testModel1=new TestModel();
                            testModel1.setName(s.getName());
                            testModels.add(testModel1);
                        }
                        testAdapter=new TestAdapter(R.layout.test_item,testModels);
                        recyclerView.setAdapter(testAdapter);
                    }
                });
    }

    @Override
    public void initEvent() {

    }



    @Override
    public void loading() {



    }

    @Override
    public void loadSuccess() {

        avLoadingIndicatorView.setVisibility(View.GONE);
        Toast.makeText(this,"加载成功",Toast.LENGTH_LONG).show();

    }

    @Override
    public void loadError() {

        Toast.makeText(this,"加载错误",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!subscription.isUnsubscribed())
        {
            subscription.unsubscribe();
        }
    }
}
