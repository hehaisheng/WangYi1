package com.example.pc_.wangyi.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.TalkItem;

import java.util.List;

/**
 * Created by pc- on 2017/5/25.
 */
public class TalkAdapter extends BaseQuickAdapter<TalkItem> {
    public TalkAdapter(int layoutResId, List<TalkItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, TalkItem talkItem) {

        String StrTemp=talkItem.getContent();
        String[]  strings=StrTemp.split("分割分");

        baseViewHolder.setText(R.id.talk_item_user_name,strings[0]+":");
        baseViewHolder.setText(R.id.talk_item_text,strings[1]);
    }
}
