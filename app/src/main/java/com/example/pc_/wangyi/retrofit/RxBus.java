package com.example.pc_.wangyi.retrofit;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by pc- on 2017/5/21.
 */
public class RxBus {

    public static volatile RxBus defaultBus;
    //一个集合，对数据的保存和发生
    public final Subject<Object,Object> subject;
    public RxBus()
    {
        subject=new SerializedSubject<>(PublishSubject.create());
    }
    //在rxBus创建的时候，创建subject，rxBus在调用方法时
    //其实就是调用subject的方法
    public static RxBus newInstance()
    {
        if(defaultBus==null)
        {
            synchronized (RxBus.class)
            {
                defaultBus=new RxBus();
            }
        }
        return defaultBus;
    }
    public void post(Object object)
    {
        subject.onNext(object);
    }
    //<T> 各种类型的Observable<T>
    public <T>  Observable<T> tObservable(Class<T> eventType)
    {
        return subject.ofType(eventType);
    }
}
