package com.example.pc_.wangyi.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.utils.ChineseFilter;

import butterknife.BindView;

/**
 * Created by pc- on 2017/7/5.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{


    @BindView(R.id.login_accountEdit)
    EditText loginAccountEdit;
    @BindView(R.id.login_passEdit)
    EditText loginPassEdit;
    @BindView(R.id.login_clickText)
    TextView loginClickText;
    @BindView(R.id.register_clickText)
    TextView registerClickText;
    @BindView(R.id.find_pass)
    TextView findPass;

    public String password;
    public String account;

    public  SharedPreferences sharedPreferences;
    public ChineseFilter chineseFilter;


    @Override
    public int getLayout() {
        return R.layout.login_activity;
    }

    @Override
    public void initView() {
        sharedPreferences = getSharedPreferences("Login",MODE_PRIVATE);
        //获取保存的账号密码
        password=sharedPreferences.getString("password","0");
        account=sharedPreferences.getString("account","0");
        chineseFilter=new ChineseFilter();

    }

    @Override
    public void initData() {
        loginAccountEdit.setFilters(new InputFilter[]{chineseFilter.getInputFilter()});
    }

    @Override
    public void initEvent() {

        loginClickText.setOnClickListener(this);
        registerClickText.setOnClickListener(this);
        findPass.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_clickText:
                String passwordString=loginPassEdit.getText().toString();
                String accountString=loginAccountEdit.getText().toString();
                if(accountString.length()!=4||passwordString.length()!=7){
                       show("账号密码个数不正确");
                }else{
                    if(password.equals("0")||account.equals("0")){
                        //没有登录过，直接注册且登录
                        saveMessage(passwordString,accountString);
                        login();
                    }else {
                        if(!password.equals(passwordString)||!account.equals(accountString)){
                              show("请确认密码账号没有错误");
                        }else{
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putBoolean("hasLogin",true);
                            editor.apply();
                            login();
                        }
                    }
                }
                break;
            case R.id.register_clickText:
                String passwordString1=loginPassEdit.getText().toString();
                String accountString1=loginAccountEdit.getText().toString();
                if(accountString1.length()!=4||passwordString1.length()!=7) {
                    show("账号密码个数不正确");
                }else{
                    if(password.equals("0")||account.equals("0")){
                        saveMessage(passwordString1,accountString1);
                        login();
                    }else {
                        show("已注册过，请登录");
                    }
                }
                break;
            case R.id.find_pass:

                //保存新的数据后，返回这里
                Intent intent=new Intent(LoginActivity.this,FindPasswordActivity.class);
                startActivity(intent);

                break;
        }

    }

    public void show(String showString){
        Toast.makeText(LoginActivity.this,showString,Toast.LENGTH_SHORT).show();
    }
    public  void saveMessage(String passwordStr,String accountStr){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("password",passwordStr);
        editor.putString("account",accountStr);
        editor.putBoolean("hasLogin",true);
        editor.apply();
    }
    public  void login(){

        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }




}
