package com.example.pc_.wangyi.story.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.pc_.wangyi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc- on 2017/8/12.
 */
public class CustomSearchLayout extends LinearLayout implements View.OnClickListener{

    //适配器不应该写在这个类，因为匹配的数据可以会改变
    //适配器可以会不同，所以应该写在Activity
    //把这里获取的数据在Activity显示
    public EditText editText;
    public ImageView deleteImage;
    public Button queryButton;


    public List<String>  hotStrings=new ArrayList<>();
    public List<String>  mateStrings=new ArrayList<>();
    public List<String>  fitResultStrings=new ArrayList<>();

    public Context context;
    public ISearchSuccess iSearchSuccess;

    public CustomSearchLayout(Context context) {
        super(context);
        this.context=context;
    }

    public CustomSearchLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context=context;
        LayoutInflater.from(context).inflate(R.layout.custom_search_layout, this);
        initView();
        initClick();
        initData();
    }
    public  void initView(){
        editText=(EditText)  findViewById(R.id.search_edit);
        deleteImage=(ImageView) findViewById(R.id.search_delete);
        queryButton=(Button) findViewById(R.id.search_query);

    }
    public void initClick(){
        //当点击时，就出现热门数据
        editText.setOnClickListener(this);
        //当文本改变时，就显示出匹配的数据
        editText.addTextChangedListener(new TextChangeListener());
        deleteImage.setOnClickListener(this );
        queryButton.setOnClickListener(this);


    }
    public void initData(){
        mateStrings.add("这是上衣1");
        mateStrings.add("这是上衣2");
        mateStrings.add("这是上衣3");
        mateStrings.add("这是男上衣1");
        mateStrings.add("这是男上衣2");
        mateStrings.add("这是男上衣3");
        mateStrings.add("这是女上衣1");
        mateStrings.add("这是女上衣2");
        mateStrings.add("这是女上衣3");

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.search_edit:
                if(editText.getText().toString().equals("")){
                    startSearchData(0);
                } 
                iSearchSuccess.searchVisibility(1);
                iSearchSuccess.searchSuccess(hotStrings);
                break;
            case R.id.search_delete:
                if(!editText.getText().toString().equals("")){
                    editText.setText(null);
                }
                iSearchSuccess.searchVisibility(0);

                break;
            //查询结果
            case R.id.search_query:

                break;
        }
    }

    public void startSearchData(int dataType){


        if(dataType==0){
            hotStrings.clear();
            for (int i=0;i<4;i++){
                hotStrings.add("这是热门上衣"+i);
            }
        }else if(dataType==1){
            //匹配
        }else  if(dataType==2){
            //最后的结果
        }

    }
    public class TextChangeListener implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if(!s.toString().equals("")){

                fitResultStrings.clear();
                String  editStr=s.toString();
                for(int i=0;i<mateStrings.size();i++){
                    if(mateStrings.get(i).contains(editStr)){
                        fitResultStrings.add(mateStrings.get(i));
                    }
                }
                iSearchSuccess.searchVisibility(1);
                if(fitResultStrings.size()!=0){
                    iSearchSuccess.searchSuccess(fitResultStrings);
                }
                deleteImage.setVisibility(VISIBLE);
            }else{
                deleteImage.setVisibility(GONE);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
    public interface  ISearchSuccess{
        void   searchSuccess(List<String> searchDataList);
        //0代表无网络，1代表无数据
        void   searchFail(int i);
        void   searchVisibility(int i);
    }
    public void  setISearchSuccess(ISearchSuccess iSearchSuccess){
        this.iSearchSuccess=iSearchSuccess;
    }

}
