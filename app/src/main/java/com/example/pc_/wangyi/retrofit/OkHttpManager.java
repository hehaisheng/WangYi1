package com.example.pc_.wangyi.retrofit;

import android.content.Context;

import com.example.pc_.wangyi.utils.NetConnectUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by pc- on 2017/5/22.
 */
public class OkHttpManager {


    public static OkHttpManager okHttpManager;
    public Context context;

   public static OkHttpManager newInstance()
   {
       if(okHttpManager==null)
       {
           synchronized (OkHttpManager.class)
           {
               if(okHttpManager==null)
               {
                   okHttpManager=new OkHttpManager();
               }
           }

       }
       return okHttpManager;

   }
    public OkHttpClient createClient(Context context)
    {
        this.context=context.getApplicationContext();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addNetworkInterceptor(getNetWorkInterceptor())
                .addInterceptor(getInterceptor());
        return builder.build();
    }

    public Interceptor getInterceptor(){
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetConnectUtil.isNetworkConnected(context)) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                return chain.proceed(request);
            }
        };
    }

    /**
     * 设置连接器  设置缓存
     */
    public Interceptor getNetWorkInterceptor (){
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                if (NetConnectUtil.isNetworkConnected(context)) {
                    int maxAge = 0 ;
                    // 有网络时 设置缓存超时时间0个小时
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")
                            .build();
                } else {
                    // 无网络时，设置超时为1天
                    int maxStale = 60 * 60 * 24 ;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return response;
            }
        };
    }
}