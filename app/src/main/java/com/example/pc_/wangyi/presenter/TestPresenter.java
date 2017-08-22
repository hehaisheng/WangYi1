package com.example.pc_.wangyi.presenter;

import android.os.Handler;
import android.util.Log;

import com.example.pc_.wangyi.model.TestModel;
import com.example.pc_.wangyi.presenter.impl.TestImpl;
import com.example.pc_.wangyi.retrofit.RxBus;


/**
 * Created by pc- on 2017/5/21.
 */
public class TestPresenter {

     public static  TestPresenter testPresenter;
     public RxBus rxBus;
     public static  TestPresenter newInstance()
    {
        if(testPresenter==null)
        {
            synchronized (TestPresenter.class)
            {
                if(testPresenter==null)
                {
                    testPresenter=new TestPresenter();
                }
            }
        }
        return testPresenter;
    }
    public void  fecthStudentData(TestImpl testImple)
    {
        //可能post时异步的所以该方法不会在这里被延迟
        //post就是异步
        rxBus=RxBus.newInstance();
        testImple.loading();
        final TestModel testModel=new TestModel();
        testImple.loadSuccess();
        testModel.setName("测试mvp和rxbus");
        new Handler().postDelayed(new Runnable(){
            public void run() {
                Log.d("延迟","哈哈");
                //让该handler内的线程停止5秒
                rxBus.post(testModel);
            }
        }, 5000);
    }
}
