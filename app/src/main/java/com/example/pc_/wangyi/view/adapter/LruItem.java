package com.example.pc_.wangyi.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.LruM;

import java.util.List;

/**
 * Created by pc- on 2017/7/3.
 */
public class LruItem extends BaseQuickAdapter<Object> {


    public LruItem(int layoutResId, List<Object> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder,Object string) {
        baseViewHolder.setText(R.id.lru_item, ((LruM) string).getValue());
    }
}
