package com.example.pc_.wangyi.utils;

import android.util.Log;

import com.example.pc_.wangyi.model.MusicDataItem;
import com.example.pc_.wangyi.view.MyApplication;

import java.util.List;

/**
 * Created by pc- on 2017/6/10.
 */
public class  QuickSort {

    public static QuickSort quickSort;
    public MyApplication myApplication;
    public static  QuickSort newInstance(){
        if(quickSort==null){
            synchronized (QuickSort.class){
                if(quickSort==null){
                    quickSort=new QuickSort();
                }
            }
        }
        return  quickSort;
    }
    public QuickSort(){
         myApplication=MyApplication.getNewInstance();
    }
    public void   quickSort(List<MusicDataItem>  sortItems, int low, int high){
        myApplication.setGedanList(sortItems);


        int pivot[];
        while (low<high){

            pivot=partition(myApplication.getGedanList(),low,high);
            quickSort(myApplication.getGedanList(),low,pivot[0]-pivot[1]-1);
            low=pivot[0]+pivot[2]+1;

        }
    }
    public int[] partition(List<MusicDataItem> sortItemList,int low,int high){
        Log.d("点击","快排有反应"+"排序的主要逻辑");
        Log.d("点击","快排有反应"+"排序的主要逻辑");
        Log.d("点击","快排有反应"+"排序的主要逻辑");
        myApplication.setGedanList(sortItemList);
        int first=low;
        int last=high;
        int left=low;
        int right=high;
        int leftLen=0;
        int rightLen=0;
        MusicDataItem sortItem=selectPivotOfThree(myApplication.getGedanList(),low,high);
        int key=sortItem.getMusicCount();
        while(low<high){
            while (high>low&&myApplication.getGedanList().get(high).getMusicCount()>=key){
                if(myApplication.getGedanList().get(high).getMusicCount()==key){
                    swap(myApplication.getGedanList(),right,high);
                    right--;
                    rightLen++;
                }
                high--;

            }
            myApplication.getGedanList().get(low).setMusicCount(myApplication.getGedanList().get(high).getMusicCount());
            myApplication.getGedanList().get(low).setMusicName(myApplication.getGedanList().get(high).getMusicName());
            while(low<high&&myApplication.getGedanList().get(low).getMusicCount()<=key){
                if(myApplication.getGedanList().get(low).getMusicCount()==key){
                    swap(myApplication.getGedanList(),left,low);
                    left++;
                    leftLen++;
                }
                low++;
            }
            myApplication.getGedanList().get(high).setMusicCount(myApplication.getGedanList().get(low).getMusicCount());
            myApplication.getGedanList().get(high).setMusicName(myApplication.getGedanList().get(low).getMusicName());

        }
        myApplication.getGedanList().get(low).setMusicCount(key);
        myApplication.getGedanList().get(low).setMusicName(myApplication.getGedanList().get(myApplication.getMiddleIndex()).getMusicName());
        int i=low-1;
        int j=first;
        while (j<left&&myApplication.getGedanList().get(i).getMusicCount()!=key){
            swap(myApplication.getGedanList(),i,j);
            i--;
            j++;
        }
        i=low+1;
        j=last;
        while(j>right&&myApplication.getGedanList().get(i).getMusicCount()!=key){
            swap(myApplication.getGedanList(),i,j);
            i++;
            j--;

        }
        return  new int[] {low,leftLen,rightLen};


    }
    public MusicDataItem selectPivotOfThree(List<MusicDataItem> sortItems,int low,int high){

        myApplication.setGedanList(sortItems);
        Log.d("点击","快排有反应"+"取中间值");
        Log.d("点击","快排有反应"+"取中间值");
        Log.d("点击","快排有反应"+"取中间值");

        int middle=(low+high)/2;
        if(myApplication.getGedanList().get(middle).getMusicCount()>myApplication.getGedanList().get(high).getMusicCount()){
            swap(myApplication.getGedanList(),middle,high);
        }
        if(myApplication.getGedanList().get(low).getMusicCount()>myApplication.getGedanList().get(high).getMusicCount()){
             swap(myApplication.getGedanList(),low,high);
        }

        if(myApplication.getGedanList().get(middle).getMusicCount()>myApplication.getGedanList().get(low).getMusicCount()){
            swap(myApplication.getGedanList(),middle,low);
        }
        myApplication.setMiddleIndex(low);
        return   myApplication.getGedanList().get(low);


    }

    public void swap(List<MusicDataItem> sortItems1,int start,int end){



        MusicDataItem sortItem=new MusicDataItem();

        sortItem.setMusicName(sortItems1.get(start).getMusicName());
        sortItem.setMusicCount(sortItems1.get(start).getMusicCount());

        sortItem.setMusicArt(sortItems1.get(start).getMusicArt());
        sortItem.setMusicDuration(sortItems1.get(start).getMusicDuration());
        sortItem.setMusicUrl(sortItems1.get(start).getMusicUrl());
        sortItem.setSize(sortItems1.get(start).getSize());


        sortItems1.get(start).setMusicName(sortItems1.get(end).getMusicName());
        sortItems1.get(start).setMusicCount(sortItems1.get(end).getMusicCount());
        sortItems1.get(start).setMusicDuration(sortItems1.get(end).getMusicDuration());
        sortItems1.get(start).setSize(sortItems1.get(end).getSize());
        sortItems1.get(start).setMusicUrl(sortItems1.get(end).getMusicUrl());
        sortItems1.get(start).setMusicArt(sortItems1.get(end).getMusicArt());


        sortItems1.get(end).setMusicName(sortItem.getMusicName());
        sortItems1.get(end).setMusicCount(sortItem.getMusicCount());
        sortItems1.get(end).setMusicArt(sortItem.getMusicArt());
        sortItems1.get(end).setMusicUrl(sortItem.getMusicUrl());
        sortItems1.get(end).setSize(sortItem.getSize());
        sortItems1.get(end).setMusicDuration(sortItem.getMusicDuration());
        //数据已经改变，所以也改变全局的数据
        myApplication.setGedanList(sortItems1);
    }

}
