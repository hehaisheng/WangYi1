package com.example.pc_.wangyi.view.adapter;

import android.net.wifi.ScanResult;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.pc_.wangyi.R;

import java.util.List;

/**
 * Created by pc- on 2017/5/24.
 */
public class FindApAdapter extends BaseQuickAdapter<ScanResult> {

    public FindApAdapter(int layoutResId, List<ScanResult> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ScanResult scanResult) {
        baseViewHolder
                .setText(R.id.find_item_name,scanResult.SSID)
                .setText(R.id.find_item_art,scanResult.BSSID);

    }
}
