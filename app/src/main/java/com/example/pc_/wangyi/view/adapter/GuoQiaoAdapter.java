package com.example.pc_.wangyi.view.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.GuoQiaoItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc- on 2017/5/14.
 */
public class GuoQiaoAdapter extends BaseAdapter {

    public List<GuoQiaoItem.result> results=new ArrayList<>();


    public Context activity;
    public void setItem(List<GuoQiaoItem.result> guoQiaoItems)
    {
        this.results=guoQiaoItems;
    }
    public GuoQiaoAdapter(Context context)
    {
        this.activity=context;
    }
    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public Object getItem(int position) {
        return results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder viewHolder;
        if(convertView==null)
        {
            convertView= LayoutInflater.from(activity).inflate(R.layout.guoqiao_card,parent,false);
            viewHolder= new ViewHolder();
            viewHolder.textView=(TextView) convertView.findViewById(R.id.guoqiao_item_text);
            viewHolder.imageView=(ImageView) convertView.findViewById(R.id.guoqiao_item_image);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(results.get(position).getTitle());
        Glide.with(activity).load(results.get(position).getHeadline_img_tb())
                .diskCacheStrategy(DiskCacheStrategy.RESULT).into(viewHolder.imageView);
        return convertView;
    }



    class ViewHolder
    {
        TextView textView;
        ImageView imageView;
    }

}
