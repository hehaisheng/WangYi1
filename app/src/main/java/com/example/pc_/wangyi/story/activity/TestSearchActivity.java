package com.example.pc_.wangyi.story.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.story.adapter.SearchDataAdapter;
import com.example.pc_.wangyi.story.view.CustomSearchLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pc- on 2017/8/12.
 */
public class TestSearchActivity extends Activity implements CustomSearchLayout.ISearchSuccess{

    @BindView(R.id.custom_search_layout)
    CustomSearchLayout customSearchLayout;

    public SearchDataAdapter  searchDataAdapter;
    public RecyclerView   searchDataView;
    public RecyclerView   searchResultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_search_activity);
        ButterKnife.bind(this);
        customSearchLayout.setISearchSuccess(this);
        searchDataView=(RecyclerView) customSearchLayout.findViewById(R.id.search_data_linear);
        searchDataView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void searchSuccess(List<String> searchDataList) {

        searchDataAdapter=new SearchDataAdapter(R.layout.search_data_adapter,searchDataList);
        searchDataView.setAdapter(searchDataAdapter);


    }

    @Override
    public void searchFail(int failType) {

        if(failType==0){
            //网络错误
        }else if(failType==1){
            //无该数据
        }


    }

    @Override
    public void searchVisibility(int i) {
       if(i==0){
           if(searchDataView.getVisibility()!=View.GONE){
               searchDataView.setVisibility(View.GONE);
           }

       }else if(i==1){
           if(searchDataView.getVisibility()!=View.VISIBLE){
               searchDataView.setVisibility(View.VISIBLE);
           }

       }
    }
}
