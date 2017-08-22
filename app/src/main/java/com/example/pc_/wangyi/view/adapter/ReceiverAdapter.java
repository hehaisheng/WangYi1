package com.example.pc_.wangyi.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.FileInfo;

import java.util.List;

/**
 * Created by pc- on 2017/5/23.
 */
public class ReceiverAdapter extends BaseQuickAdapter<FileInfo> {
    public ReceiverAdapter(int layoutResId, List<FileInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder,FileInfo musicDataItem) {

        baseViewHolder.setText(R.id.receiver_item_name,musicDataItem.getName())
                .setText(R.id.receiver_item_art,musicDataItem.getMusicArt());

    }
}
