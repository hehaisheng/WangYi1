package com.example.pc_.wangyi.view.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.utils.AutoSplit;

import java.util.List;

/**
 * Created by pc- on 2017/7/3.
 */
public class FriendAdapter extends BaseQuickAdapter<String> {

    public Context context;
    public AutoSplit autoSplit;
    public FriendAdapter(Context context,int layoutResId, List<String> data){
        this(layoutResId,data);
        this.context=context;

    }
    public FriendAdapter(int layoutResId, List<String> data) {
        super(layoutResId, data);
        autoSplit=new AutoSplit();
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String s) {
        String[] strings=s.split("s-p");
        String recommandStrs=autoSplit.autoSplit(strings[1]);
        baseViewHolder.setText(R.id.friend_user_name,strings[0])
        .setText(R.id.recommand_text_list,recommandStrs);



    }
}
