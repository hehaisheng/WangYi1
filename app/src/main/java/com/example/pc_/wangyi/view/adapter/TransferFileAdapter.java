package com.example.pc_.wangyi.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.FileInfo;

import java.util.List;

/**
 * Created by pc- on 2017/5/24.
 */
public class TransferFileAdapter extends BaseQuickAdapter<FileInfo> {


    public TransferFileAdapter(int layoutResId, List<FileInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, FileInfo fileInfo) {
        baseViewHolder
                .setText(R.id.file_item_name,fileInfo.getName())
                .setText(R.id.file_item_art,fileInfo.getMusicArt())
                .setText(R.id.file_item_done,fileInfo.getHasDone()+"");

    }
}
