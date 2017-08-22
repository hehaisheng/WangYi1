package com.example.pc_.wangyi.view.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.ZhiHuNews;
import com.example.pc_.wangyi.utils.MyLruCache;

import java.util.List;

/**
 * Created by pc- on 2017/5/17.
 */
public class ZhiHuAdapter extends BaseQuickAdapter<ZhiHuNews.Question> {


    public Context context;
    public MyLruCache myLruCache;
    public  ZhiHuAdapter(Context context,int layoutResId,List<ZhiHuNews.Question> data){
        //this是ZhiHuAdapter的实例
        this(layoutResId,data);
        this.context=context;
        myLruCache=new MyLruCache();
    }
    public ZhiHuAdapter(int layoutResId, List<ZhiHuNews.Question> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final ZhiHuNews.Question question) {
        //获取在application数据的界面比从网络获取的界面卡
            int index= question.getImages().toString().length();
            String imageUrl =question.getImages().toString().substring(1, index - 1);
            Glide.with(context).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.RESULT).into((ImageView)baseViewHolder.getView(R.id.zhihu_item_image));
            baseViewHolder
                .setText(R.id.zhihu_item_text,question.getTitle());






    }



}
