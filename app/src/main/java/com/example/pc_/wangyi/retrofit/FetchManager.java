package com.example.pc_.wangyi.retrofit;

import android.content.Context;

import com.example.pc_.wangyi.model.DouBanContent;
import com.example.pc_.wangyi.model.DouBanNews;
import com.example.pc_.wangyi.model.GuoQiaoItem;
import com.example.pc_.wangyi.model.ZhiHuContent;
import com.example.pc_.wangyi.model.ZhiHuNews;
import com.example.pc_.wangyi.presenter.NewDataPresenter;
import com.example.pc_.wangyi.presenter.impl.IFetchCompleteImpl;
import com.example.pc_.wangyi.view.MyApplication;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by pc- on 2017/6/17.
 */

public class FetchManager {



    public Observable<ZhiHuNews> zhiHuNewsObser;
    public  Observable<DouBanNews>  douBanObservable;

    public MyApplication myApplication;
    public static FetchManager fetchManager;
    public Context context;
    public Observable<?>  observable;
    public RxBus rxBus;
    public IFetchCompleteListener iFetchCompleteListener;
    public IFetchInstanceCompleteListener iFetchInstanceCompleteListener;
    public IFetchCompleteImpl iFetchComplete;
    public void setiFetchComplete(IFetchCompleteImpl iFetchComplete){
        this.iFetchComplete=iFetchComplete;
    }



    public ArrayList<ZhiHuNews.Question> dawuQuestion=new ArrayList<>();
    public ArrayList<ZhiHuNews.Question> tucaoQuestion=new ArrayList<>();
    public ArrayList<ZhiHuNews.Question> xiaoshiQuestion=new ArrayList<>();
    public List<ZhiHuNews> zhiHuNewsList=new ArrayList<>();

    public int countToSuccess=0;


    public static FetchManager newInstance(Context context){
        if(fetchManager==null){
            synchronized (FetchManager.class){
                if(fetchManager==null){
                    fetchManager=new FetchManager(context);
                }
            }
        }
        return fetchManager;
    }
    public FetchManager(Context context){
        this.context=context.getApplicationContext();
        myApplication=MyApplication.getNewInstance();
        rxBus=RxBus.newInstance();

    }

    //获取知乎分类数据
    public Subscription fetchZhiHuCategoty(int start){


                zhiHuNewsObser= NewDataPresenter.getInstance(context).fecthNews(start);
                //这里的线程切换有点不合第一印象，因为是为了更好的处理后面的代码间的逻辑
               return  zhiHuNewsObser.compose(ComSchedulers.<ZhiHuNews>applyIoSchedulers())
                        .flatMap(new Func1<ZhiHuNews, Observable<ZhiHuNews.Question>>() {
                            @Override
                            public Observable<ZhiHuNews.Question> call(ZhiHuNews zhiHuNews) {
                                zhiHuNewsList.add(zhiHuNews);
                                myApplication.setZhiHuNewsList(zhiHuNewsList);
                                return  Observable.from(zhiHuNews.getStories());
                            }
                        })
                        .subscribe(new Action1<ZhiHuNews.Question>() {
                            @Override
                            public void call(ZhiHuNews.Question question) {
                                if(question.getTitle().contains("大误")) {
                                    countToSuccess++;
                                    dawuQuestion.add(question);
                                    myApplication.setDawuList(dawuQuestion);
                                }
                                else if(question.getTitle().contains("吐槽")) {
                                    countToSuccess++;
                                    tucaoQuestion.add(question);
                                    myApplication.setTucaoList(tucaoQuestion);
                                }
                                else if(question.getTitle().contains("小事")) {
                                    countToSuccess++;
                                    xiaoshiQuestion.add(question);
                                    myApplication.setXiaoshiList(xiaoshiQuestion);
                                }
                                if(countToSuccess==30){
                                    countToSuccess=0;
//                                    dawuQuestion.clear();
//                                    tucaoQuestion.clear();
//                                    xiaoshiQuestion.clear();
                                    iFetchCompleteListener.loadSuccess();

                                }

                            }
                        });

            }

    public Subscription fetchDouBanCategory(String jixueDate) {
       douBanObservable= NewDataPresenter.getInstance(context).fetchDouBanNews(jixueDate);
       return   douBanObservable.compose(ComSchedulers.<DouBanNews>applyIoSchedulers())
                .flatMap(new Func1<DouBanNews, Observable<DouBanNews.posts>>() {
                    @Override
                    public Observable<DouBanNews.posts> call(DouBanNews douBanNews) {
                        return Observable.from(douBanNews.getPosts());
                    }
                }).filter(new Func1<DouBanNews.posts, Boolean>() {
                    @Override
                    public Boolean call(DouBanNews.posts posts) {
                        //符合条件的才会传递给观察者处理
                        return posts.getTitle().contains("打鸡血");
                    }
                }).subscribe(new Action1<DouBanNews.posts>() {
                    @Override
                    public void call(DouBanNews.posts posts) {
                        iFetchInstanceCompleteListener.loadSuccess(posts);
                    }
                });
    }

    //获取数据
    public Subscription fetch(final String dataName, final IFetchCompleteImpl iFetchComplete){

        this.iFetchComplete=iFetchComplete;
        switch (dataName) {
            case "ZhiHu":
                observable = NewDataPresenter.getInstance(context).fecthNews(20170517);
                break;
            case "DouBan":
                String date = "2017-05-18";
                observable = NewDataPresenter.getInstance(context).fetchDouBanNews(date);
                break;
            case "GuoQiao":
                observable = NewDataPresenter.getInstance(context).fecthGuoQiao();
                break;
        }

            return observable
                    .compose(ComSchedulers.<Object>applyIoSchedulers())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object object) {

                            if(object instanceof  ZhiHuNews){
                                myApplication.setZhiHuNews((ZhiHuNews) object);
                            }else if(object instanceof DouBanNews){
                                myApplication.setDouBanNewses((DouBanNews) object);
                            }else if(object instanceof GuoQiaoItem){
                                myApplication.setGuoqiaoList(((GuoQiaoItem) object).getResult());
                            }
                             iFetchComplete.loadSuccess(object);
                            //iFetchInstanceCompleteListener.loadSuccess(object);

                        }
                    });


    }
    //获取详细的内容数据
    public Subscription fetchContent(int id,String dataName){
        switch (dataName) {
            case "ZhiHu":
                observable = NewDataPresenter.getInstance(context).fecthContent(id);
                break;
            case "DouBan":
                observable = NewDataPresenter.getInstance(context).fetchDouBanContent(id);
                break;
            case "GuoQiao":
                observable = NewDataPresenter.getInstance(context).fecthGuoQiaoContent(id);

                break;
        }
        return observable.compose(ComSchedulers.applyIoSchedulers())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        if(o instanceof  String){
                            iFetchInstanceCompleteListener.loadSuccess(o);

                        }else if(o instanceof ZhiHuContent){
                            iFetchInstanceCompleteListener.loadSuccess((ZhiHuContent) o);

                        }else if(o instanceof DouBanContent){
                            iFetchInstanceCompleteListener.loadSuccess((DouBanContent) o);

                        }

                    }
                });

    }

    public void setFetchCompleteListener(IFetchCompleteListener iFetchCompleteListener){
        this.iFetchCompleteListener=iFetchCompleteListener;
    }
    public void setiFetchInstanceCompleteListener(IFetchInstanceCompleteListener iFetchInstanceCompleteListener){
        this.iFetchInstanceCompleteListener=iFetchInstanceCompleteListener;
    }
   public interface  IFetchCompleteListener{
       void loadSuccess();

   }
    public interface  IFetchInstanceCompleteListener{
        void loadSuccess(Object object);

    }




}
