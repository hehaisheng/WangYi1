package com.example.pc_.wangyi.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.example.pc_.wangyi.model.ZhiHuNews;

/**
 * Created by pc- on 2017/5/22.
 */
public class CacheManager {

//    private LruCache<String, Bitmap> mMemoryCache;
//    int maxMemory = (int) Runtime.getRuntime().maxMemory();
//    int cacheSize = maxMemory / 8;
//    // 设置图片缓存大小为程序最大可用内存的1/8
//    mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
//        @Override
//        protected int sizeOf(String key, Bitmap bitmap) {
//            return bitmap.getByteCount();
//        }
//    };
    public LruCache<String,Bitmap> bitmapMemoryCache;
    public LruCache<String,String> contentMemoryCache;
//    public LruCache<String,ZhiHuNews> zhiHuNewsLruCache;

    public static  CacheManager cacheManager;
    public static CacheManager  newInstance()
    {
        if(cacheManager==null)
        {
            synchronized (CacheManager.class)
            {
                cacheManager=new CacheManager();
            }
        }
        return  cacheManager;
    }

    public LruCache<String,ZhiHuNews> getZhiHuNewsLruCacheInstance()
    {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        // 设置图片缓存大小为程序最大可用内存的1/8
        LruCache<String,ZhiHuNews> zhiHuNewsLruCache= new LruCache<String, ZhiHuNews>(cacheSize) {
            @Override
            protected int sizeOf(String key, ZhiHuNews zhiHuNews) {
                return zhiHuNews.toString().getBytes().length;
            }
        };
        return zhiHuNewsLruCache;
    }

    public LruCache<String,Bitmap> getBitmapLru()
    {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        // 设置图片缓存大小为程序最大可用内存的1/8
        bitmapMemoryCache= new LruCache<String, Bitmap>(cacheSize) {
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
            return bitmap.getByteCount();
          }
       };
       return bitmapMemoryCache;
    }
    public LruCache<String,String> getContentLru()
    {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        // 设置图片缓存大小为程序最大可用内存的1/8
        contentMemoryCache= new LruCache<String, String>(cacheSize) {
            @Override
            protected int sizeOf(String key, String content) {
                return content.getBytes().length;
            }
        };
        return contentMemoryCache;
    }

    public void setLruContent(String title,String content)
    {
               if(getLruContent(title)==null)
               {
                   contentMemoryCache.put(title,content);
               }
    }
    public String getLruContent(String title)
    {
            return contentMemoryCache.get(title);
    }

    public Bitmap getLruBitmap(String imageUrl)
    {
        return bitmapMemoryCache.get(imageUrl);
    }
    public void setLruBitmap(String imageUrl,Bitmap bitmap)
    {
        if(getLruBitmap(imageUrl)==null)
        {
            bitmapMemoryCache.put(imageUrl,bitmap);
        }
    }

}
