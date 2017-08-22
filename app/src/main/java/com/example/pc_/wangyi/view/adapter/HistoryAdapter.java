package com.example.pc_.wangyi.view.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.HistoryItem;

import java.util.List;

/**
 * Created by pc- on 2017/5/19.
 */
public class HistoryAdapter  extends BaseQuickAdapter<HistoryItem> {


    public Context context;
    public HistoryAdapter(Context context,int layoutResId,List<HistoryItem> data){
        this(layoutResId,data);
        this.context=context;
    }
    public HistoryAdapter(int layoutResId, List<HistoryItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final HistoryItem historyItem) {

        baseViewHolder
                .setText(R.id.history_item_text,historyItem.getTitle());
        if(historyItem.getImageUrl().equals("nourl")) {
            baseViewHolder.setImageResource(R.id.history_item_image,R.drawable.earth);
        }
        else {

            Glide.with(context)
                    .load(historyItem.getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into((ImageView) baseViewHolder.getView(R.id.history_item_image));
        }
       





    }
}
