package com.example.pc_.wangyi.view.activity;


import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.SortItem;
import com.example.pc_.wangyi.utils.QuickSort;
import com.example.pc_.wangyi.view.MyApplication;
import com.example.pc_.wangyi.view.adapter.HeapAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by pc- on 2017/6/10.
 */
public class HeapActivity extends BaseActivity {



    public ListView listView;
    public HeapAdapter heapAdapter;
    public QuickSort  myQuickSort;
    public MyApplication myApplication;

    public TextView textView;

    public List<SortItem> sortItemList = new ArrayList<>();


    //我 12，你 11，它，10，中，9，好，值8，部7，被6，长5，是4，想3，的2，人1，大0
    public String[] strings=new String[]{"原因","踢人","调用","原因","踢人","调用","原因","踢人","调用","原因","踢人","调用"};
    public int[] indexs=new int[]{11,3,4,11,3,4,11,3,4,11,3,4};

    @Override
    public int getLayout() {
        return R.layout.heap_activity;
    }


    @Override
    public void initView() {
        textView=(TextView) findViewById(R.id.heap_name_diaj);
        listView=(ListView) findViewById(R.id.heap_list);
        myApplication=MyApplication.getNewInstance();
        myQuickSort=QuickSort.newInstance();
        heapAdapter=new HeapAdapter(this);
        for(int i=0;i<strings.length;i++){
            SortItem sortItem=new SortItem();
            sortItem.setMusicName(strings[i]);
            sortItem.setPlayIndex(indexs[i]);
            sortItemList.add(sortItem);

        }
        heapAdapter.setSortItemList(sortItemList);
        listView.setAdapter(heapAdapter);

    }

    @Override
    public void initData() {




    }

    @Override
    public void initEvent() {

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("点击","Activity有反应");
                Log.d("点击","Activity有反应");
                Log.d("点击","Activity有反应");

                Log.d("点击","Activity有反应"+sortItemList.get(0).getPlayIndex());
                Log.d("点击","Activity有反应"+sortItemList.get(0).getPlayIndex());
                Log.d("点击","Activity有反应"+sortItemList.get(0).getPlayIndex());
               // myQuickSort.quickSort(sortItemList,0,sortItemList.size()-1);
                heapAdapter.setSortItemList(myApplication.getQucikItems());
                Log.d("点击","Application有反应"+myApplication.getQucikItems().get(0).getPlayIndex());
                Log.d("点击","Application有反应"+myApplication.getQucikItems().get(0).getPlayIndex());
                Log.d("点击","Application有反应"+myApplication.getQucikItems().get(0).getPlayIndex());
                listView.setAdapter(heapAdapter);
                heapAdapter.notifyDataSetChanged();


            }
        });
    }


}
