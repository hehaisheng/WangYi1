package com.example.pc_.wangyi.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.example.pc_.wangyi.retrofit.ComSchedulers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by pc- on 2017/6/6.
 */
public class HHImageLoader {

    //HHImageLoader.newInstance(context).load(url).size(100,100).into(image)
    //默认已使用缓存，对图片内存压缩，大小由用户自己设置

    public static HHImageLoader hhImageLoader;
    public LruCache<String,Bitmap> lruCache;
    public DiskLruCache  diskLruCache;
    public File dirFile;
    public Context context;
    public OkHttpClient okHttpClient;




    public static  final long DISK_CACHE_SIZE=1024*1024*30;
    public int  reqWidthSize,reqHeightSize;
    public String bitmapUrl;


    public static HHImageLoader  newInstance(Context context){
        if(hhImageLoader==null){
            synchronized (HHImageLoader.class){
                if(hhImageLoader==null){
                    hhImageLoader=new HHImageLoader(context);
                }

            }
        }
        return hhImageLoader;
    }

    public HHImageLoader(Context context){
        this.context=context.getApplicationContext();
        init();
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void init(){
        okHttpClient=new OkHttpClient();
         long maxMemory=Runtime.getRuntime().maxMemory()/1024;
         int lruSize=(int) maxMemory/8;
          lruCache=new LruCache<String,Bitmap>(lruSize){
              @Override
              protected int sizeOf(String key, Bitmap value) {
                  return value.getRowBytes()*value.getHeight()/1024;
              }
          };
        File file=openOrCreateDir();
        if(!file.exists()){
            file.mkdirs();
        }
        long hasBlock;
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.GINGERBREAD){
            hasBlock=file.getUsableSpace();
        }else{
            final StatFs statFs=new StatFs(file.getPath());
            hasBlock=statFs.getBlockSizeLong()*statFs.getAvailableBlocksLong();
        }
        if(hasBlock>=DISK_CACHE_SIZE){
            try {
                diskLruCache=DiskLruCache.open(file,1,1,DISK_CACHE_SIZE);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        else{
            try {
                diskLruCache=DiskLruCache.open(file,1,1,hasBlock/2);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }

    }
    public File openOrCreateDir(){
        String cachePath;
        boolean isExternal= Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if(isExternal){
            cachePath=context.getApplicationContext().getExternalCacheDir().getPath();
        }else {
            cachePath=context.getApplicationContext().getCacheDir().getPath();
        }
        return dirFile=new File(cachePath+File.separator+"bitmap");
    }



    public  HHImageLoader load(String url){
        this.bitmapUrl=url;
        return this;
    }

    public HHImageLoader size(int width,int height){
        this.reqWidthSize=width;
        this.reqHeightSize=height;
        return this;

    }
    public void into(final ImageView imageView){

        Observable.just(bitmapUrl)
                .map(new Func1<String,Bitmap>() {
                    @Override
                    public Bitmap call(String s) {
                        return loadBitmap(s);
                    }
                })
                .compose(ComSchedulers.<Bitmap>applyIoSchedulers())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                    }
                });


    }


    //这里可以使用Observable
    public Bitmap loadBitmap(String url){
        //从内存加载
        Bitmap bitmap=loadBitmapFromMemory(url);
        if(bitmap!=null){
            return bitmap;
        }
        //如果内存没有，就加载硬盘，硬盘有的话，就把硬盘的加入缓存
        //如果硬盘也没有，就从网络获取,获取后加入内存和硬盘
        bitmap=loadBitmapFromDiskCache(url,reqWidthSize,reqHeightSize);
        if(bitmap!=null){
            return bitmap;
        }
        bitmap= loadBitmapFromNet(url,reqWidthSize,reqHeightSize);
        return bitmap;
    }
    public Bitmap loadBitmapFromMemory(String url){
        String key=hashKeyFromUrl(url);
        return lruCache.get(key);
    }
    //内存没有才调用该方法
    public Bitmap loadBitmapFromDiskCache(String url,int width,int height){
        String key=hashKeyFromUrl(url);
        if(diskLruCache==null){
          return null;
        }
        Bitmap bitmap=null;
        try{
            DiskLruCache.Snapshot snapshot=diskLruCache.get(key);
            if(snapshot!=null){
                FileInputStream fileInputStream=(FileInputStream) snapshot.getInputStream(0);
                //FileInputStream是获取有序的数据，但是压缩时，数据变化了
                FileDescriptor fileDescriptor=fileInputStream.getFD();
                bitmap=ImageSizeManager.newInstance().decodeSampleBitmapFromFD(fileDescriptor,width,height);
                if(bitmap!=null){
                    lruCache.put(key,bitmap);
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return bitmap;

    }
    public Bitmap loadBitmapFromNet(String url,int reqWidthSize,int reqHeightSize){
        String key=hashKeyFromUrl(url);
        try{
            DiskLruCache.Editor editor=diskLruCache.edit(key);
            if(editor!=null){
                OutputStream outputStream=editor.newOutputStream(0);
                if(fromUrlToStream(url,outputStream)){
                    editor.commit();
                }
                else {
                    editor.abort();
                }
                diskLruCache.flush();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
       return loadBitmapFromDiskCache(url,reqWidthSize,reqHeightSize);
    }

    //避免url的不合法字符
    public String hashKeyFromUrl(String url){
        String key;
        try{
            MessageDigest messageDigest=MessageDigest.getInstance("MD5");
            messageDigest.update(url.getBytes());
            //字节转为Hex字符
            key=bytesToHexString(messageDigest.digest());

        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            key=String.valueOf(url.hashCode());
        }
        return key;
    }

    public String bytesToHexString(byte[] bytes){
        StringBuilder stringBuilder=new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                stringBuilder.append("0");
            }
            stringBuilder.append(hex);
        }
        return stringBuilder.toString();
    }
    public boolean fromUrlToStream(String url,OutputStream outputStream){
        Request request=new Request.Builder().url(url).build();
        Call call=okHttpClient.newCall(request);
        try {
            Response response=call.execute();
            BufferedInputStream  inputStream=new BufferedInputStream(response.body().byteStream());
            BufferedOutputStream outputStream1=new BufferedOutputStream(outputStream);
            int len;
            byte[] buff=new byte[2048];
            while ((len=inputStream.read(buff))!=-1){
                outputStream1.write(buff,0,len);

            }
            return true;

        }catch (IOException e){
            e.printStackTrace();
        }
        return  false;

    }


}
