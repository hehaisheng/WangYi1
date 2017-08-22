package com.example.pc_.wangyi.view.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.TestModel;

import java.util.List;

/**
 * Created by pc- on 2017/5/21.
 */
public class TestAdapter extends BaseQuickAdapter<TestModel> {


    public TestAdapter(int layoutResId, List<TestModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, TestModel testModel) {
        baseViewHolder.setText(R.id.test_text2,testModel.getName());

    }
}