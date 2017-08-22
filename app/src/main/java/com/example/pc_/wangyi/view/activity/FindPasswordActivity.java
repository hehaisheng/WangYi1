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
public class FindPasswordActivity extends BaseActivity {


    @BindView(R.id.find_accountEdit)
    EditText loginAccountEdit;
    @BindView(R.id.find_passEdit)
    EditText loginPassEdit;
    @BindView(R.id.find_clickText)
    TextView registerClickText;

    public SharedPreferences sharedPreferences;
    public ChineseFilter chineseFilter;


    @Override
    public int getLayout() {
        return R.layout.findpass_activity;
    }

    @Override
    public void initView() {

        sharedPreferences = getSharedPreferences("Login",MODE_PRIVATE);
        chineseFilter=new ChineseFilter();

    }

    @Override
    public void initData() {
        loginAccountEdit.setFilters(new InputFilter[]{chineseFilter.getInputFilter()});
    }

    @Override
    public void initEvent() {

        registerClickText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String passwordString=loginPassEdit.getText().toString();
                String accountString=loginAccountEdit.getText().toString();
                if(passwordString.length()!=7||accountString.length()!=4){
                    show("账号密码个数不正确");
                }else{
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("password",passwordString);
                    editor.putString("account",accountString);
                    editor.apply();
                    Intent intent=new Intent(FindPasswordActivity.this,LoginActivity.class);
                    startActivity(intent);
                    FindPasswordActivity.this.finish();

                }

            }
        });
    }

    public void show(String showString){
        Toast.makeText(FindPasswordActivity.this,showString,Toast.LENGTH_SHORT).show();
    }



}
