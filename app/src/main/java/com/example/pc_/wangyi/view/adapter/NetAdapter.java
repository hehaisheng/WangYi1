package com.example.pc_.wangyi.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.NetMusic;

import java.util.List;

/**
 * Created by pc- on 2017/5/26.
 */
public class NetAdapter extends BaseQuickAdapter<NetMusic> {
    public NetAdapter(int layoutResId, List<NetMusic> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, NetMusic netMusic) {
        baseViewHolder.setText(R.id.net_item_name,netMusic.getMusicName())
                .setText(R.id.net_item_art,netMusic.getMusicArt());

    }




}
