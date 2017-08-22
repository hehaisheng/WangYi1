package com.example.pc_.wangyi.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.model.DouBanNews;
import com.example.pc_.wangyi.presenter.impl.IFetchCompleteImpl;
import com.example.pc_.wangyi.retrofit.FetchManager;
import com.example.pc_.wangyi.view.activity.DouBanActivity;
import com.example.pc_.wangyi.view.activity.ZhiHuActivity;
import com.example.pc_.wangyi.view.activity.ZhiHuContentActivity;

import butterknife.BindView;

/**
 * Created by pc- on 2017/5/14.
 */
public class DouBanFragment extends BaseFragment implements View.OnClickListener ,IFetchCompleteImpl{



    //视图类

    public ImageView doubanLeftView, doubanMiddleView, doubanRightView, doubanBottomView;
    public ImageView[] imageViews;
    @BindView(R.id.douban_gif)
    ImageView doubanGif;
    @BindView(R.id.douban_tucao)
    Button doubanTucao;
    @BindView(R.id.douban_dawu)
    Button doubanDawu;
    @BindView(R.id.douban_suiwen)
    Button doubanSuiwen;
    @BindView(R.id.douban_middle_text)
    TextView doubanMiddleText;
    @BindView(R.id.douban_left_text)
    TextView doubanLeftText;
    @BindView(R.id.douban_right_text)
    TextView doubanRightText;
    @BindView(R.id.douban_bottom_text)
    TextView doubanBottomText;


    public DouBanNews douBanNews1;
    public FetchManager  fetchManager;





    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void rxPreLoad() {
        if(myApplication.getDouBanNewses() == null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                        fetchManager= FetchManager.newInstance(getActivity());
                        compositeSubscription.add(fetchManager.fetch("DouBan",DouBanFragment.this));
                }
            }).start();

        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.douban_fragment;
    }


    @Override
    public void initView() {


        doubanLeftView = (ImageView) getActivity().findViewById(R.id.douban_left_recommand);
        doubanRightView = (ImageView) getActivity().findViewById(R.id.douban_right_recommand);
        doubanMiddleView = (ImageView) getActivity().findViewById(R.id.douban_middle_recommand);
        doubanBottomView = (ImageView) getActivity().findViewById(R.id.douban_bottom_recommand);
        imageViews = new ImageView[]{doubanLeftView, doubanMiddleView, doubanRightView, doubanBottomView};


    }

    @Override
    public void initData() {

        if(myApplication.getDouBanNewses()!=null){
            douBanNews1 = myApplication.getDouBanNewses();
            setImageToView(douBanNews1);
        }



    }


    @Override
    public void initEvent() {
        doubanDawu.setOnClickListener(this);
        doubanSuiwen.setOnClickListener(this);
        doubanTucao.setOnClickListener(this);
        doubanLeftView.setOnClickListener(this);
        doubanMiddleView.setOnClickListener(this);
        doubanRightView.setOnClickListener(this);
        doubanBottomView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.douban_tucao:
                toDouBanActivity("SuiWen");
                break;
            case R.id.douban_dawu:
                Intent toZhiHu = new Intent(getActivity(), ZhiHuActivity.class);
                toZhiHu.putExtra("model", 3);
                startActivity(toZhiHu);
                break;
            case R.id.douban_suiwen:
                toDouBanActivity("JiXue");
                break;
            case R.id.douban_left_recommand:
                toZhihuContentActivity(douBanNews1.getPosts().get(0).getId(), "DouBan");
                break;
            case R.id.douban_middle_recommand:
                toZhihuContentActivity(douBanNews1.getPosts().get(1).getId(), "DouBan");
                break;
            case R.id.douban_right_recommand:
                toZhihuContentActivity(douBanNews1.getPosts().get(2).getId(), "DouBan");
                break;
            case R.id.douban_bottom_recommand:
                toZhihuContentActivity(douBanNews1.getPosts().get(3).getId(), "DouBan");
                break;
        }
    }

    public void toDouBanActivity(String modelType) {
        Intent toDouBan = new Intent(getActivity(), DouBanActivity.class);
        toDouBan.putExtra("DouBanType", modelType);
        startActivity(toDouBan);
    }

    public void toZhihuContentActivity(int id, String modelType) {
        Intent toContentIntent3 = new Intent(getActivity(), ZhiHuContentActivity.class);
        toContentIntent3.putExtra("Id", id);
        toContentIntent3.putExtra("ModelType", modelType);
        startActivity(toContentIntent3);
    }


    public void setImageToView(DouBanNews douBanNews2) {
        doubanLeftText.setText(douBanNews2.getPosts().get(0).getTitle());
        doubanMiddleText.setText(douBanNews2.getPosts().get(1).getTitle());
        doubanRightText.setText(douBanNews2.getPosts().get(2).getTitle());
        doubanBottomText.setText(douBanNews2.getPosts().get(3).getTitle());
        for (int i = 0; i < 4; i++) {
            if (douBanNews2.getPosts().get(i).getThumbs().size() == 0) {
                imageViews[i].setImageResource(R.drawable.earth);
            } else {
                String imageUrl = douBanNews2.getPosts().get(i).getThumbs().get(0).getMedium().getUrl();
                Glide.with(getActivity()).load(imageUrl).into(imageViews[i]);
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDetach() {
        super.onDetach();

        if(imageViews!=null){
            imageViews=null;
        }
        doubanBottomView=null;
       doubanLeftView=null;
       doubanRightView=null;
       doubanMiddleView=null;
    }

    @Override
    public void loadSuccess(Object object) {
        douBanNews1=(DouBanNews) object;
        setImageToView(douBanNews1);
    }
}


