package com.example.pc_.wangyi.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.MusicDataItem;

import java.util.List;

/**
 * Created by pc- on 2017/5/23.
 */
public class TransferAdapter extends BaseQuickAdapter<MusicDataItem> {

    public TransferAdapter(int layoutResId, List<MusicDataItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MusicDataItem musicDataItem) {

        baseViewHolder
                .setText(R.id.fengxiang_item_name,musicDataItem.getMusicName())
                .setText(R.id.fengxiang_item_art,musicDataItem.getMusicArt());
    }
}
