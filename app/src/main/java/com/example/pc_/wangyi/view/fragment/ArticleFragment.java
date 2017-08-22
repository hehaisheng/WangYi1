package com.example.pc_.wangyi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.pc_.wangyi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc- on 2017/5/17.
 */
public class  ArticleFragment  extends BaseFragment {


    public int start=20170517;
    public String[] titles={"知乎","果壳","豆瓣"};


    public TabLayout tabLayout;
    public ViewPager viewPager;
    public ZhiHuFragment zhiHuFragment;
    public GuoQiaoFragment guoQiaoFragment;
    public  DouBanFragment douBanFragment;
    public View view=null;



    public List<Fragment> fragmentList=new ArrayList<>();




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void rxPreLoad() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.art_fragment;
    }


    @Override
    public void initView() {


        fragmentList.clear();
        zhiHuFragment=new  ZhiHuFragment();
        guoQiaoFragment=new GuoQiaoFragment();
        douBanFragment=new DouBanFragment();
        fragmentList.add(zhiHuFragment);
        fragmentList.add(guoQiaoFragment);
        fragmentList.add(douBanFragment);
        tabLayout=(TabLayout) getActivity().findViewById(R.id.art_tab);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager=(ViewPager)  getActivity().findViewById(R.id.art_viewpager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        viewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return  fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });

    }

    @Override
    public void initData() {


    }

    @Override
    public void initEvent() {


    }
}
