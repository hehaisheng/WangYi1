package com.example.pc_.wangyi.view.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.DouBanNews;

import java.util.List;

/**
 * Created by pc- on 2017/5/19.
 */
public class DouBanAdapter extends BaseQuickAdapter<DouBanNews.posts>{

    public Context context;
    public DouBanAdapter(Context context,int layoutResId,List<DouBanNews.posts> data){
        this(layoutResId,data);
        this.context=context;
    }

    public DouBanAdapter(int layoutResId, List<DouBanNews.posts> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, DouBanNews.posts posts) {
        if (posts.getThumbs().size() == 0) {
          baseViewHolder.setImageResource(R.id.douban_item_image,R.drawable.earth);
        } else {
            String imageUrl = posts.getThumbs().get(0).getMedium().getUrl();
            Glide.with(context).load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT).into((ImageView) baseViewHolder.getView(R.id.douban_item_image));
        }
        baseViewHolder
                .setText(R.id.douban_item_text,posts.getTitle());


    }
}
