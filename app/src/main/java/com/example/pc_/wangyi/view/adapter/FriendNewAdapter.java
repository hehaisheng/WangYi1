package com.example.pc_.wangyi.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.FriendModel;
import com.example.pc_.wangyi.utils.AutoSplit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by pc- on 2017/7/4.
 */
public class FriendNewAdapter extends BaseAdapter {



    public final static String[] imageUrls = new String[] {
            "http://img.my.csdn.net/uploads/201309/01/1378037235_3453.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037235_9280.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037234_3539.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037234_6318.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037194_2965.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037193_1687.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037193_1286.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037192_8379.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037178_9374.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037177_1254.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037177_6203.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037152_6352.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037151_9565.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037151_7904.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037148_7104.jpg"


    };
    public Random random ;

    public List<FriendModel>  friendModels=new ArrayList<>();
    public void setFriendModels(List<FriendModel> friendModels){

        this.friendModels=friendModels;
    }
    public void clear(){
        this.friendModels.clear();
    }

    public Context context;
    public AutoSplit autoSplit;
    public FriendNewAdapter(Context context){
        this.context=context;
        autoSplit=new AutoSplit();
        random=new Random();
//        int i=Math.abs(random.nextInt())%10;
    }

    @Override
    public int getCount() {
        return friendModels.size();
    }

    @Override
    public Object getItem(int position) {
        return friendModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
         final ViewHolder viewHolder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.friend_new_recommand,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.userNameView=(TextView) convertView.findViewById(R.id.friend_user_name);
            viewHolder.commentTextView=(TextView)  convertView.findViewById(R.id.recommand_text_list);
            viewHolder.commentClickView=(TextView)  convertView.findViewById(R.id.friend_pinglun);
            viewHolder.zanClickView=(TextView)  convertView.findViewById(R.id.friend_dianzan);
            viewHolder.zanTextView=(TextView) convertView.findViewById(R.id.friend_zan_name);
            viewHolder.moreImage=(ImageView)  convertView.findViewById(R.id.friend_more);
            viewHolder.userMessageView=(TextView)  convertView.findViewById(R.id.friend_user_xinqing);
            viewHolder.moreLinear=(LinearLayout) convertView.findViewById(R.id.friend_more_linear);
            viewHolder.headImage=(ImageView) convertView.findViewById(R.id.friend_touxiang);
            viewHolder.xinQingIMage=(ImageView) convertView.findViewById(R.id.friend_xinqing_tupian);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();

        }
        final FriendModel friendModel=friendModels.get(position);
        viewHolder.userNameView.setText(friendModel.getUserName());
        if(friendModel.getZanUserName()!=null){

                viewHolder.zanTextView.setVisibility(View.VISIBLE);
                viewHolder.zanTextView.setText(friendModel.getZanUserName());

         }else {
            viewHolder.zanTextView.setVisibility(View.GONE);
        }
        if(friendModel.getUserMessage()!=null){
            viewHolder.userMessageView.setVisibility(View.VISIBLE);
            viewHolder.userMessageView.setText(friendModel.getUserMessage());
        }else{
            viewHolder.userMessageView.setVisibility(View.GONE);
        }

        if(friendModel.getCommentText()!=null){
            viewHolder.commentTextView.setVisibility(View.VISIBLE);
            //覆盖原来的数据，数据才不会重复
            viewHolder.commentTextView.setText(autoSplit.autoSplit(friendModel.getCommentText()));
        }else {
            viewHolder.commentTextView.setVisibility(View.GONE);
        }


        if((viewHolder.moreLinear.getTag())!=null){
            if(((int)(viewHolder.moreLinear.getTag()))!=position){
                    if(viewHolder.moreLinear.getVisibility()==View.VISIBLE){
                        viewHolder.moreLinear.setVisibility(View.GONE);
                    }
            }
        }
        //viewHolder.commentTextView.setText(autoSplit.autoSplit(friendModels.get(position).getCommentText()));
        viewHolder.moreImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(viewHolder.moreLinear.getVisibility()==View.GONE){
                        viewHolder.moreLinear.setVisibility(View.VISIBLE);
                    }else if(viewHolder.moreLinear.getVisibility()==View.VISIBLE){
                        viewHolder.moreLinear.setVisibility(View.GONE);
                    }
                viewHolder.moreLinear.setTag(position);


            }
        });
        int i=Math.abs(random.nextInt())%10;
        Glide.with(context).load(imageUrls[i])
                .diskCacheStrategy(DiskCacheStrategy.RESULT).into(viewHolder.headImage);
        Glide.with(context).load(imageUrls[i+2])
                .diskCacheStrategy(DiskCacheStrategy.RESULT).into(viewHolder.xinQingIMage);
        viewHolder.commentClickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //createTime 代表评论谁
                //"NewCreate"+"好音好"+account+"好音好"+time+"好音好"+message;
                String createTime=friendModel.getCreateTime();
                iSendComment.send("评论好音好"+position+"好音好"+createTime);
                if(viewHolder.moreLinear.getVisibility()==View.VISIBLE){
                    viewHolder.moreLinear.setVisibility(View.GONE);
                }
            }
        });
        viewHolder.zanClickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iSendComment.send("赞好音好"+position);
                viewHolder.zanClickView.setTag(position);
                if(viewHolder.moreLinear.getVisibility()==View.VISIBLE){
                    viewHolder.moreLinear.setVisibility(View.GONE);
                }

            }
        });

        return convertView;
    }



    public class ViewHolder{
        TextView userNameView,commentTextView,commentClickView,zanClickView,zanTextView;
        TextView userMessageView;
        ImageView  moreImage,headImage,xinQingIMage;
        LinearLayout moreLinear;

    }
    public  ISendComment iSendComment;
    public interface  ISendComment{
        void send(String message);
    }
    public void addISendComment(ISendComment iSendComment){
        this.iSendComment=iSendComment;
    }

}
