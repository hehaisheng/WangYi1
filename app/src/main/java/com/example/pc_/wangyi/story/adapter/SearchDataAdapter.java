package com.example.pc_.wangyi.story.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.pc_.wangyi.R;

import java.util.List;

/**
 * Created by pc- on 2017/8/12.
 */
public class SearchDataAdapter  extends BaseQuickAdapter<String>{

    public SearchDataAdapter(int layoutResId, List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String s) {

        baseViewHolder.setText(R.id.search_item_text,s);

    }
}
