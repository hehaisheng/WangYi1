package com.example.pc_.wangyi.view.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.utils.HHImageLoader;

import java.util.List;

/**
 * Created by pc- on 2017/7/6.
 */
public class ImageAdapter  extends BaseQuickAdapter<String> {

    public Context context;
    public ImageAdapter(Context context,int layoutResId,List<String> data){
        this(layoutResId,data);
        this.context=context;
    }
    public ImageAdapter(int layoutResId, List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String s) {
        HHImageLoader.newInstance(context).load(s).size(200,200).into((ImageView) baseViewHolder.getView(R.id.test_image));
    }
}
