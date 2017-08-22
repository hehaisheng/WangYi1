package com.example.pc_.wangyi.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.database.FriendCommentModel;
import com.example.pc_.wangyi.model.FriendModel;
import com.example.pc_.wangyi.retrofit.ComSchedulers;
import com.example.pc_.wangyi.retrofit.RxBus;
import com.example.pc_.wangyi.utils.LiteOrmManager;
import com.example.pc_.wangyi.view.InputLayout;
import com.example.pc_.wangyi.view.MyApplication;
import com.example.pc_.wangyi.view.adapter.FriendNewAdapter;
import com.example.pc_.wangyi.view.service.FriendCommentService;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.BindView;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by pc- on 2017/7/3.
 */
public class FriendActivity extends BaseActivity {




    @BindView(R.id.friend_activity)
    ListView friendActivity;
    @BindView(R.id.friend_talk_edit)
    EditText talkEdit;
    @BindView(R.id.friend_talk_send)
    TextView talkSend;
    @BindView(R.id.friend_send_linear)
    PercentLinearLayout friendSendLinear;
    @BindView(R.id.friend_header)
    InputLayout inputLayout;

    public FriendNewAdapter friendAdapter;
    //    public List<String> stringList = new ArrayList<>();
    public String splitString = "好音好";
    public List<FriendModel> friendModels = new CopyOnWriteArrayList<>();
    public List<FriendModel>  tempFriendModels=new CopyOnWriteArrayList<>();

    public List<FriendModel> reversalFriendModels=new CopyOnWriteArrayList<>();




    final String Broadcast_Ip1 = "230.0.0.1";
    final int Broadcast_Port1 = 30001;
    DatagramPacket outPacket1;
    MulticastSocket multicastSocket1;
    public RxBus rxBus1;
    InetAddress inetAddress1;
    public Subscription subscription;
    public Intent intent;

    //根据点击的位置来更改位置的内容
    public int currentPosition;
    public String sendString;
    //发送的用户名文本
    public String sendName;
    @BindView(R.id.friend_createMessage)
    ImageView friendCreateMessage;

    public  List<FriendCommentModel>  friendCommentModels=new ArrayList<>();
    public LiteOrmManager liteOrmManager;
    public  List<FriendCommentModel>  reversalFriendCommentModels=new ArrayList<>();
    public SharedPreferences sharedPreferences;
    public  String account;
    public  int enterCount=0;
    public MyApplication myApplication;



//    public <T> List<T> getQueryAll(Class<T> cla) {
//        return liteOrm.query(cla);
//    }
    @Override
    public int getLayout() {
        return R.layout.friend_activity;
    }

    @Override
    public void initView() {



        myApplication=MyApplication.getNewInstance();
        sharedPreferences = getSharedPreferences("Login",MODE_PRIVATE);
        account=sharedPreferences.getString("account","0");
        intent = new Intent(this, FriendCommentService.class);
        startService(intent);
        rxBus1 = RxBus.newInstance();
        liteOrmManager=LiteOrmManager.newInstance(this);
        //friendCommentModels=liteOrmManager.getQueryAll(FriendCommentModel.class);
        //正常顺序
       reversalFriendCommentModels=liteOrmManager.getQueryAll(FriendCommentModel.class);

        for(int i=0;i<reversalFriendCommentModels.size();i++){
            FriendCommentModel friendCommentModel= reversalFriendCommentModels.get(i);
            FriendModel friendModel=new FriendModel();
            friendModel.setUserName(friendCommentModel.getUserName());
            friendModel.setUserMessage(friendCommentModel.getMessageContent());
            //NoName没有数据时
            friendModel.setZanUserName(friendCommentModel.getZanUserName());
            friendModel.setCommentText(friendCommentModel.getCommentText());
            friendModel.setCreateTime(friendCommentModel.getCreateTime());
            reversalFriendModels.add(friendModel);
           // friendModels.add(friendModel);1
        }
       // MyApplication.getNewInstance().setFriendModels(friendModels);
       int length=reversalFriendModels.size();
        for(int i=length-1;i>=0;i--){
            friendModels.add(reversalFriendModels.get(i));
        }
        int length1=reversalFriendCommentModels.size();
        for(int i=length1-1;i>=0;i--){
            friendCommentModels.add(reversalFriendCommentModels.get(i));
        }

        friendAdapter = new FriendNewAdapter(this);
      //  friendAdapter.setFriendModels(friendModels);1
        friendAdapter.setFriendModels(friendModels);
        friendActivity.setAdapter(friendAdapter);
        //翻转内存


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    multicastSocket1 = new MulticastSocket(Broadcast_Port1);
                    inetAddress1 = InetAddress.getByName(Broadcast_Ip1);
                    multicastSocket1.joinGroup(inetAddress1);
                    multicastSocket1.setLoopbackMode(false);
                    outPacket1 = new DatagramPacket(new byte[0], 0, inetAddress1, Broadcast_Port1);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


    @Override
    protected void onRestart() {
        super.onRestart();

        reversalFriendCommentModels=liteOrmManager.getQueryAll(FriendCommentModel.class);
        friendModels.clear();
        reversalFriendModels.clear();
        friendAdapter.clear();
        friendCommentModels.clear();


        for(int i=0;i<reversalFriendCommentModels.size();i++){
            FriendCommentModel friendCommentModel=reversalFriendCommentModels.get(i);
            FriendModel friendModel=new FriendModel();
            friendModel.setUserName(friendCommentModel.getUserName());
            friendModel.setUserMessage(friendCommentModel.getMessageContent());
            friendModel.setZanUserName(friendCommentModel.getZanUserName());
            friendModel.setCommentText(friendCommentModel.getCommentText());
            friendModel.setCreateTime(friendCommentModel.getCreateTime());
           // friendModels.add(friendModel);
            reversalFriendModels.add(friendModel);
        }
        int length=reversalFriendModels.size();
        for(int i=length-1;i>=0;i--){
            friendModels.add(reversalFriendModels.get(i));
        }
        int length1=reversalFriendCommentModels.size();
        for(int i=length1-1;i>=0;i--){
            friendCommentModels.add(reversalFriendCommentModels.get(i));
        }


        friendAdapter.setFriendModels(friendModels);
        friendActivity.setAdapter(friendAdapter);

    }

    @Override
    public void initData() {

        inputLayout.setOnkeyboarddStateListener(new InputLayout.onKeyboardsChangeListener() {
            @Override
            public void onKeyBoardStateChange(int state) {
                switch (state) {
                    case InputLayout.KEYBOARD_STATE_HIDE:
                        if (friendSendLinear.getVisibility() == View.VISIBLE) {
                            friendSendLinear.setVisibility(View.GONE);
                        }
                        break;
                }
            }

        });

        friendAdapter.addISendComment(new FriendNewAdapter.ISendComment() {
            @Override
            public void send(final String message) {
             //   "赞0好音好"+position1+"好音好"+friendModels.get(position).getUserName()2
                if (message.contains("赞")) {

                    //谁赞谁，显示在createTime这个对象
                    //赞  点赞的用户  被点赞的用户
                   // "赞0好音好"+position1
                   // "赞好音好"+position 评论谁
                    String[] zanStr = message.split(splitString);
                    currentPosition = Integer.valueOf(zanStr[1]);
                    String createTime=friendModels.get(currentPosition).getCreateTime();
                    final String sendStr="赞"+splitString+account+splitString+createTime;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            byte[] sendMessage = sendStr.getBytes();
                            outPacket1.setData(sendMessage);
                            try {
                                multicastSocket1.send(outPacket1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                } else if (message.contains("评论")) {
                    String[] zanStr = message.split(splitString);
                    currentPosition = Integer.valueOf(zanStr[1]);
                    if (friendSendLinear.getVisibility() == View.GONE) {
                        friendSendLinear.setVisibility(View.VISIBLE);
                    }

                }


            }
        });
        subscription = rxBus1.tObservable(String.class)
                .compose(ComSchedulers.<String>applyIoSchedulers())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String string) {
                        FriendModel friendModel;
                        String[] message = string.split(splitString);

                        switch (message[0]) {
                            case "赞":
                                boolean isFind1=false;
                                for(int i=0;i<friendModels.size()&&!isFind1;i++){
                                    FriendModel friendModel1=friendModels.get(i);
                                    if(message[2].equals(friendModel1.getCreateTime())){
                                        if(!friendModel1.getZanUserName().contains(message[1])){
                                            friendModel1.addZanUserName(message[1]+",");
                                            FriendCommentModel friendCommentModel=friendCommentModels.get(i);
                                            friendCommentModel.addZanUserName(message[1]+",");
                                            liteOrmManager.update(friendCommentModel) ;
                                            friendModels.remove(i);
                                            friendModels.add(i, friendModel1);
                                            friendAdapter.notifyDataSetChanged();
                                        }
                                        isFind1=true;

                                    }
                                }

                                break;
                            case "评论":
                               // "评论好音好"+position+"好音好"+createTime

                               // "评论"+splitString+userName+splitString+createTime+splitString+talkEdit.getText().toString();
                                boolean isFind=false;
                                for(int i=0;i<friendModels.size()&&!isFind;i++){
                                    FriendModel friendModel1=friendModels.get(i);
                                    if(message[2].equals(friendModel1.getCreateTime())){
                                         friendModel1.addCommentText(splitString+message[1]+":"+message[3]);
                                         FriendCommentModel friendCommentModel=friendCommentModels.get(i);
                                         friendCommentModel.addCommentText(splitString+message[1]+":"+message[3]);
                                         liteOrmManager.update(friendCommentModel) ;
                                         friendModels.remove(i);
                                         friendModels.add(i, friendModel1);
                                         friendAdapter.notifyDataSetChanged();
                                        isFind=true;
                                    }
                                }

                                break;
                            case "NewCreate":
                               // "NewCreate"+"好音好"+account+"好音好"+time+"好音好"+message;
                                friendModel=new FriendModel();
                                friendModel.setUserName(message[1]);
                                friendModel.setCreateTime(message[2]);
                                friendModel.setUserMessage(message[3]);
                                friendModel.setZanUserName("赞:");
                                friendModel.setCommentText("评论:");
                                tempFriendModels.add(friendModel);
                                if(friendModels.size()!=0){
                                    for(FriendModel friendModel1:friendModels){
                                        tempFriendModels.add(friendModel1);
                                    }
                                }
                                friendModels=tempFriendModels;
                                tempFriendModels.clear();
                                friendAdapter.setFriendModels(friendModels);
                                friendAdapter.notifyDataSetChanged();
                                break;
                        }

                    }
                });
    }

    @Override
    public void initEvent() {
        talkSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (talkEdit.getText().length() != 0) {
                    //用户名和编辑的内容

                    //谁评论谁就可以
                    String userName = friendModels.get(currentPosition).getUserName();
                    String createTime=friendModels.get(currentPosition).getCreateTime();
                    sendString ="评论"+splitString+account+splitString+createTime+splitString+talkEdit.getText().toString();
                   // sendString = userName + splitString + talkEdit.getText().toString();
                    talkEdit.setText("");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            byte[] sendMessage = sendString.getBytes();
                            outPacket1.setData(sendMessage);
                            try {
                                multicastSocket1.send(outPacket1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();


                }
            }
        });

        friendCreateMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FriendActivity.this,CreateFriendActivity.class);
                startActivity(intent);
            }
        });

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
            subscription = null;
        }
        if (multicastSocket1 != null) {
            multicastSocket1.close();
            multicastSocket1 = null;
        }

    }


}
