package com.iexamcenter.odiacalendar;


import com.iexamcenter.odiacalendar.news.NewsListFragment;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by sasikanta on 11/10/2017.
 */

public class MyRxJava {
    private static MyRxJava obj;

    public static MyRxJava newInstance() {
        if (obj == null)
            obj = new MyRxJava();
        return obj;
    }


    public void rssInBackground(final MainActivity mContext) {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                mContext.doRssUpdateInBackground();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {

                    }
                });
    }


    public void rssFetchInBackground(final NewsListFragment listFragment) {

        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                listFragment.doRssInBg();
                subscriber.onNext(1);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        listFragment.postExecute();
                    }
                });
    }


}
