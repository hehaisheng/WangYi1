package com.example.pc_.wangyi.presenter;

import android.content.Context;

import com.example.pc_.wangyi.model.DouBanContent;
import com.example.pc_.wangyi.model.DouBanNews;
import com.example.pc_.wangyi.model.GuoQiaoItem;
import com.example.pc_.wangyi.model.ZhiHuContent;
import com.example.pc_.wangyi.model.ZhiHuNews;
import com.example.pc_.wangyi.retrofit.Api;
import com.example.pc_.wangyi.retrofit.NewsFetchRetrofit;

import rx.Observable;


/**
 * Created by pc- on 2017/5/17.
 */
public class NewDataPresenter {


    //Execution failed for task ':app:compileDebugJavaWithJavac'.
    //Compilation failed; see the compiler error output for details.
    //因为在删除时把库删了，所以需要重新导入
    public static Context context;
    public static NewDataPresenter newDataPresenter;
    public Observable<ZhiHuNews> zhiHuNewsObservable;
    public Observable<ZhiHuContent> zhiHuContentObservable;
    public Observable<GuoQiaoItem> guoQiaoItemObservable;
    public Observable<String> resultObservable;
    public Observable<DouBanNews> douBanNewsObservable;
    public Observable<DouBanContent> douBanContentObservable;

    public static NewDataPresenter getInstance(Context context1) {
        context=context1;
        if(newDataPresenter==null) {
            synchronized (NewDataPresenter.class) {
                if(newDataPresenter==null) {
                    newDataPresenter=new NewDataPresenter();
                }
            }
        }
        return newDataPresenter;
    }
    public Observable<ZhiHuNews> fecthNews(int id) {

        zhiHuNewsObservable= NewsFetchRetrofit.newInstance().createRetfofit(0,"Other",context).create(Api.class).fetchZhiHuNews(id);
        return  zhiHuNewsObservable;


    }
    public Observable<ZhiHuContent> fecthContent(int id) {

        zhiHuContentObservable= NewsFetchRetrofit.newInstance().createRetfofit(0,"Other",context).create(Api.class).fetchZhiHuContent(id);
        return zhiHuContentObservable;

    }
    public Observable<GuoQiaoItem> fecthGuoQiao() {
       guoQiaoItemObservable= NewsFetchRetrofit.newInstance().createRetfofit(1,"Other",context).create(Api.class).fecthQuoQiaoNews();
        return guoQiaoItemObservable;
    }
    public Observable<String> fecthGuoQiaoContent(int id) {
        resultObservable=NewsFetchRetrofit.newInstance().createRetfofit(2,"GuoQiaoType",context).create(Api.class).fecthGuoQiaoContent(id);
        return resultObservable;
    }
    public Observable<DouBanNews> fetchDouBanNews(String  date) {
        douBanNewsObservable=NewsFetchRetrofit.newInstance().createRetfofit(3,"Other",context).create(Api.class).fecthDouBanNews(date);
        return douBanNewsObservable;

    }
    public Observable<DouBanContent> fetchDouBanContent(int id) {
        douBanContentObservable=NewsFetchRetrofit.newInstance().createRetfofit(3,"Other",context).create(Api.class).fecthDouBanContent(id);
        return douBanContentObservable;
    }



}
