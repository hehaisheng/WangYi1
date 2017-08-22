package com.example.pc_.wangyi.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileDescriptor;

/**
 * Created by pc- on 2017/6/6.
 */
public class ImageSizeManager {

    public static ImageSizeManager imageSizeManager;

    public static ImageSizeManager newInstance(){
        if(imageSizeManager==null){
            synchronized (ImageSizeManager.class){
                if(imageSizeManager==null){
                    imageSizeManager=new ImageSizeManager();
                }
            }
        }
        return imageSizeManager;
    }

    public Bitmap decodeSampleBitmapFromFD(FileDescriptor fileDescriptor, int width, int height){
        BitmapFactory.Options options= new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor,null,options);
        options.inSampleSize=calInSampleSize(options,width,height);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeFileDescriptor(fileDescriptor,null,options);


    }
    public int calInSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){
        if(reqWidth==0||reqHeight==0){
            return 1;
        }

        int inSampleSize=1;
        int bitmapHeight=options.outHeight;
        int bitmapWidth=options.outWidth;
        //如果图片的本来大小大于需要设置的大小


       if(bitmapHeight>reqHeight||bitmapWidth>reqWidth){
            int halfHeight=bitmapHeight/2;
            int halfWidth=bitmapWidth/2;
            while (halfHeight/inSampleSize>reqHeight&&halfWidth/inSampleSize>reqWidth){
              inSampleSize*=2;
          }
        }
        return  inSampleSize;
    }
}
