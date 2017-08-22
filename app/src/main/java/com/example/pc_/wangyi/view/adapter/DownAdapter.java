package com.example.pc_.wangyi.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.DownItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc- on 2017/5/20.
 */
public class DownAdapter extends BaseAdapter {

    public List<DownItem> downItems=new ArrayList<>();
    public void setDownItems(List<DownItem> downItems)
    {
        this.downItems=downItems;
        notifyDataSetChanged();
    }
    public Context context;
    public DownAdapter(Context context)
    {
        this.context=context;
    }
    @Override
    public int getCount() {
        return downItems.size();
    }

    @Override
    public Object getItem(int position) {
        return downItems.get(position);
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
            convertView= LayoutInflater.from(context).inflate(R.layout.down_item,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.textView=(TextView) convertView.findViewById(R.id.down_item_text);
            viewHolder.imageView=(ImageView) convertView.findViewById(R.id.down_item_image);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(downItems.get(position).getTitle());
        if(downItems.get(position).getImageUrl().equals("NoUrl"))
        {
            viewHolder.imageView.setImageResource(R.drawable.earth);
        }
        else
        {
            Glide.with(context).load(downItems.get(position).getImageUrl()).centerCrop().into(viewHolder.imageView);
        }
        return convertView;
    }

    class ViewHolder
    {
        TextView textView;
        ImageView imageView;
    }
}
