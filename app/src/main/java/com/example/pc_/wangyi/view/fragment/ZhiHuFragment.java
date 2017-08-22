package com.example.pc_.wangyi.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.ZhiHuNews;
import com.example.pc_.wangyi.presenter.impl.IFetchCompleteImpl;
import com.example.pc_.wangyi.retrofit.FetchManager;
import com.example.pc_.wangyi.view.activity.ZhiHuActivity;
import com.example.pc_.wangyi.view.activity.ZhiHuContentActivity;

import butterknife.BindView;

public class ZhiHuFragment extends BaseFragment implements View.OnClickListener ,IFetchCompleteImpl{


    @BindView(R.id.zhihu_back)
    ImageView zhihuBack;
    @BindView(R.id.tucao)
    Button tucao;
    @BindView(R.id.dawu)
    Button dawu;
    @BindView(R.id.suiwen)
    Button suiwen;
    @BindView(R.id.middle_text)
    TextView middleText;
    @BindView(R.id.left_text)
    TextView leftText;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.bottom_text)
    TextView bottomText;

    public ImageView leftView, middleView, rightView, bottomView;

    public ZhiHuNews zhiHuNews;
    public FetchManager fetchManager;
    public  ImageView[] imageViews;



    public int toId;
    public int index;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void rxPreLoad() {


       new Thread(new Runnable() {
          @Override
          public void run() {

               fetchManager=FetchManager.newInstance(getActivity());
               compositeSubscription.add(fetchManager.fetch("ZhiHu",ZhiHuFragment.this));
          }
       }).start();
   }

    @Override
    public int getLayoutId() {
        return R.layout.zhihu_fragment;
     }


    @Override
    public void initView() {



        leftView = (ImageView) getActivity().findViewById(R.id.left_recommand);
        middleView = (ImageView) getActivity().findViewById(R.id.middle_recommand);
        rightView = (ImageView) getActivity().findViewById(R.id.right_recommand);
        bottomView = (ImageView) getActivity().findViewById(R.id.bottom_recommand);
        imageViews = new ImageView[]{leftView, middleView, rightView, bottomView};



    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        leftView.setOnClickListener(this);
        rightView.setOnClickListener(this);
        middleView.setOnClickListener(this);
        bottomView.setOnClickListener(this);


        dawu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startToZhiHu(0,0);

            }
        });
        tucao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 startToZhiHu(1,0);
            }
        });
        suiwen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startToZhiHu(2,0);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_recommand:
                toId = zhiHuNews.getStories().get(0).getId();
                index=0;
                break;
            case R.id.middle_recommand:
                toId = zhiHuNews.getStories().get(1).getId();
                index=1;
                break;
            case R.id.right_recommand:
                toId = zhiHuNews.getStories().get(2).getId();
                index=2;
                break;
            case R.id.bottom_recommand:
                toId = zhiHuNews.getStories().get(3).getId();
                index=3;
                break;

        }
        startToActivity(toId,index);

    }

    public void startToActivity(int id,int index) {
        ZhiHuNews.Question question=zhiHuNews.getStories().get(index);
        Intent leftIntent = new Intent(getActivity(), ZhiHuContentActivity.class);
        leftIntent.putExtra("ModelType", "ZhiHu");
        leftIntent.putExtra("Id", id);
        leftIntent.putExtra("Title",question.getTitle());
        leftIntent.putExtra("ImageUrl",question.getImages().get(0));
        startActivity(leftIntent);
    }
    //根据模式不同数据数据，history决定是否是历史数据
    public void startToZhiHu(int model,int history){
        Intent intent = new Intent(getActivity(), ZhiHuActivity.class);
        intent.putExtra("model",model);
        intent.putExtra("History",history);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void loadSuccess(Object object) {
        zhiHuNews=(ZhiHuNews)  object;
        leftText.setText(zhiHuNews.getStories().get(0).getTitle());
        middleText.setText(zhiHuNews.getStories().get(1).getTitle());
        rightText.setText(zhiHuNews.getStories().get(2).getTitle());
        bottomText.setText(zhiHuNews.getStories().get(3).getTitle());
        for (int i = 0; i < 4; i++) {
            int index = zhiHuNews.getStories().get(i).getImages().toString().length();
            String imageUrl = zhiHuNews.getStories().get(i).getImages().toString().substring(1, index - 1);
            if (i != 3) {
                Glide.with(getActivity()).load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT).into(imageViews[i]);


            }
        }

        Glide.with(getActivity()).load(R.drawable.raingif).asGif()
                .diskCacheStrategy(DiskCacheStrategy.RESULT).into(zhihuBack);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(imageViews!=null){
            imageViews=null;
        }
        bottomView=null;
        leftView=null;
        rightView=null;
        middleView=null;
    }
}
