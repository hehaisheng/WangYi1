package com.example.pc_.wangyi.retrofit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pc- on 2017/5/23.
 */
public class ComSchedulers {



    private final static Observable.Transformer ioTransformer = new Observable.Transformer() {
        @Override
        public Object call(Object o) {
            return ((Observable) o).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    public static <T> Observable.Transformer<T, T> applyIoSchedulers() {
        return (Observable.Transformer<T, T>) ioTransformer;
    }

//
//    zhiHuNewsObser.observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe(new Action1<ZhiHuNews>() {
//        @Override
//        public void call(ZhiHuNews zhiHuNews) {
//
//
//            zhiHuAdapter.setNewItemList(zhiHuNews.getStories());
//            listView.setAdapter(zhiHuAdapter);
//
//        }
//    });
//}
}
