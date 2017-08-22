package com.example.pc_.wangyi.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.SortItem;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by pc- on 2017/6/10.
 */
public class HeapAdapter extends BaseAdapter {

    public Context context;
    public List<SortItem>  sortItemList=new ArrayList<>();
    public void setSortItemList(List<SortItem> sortItemList){
        this.sortItemList=sortItemList;


    }
    public HeapAdapter(Context context){

        this.context=context;
    }
    @Override
    public int getCount() {
        return sortItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return   sortItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView=LayoutInflater.from(context).inflate(R.layout.heap_item,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.name=(TextView) convertView.findViewById(R.id.heap_name);
            viewHolder.times=(TextView) convertView.findViewById(R.id.heap_times);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder) convertView.getTag();

        }
        viewHolder.name.setText(sortItemList.get(position).getMusicName());
        viewHolder.times.setText(sortItemList.get(position).getPlayIndex()+"");
        return convertView;
    }
    class ViewHolder{
        TextView name,times;
    }
}
