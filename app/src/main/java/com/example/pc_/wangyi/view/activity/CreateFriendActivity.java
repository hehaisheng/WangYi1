package com.example.pc_.wangyi.view.activity;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.retrofit.RxBus;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import butterknife.BindView;

/**
 * Created by pc- on 2017/7/9.
 */
public class CreateFriendActivity extends BaseActivity {



    @BindView(R.id.friend_send_click)
    TextView friendSendClick;
    @BindView(R.id.friend_create_edit)
    EditText friendCreateEdit;


    final String Broadcast_Ip1 = "230.0.0.1";
    final int Broadcast_Port1 = 30001;
    DatagramPacket outPacket1;
    MulticastSocket multicastSocket1;
    public RxBus rxBus1;
    InetAddress inetAddress1;
    public SharedPreferences sharedPreferences;

    public  String account;

    @Override
    public int getLayout() {
        return R.layout.friend_create;
    }

    @Override
    public void initView() {

        sharedPreferences = getSharedPreferences("Login",MODE_PRIVATE);


        account=sharedPreferences.getString("account","0");
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
    public void initData() {

    }

    @Override
    public void initEvent() {
        friendSendClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message=friendCreateEdit.getText().toString();
                if(message.length()!=0){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String time=String.valueOf(System.currentTimeMillis());
                            String sendMessageStr="NewCreate"+"好音好"+account+"好音好"+time+"好音好"+message;
                            byte[] sendMessage = sendMessageStr.getBytes();
                            outPacket1.setData(sendMessage);
                            try {
                                multicastSocket1.send(outPacket1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    friendCreateEdit.setText("");

                }else{
                    Toast.makeText(CreateFriendActivity.this,"内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(outPacket1!=null){
            outPacket1=null;
        }
        if(multicastSocket1!=null){
            multicastSocket1.close();
            multicastSocket1=null;
        }
        if(sharedPreferences!=null){
            sharedPreferences=null;
        }
    }
}
