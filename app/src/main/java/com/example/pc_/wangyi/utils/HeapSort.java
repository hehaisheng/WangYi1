package com.example.pc_.wangyi.utils;


import com.example.pc_.wangyi.model.SortItem;
import com.example.pc_.wangyi.view.MyApplication;

import java.util.List;

/**
 * Created by pc- on 2017/6/10.
 */
public class HeapSort {


    //使用该算法的原因是速度和快排差不多，实现比较简单
    //且使用的内存辅助比较少

    public static  HeapSort heapSort;
    public MyApplication myApplication;
    public static HeapSort newInstance(){
        if(heapSort==null){
            synchronized (HeapSort.class){
                if(heapSort==null){
                    heapSort=new HeapSort();
                }
            }
        }
        return heapSort;
    }
    public HeapSort(){
        myApplication=MyApplication.getNewInstance();
    }


    public List<SortItem>  heapSort(List<SortItem> sortItemList){

        //原始数据
        myApplication.setHeapSort(sortItemList);
        int i;
        int length=sortItemList.size();
        for(i=length/2-1;i>=0;i--){
            //对application的数据排序
            heapAdjust(myApplication.getSortItems(),i,length);
        }
        for(i=sortItemList.size()-1;i>=1;i--){
            swap(myApplication.getSortItems(),1,i);
            heapAdjust(myApplication.getSortItems(),1,i);
        }
        return myApplication.getSortItems();

    }
    public void heapAdjust(List<SortItem> sortItems,int s,int length){
        int temp,j;
        //播放的次数
        temp=sortItems.get(s).getPlayIndex();
        for(j=2*s;j<=length-1;j*=2){
            if(sortItems.get(j+1).getPlayIndex()<sortItems.get(j+2).getPlayIndex()){
                 j=j+1;
            }
            else{
                j=j+2;
            }
            if(sortItems.get(s).getPlayIndex()>=sortItems.get(j).getPlayIndex()){
                break;
            }
            int tempIndex=sortItems.get(j).getPlayIndex();
            sortItems.get(s).setPlayIndex(tempIndex);
            sortItems.get(s).setMusicName(sortItems.get(j).getMusicName());
            s=j;

        }
        sortItems.get(s).setPlayIndex(temp);
        sortItems.get(s).setMusicName(sortItems.get(temp).getMusicName());
        myApplication.setHeapSort(sortItems);
    }
    public void swap(List<SortItem> sortItems1,int start,int end){
        SortItem sortItem=new SortItem();
        sortItem.setMusicName(sortItems1.get(start).getMusicName());
        sortItem.setPlayIndex(sortItems1.get(start).getPlayIndex());
        sortItems1.get(start).setMusicName(sortItems1.get(end).getMusicName());
        sortItems1.get(start).setPlayIndex(sortItems1.get(end).getPlayIndex());
        sortItems1.get(end).setMusicName(sortItem.getMusicName());
        sortItems1.get(end).setPlayIndex(sortItem.getPlayIndex());
        myApplication.setHeapSort(sortItems1);
    }
}
