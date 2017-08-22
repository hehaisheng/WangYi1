package com.example.pc_.wangyi.retrofit;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by pc- on 2017/5/17.
 */


public class NewsFetchRetrofit
{

    //因为果壳的使用过一次retfofit，retfofit是单例模式，所以到知乎使用时
    //出现url错误，导致崩溃
    //这里创建retrofit的方式很不好，因为创建过多的retrofit
    //是否只创建一个base_url对应情况更换
    public static NewsFetchRetrofit newsFetchRetrofit;
    public static Retrofit zhihuRetrofit;
    public static Retrofit guoqiaoRetrofit;
    public static Retrofit guoqiaoContentRetrofit;
    public static Retrofit doubanRetrofit;

    public String url;
    public static NewsFetchRetrofit newInstance()
    {
        if(newsFetchRetrofit==null)
        {
            synchronized (NewsFetchRetrofit.class)
            {
                if(newsFetchRetrofit==null)
                {
                    newsFetchRetrofit=new NewsFetchRetrofit();
                }

            }
        }
        return newsFetchRetrofit;
    }
    public Retrofit createRetfofit(int index,String retrofitType,Context context)
    {
        if(index==0)
        {
            url= Api.ZHIHU_BASE_URL;
            zhihuRetrofit=newInstance(url,retrofitType,context);
            return zhihuRetrofit;
        }
        else if(index==1)
        {
            url=Api.GUOQIAO_NEW;
            guoqiaoRetrofit=newInstance(url,retrofitType,context);
            return guoqiaoRetrofit;
        }
        else if(index==2)
        {
            url=Api.GUOQIAO_CONTENT;
            guoqiaoContentRetrofit=newInstance(url,retrofitType,context);

            return  guoqiaoContentRetrofit;
        }
        else if(index==3)
        {
            url=Api.DOUBAN_BASE_URL;
            doubanRetrofit=newInstance(url,retrofitType,context);
            return doubanRetrofit;
        }
        return  null;

    }
    public Retrofit newInstance(String strUrl,String retrofitType,Context context)
    {

        if(retrofitType.equals("GuoQiaoType"))
        {
            return new Retrofit.Builder()
                    .baseUrl(strUrl)
                    .client(OkHttpManager.newInstance().createClient(context))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        else if(retrofitType.equals("Other"))
        {
            return  new Retrofit.Builder()
                    .baseUrl(strUrl)
                    .client(OkHttpManager.newInstance().createClient(context))
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return null;

    }

}