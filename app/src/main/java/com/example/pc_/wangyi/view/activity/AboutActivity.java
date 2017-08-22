package com.example.pc_.wangyi.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.pc_.wangyi.R;
import com.example.pc_.wangyi.utils.AutoSplit;

/**
 * Created by pc- on 2017/7/5.
 */
public class AboutActivity  extends Activity {


    public String str1="项目的整体架构基于MVP";
    public String str2="项目的界面的列表基于RecyclerView+BaseQuickAdapter";
    public String str3="并使用预加载的方式来获取显示的数据";
    public String str4="控件初始化使用ButterKnife";
    public String str5="图片的加载基于自己写的图片加载框架HHImageLoader";
    public String str6="知乎，豆瓣，果壳数据的获取基于Retrofit+RxJava+OkHttp，内容的保存基于SQLite";
    public String str7="音乐文件的快传分享基于WifiAp+Socket,另音乐的评论是基于DatagramSocket";
    public String str8="音乐文件的播放Service是基于AIDL+Binder池";
    public String str9="在线音乐的下载和断点续传基于多线程+OkHttp+SQLite";
    public String str10="音乐的显示排序是基于优化过的快排算法 ";
    public String str11="朋友圈功能的实现基于DatagramSocket+RxBus，可以评论，点赞，发朋友圈";
    public String str13="另还实现了注册，登录更改等功能";
    public String  str14="使用LeakCanary监听项目，运行暂无发现内存泄漏";
    public  String[]  strings=new String[]{str1,str2,str3,str4,str5,str6,str7,str8,str9,str10,str11,str13,str14};

    public TextView  contentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        contentView=(TextView)  findViewById(R.id.about_content);
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=0;i<strings.length;i++){
            if(i!=strings.length-1){
                stringBuilder.append(strings[i]).append("好音好");
            }else {
                stringBuilder.append(strings[i]);
            }

        }
        AutoSplit autoSplit=new AutoSplit();
        contentView.setText(autoSplit.autoSplit(stringBuilder.toString()));

    }
}
