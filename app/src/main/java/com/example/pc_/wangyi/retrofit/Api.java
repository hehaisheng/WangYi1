package com.example.pc_.wangyi.retrofit;


import com.example.pc_.wangyi.model.DouBanContent;
import com.example.pc_.wangyi.model.DouBanNews;
import com.example.pc_.wangyi.model.GuoQiaoItem;
import com.example.pc_.wangyi.model.ZhiHuContent;
import com.example.pc_.wangyi.model.ZhiHuNews;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by pc- on 2017/5/17.
 */
public interface    Api {

     String ZHIHU_BASE_URL="http://news.at.zhihu.com/";
     String GUOQIAO_NEW="http://apis.guokr.com/handpick/";
     String GUOQIAO_CONTENT="http://jingxuan.guokr.com/";
     String DOUBAN_BASE_URL="https://moment.douban.com/";
     @GET("api/4/news/before/{id}")
     Observable<ZhiHuNews> fetchZhiHuNews(@Path("id") int id);
     @GET("api/4/news/{id}")
     Observable<ZhiHuContent> fetchZhiHuContent(@Path("id") int id);
     @GET("article.json?retrieve_type=by_since&category=all&limit=25&ad=1")
     Observable<GuoQiaoItem> fecthQuoQiaoNews();
     //@GET("pick/{guoqiaoId}")
    // Observable<GuoQiaoItem.result> fecthGuoQiaoContent(@Path("guoqiaoId")int guoQiaoId);
     @GET("pick/{guoqiaoId}")
     Observable<String> fecthGuoQiaoContent(@Path("guoqiaoId") int guoQiaoId);
     @GET("api/stream/date/{date}")
     Observable<DouBanNews>  fecthDouBanNews(@Path("date") String date);
     //  https://moment.douban.com/api/stream/date/2016-08-11
     @GET("api/post/{id}")
     Observable<DouBanContent> fecthDouBanContent(@Path("id") int id);





}
