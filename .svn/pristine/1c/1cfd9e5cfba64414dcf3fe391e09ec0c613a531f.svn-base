package com.hs.advertise.utils;

import android.support.annotation.NonNull;

import com.lunzn.tool.log.LogUtil;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Desc: TODO
 * <p>
 * Author: ganxinrong
 * PackageName: com.hs.advertise.mvp.handle
 * ProjectName: Advertise
 * Date: 2020/3/16 14:05
 */
public class RxjavaUtils {
    
    private static final String TAG = "Tag_"+ RxjavaUtils.class.getSimpleName();

    private static ArrayList<Disposable> mDisposable = new ArrayList<>();

    //主线程做操作
    public static void doOnUIThread(UITask uiTask) {
        Observable.just(uiTask)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UITask>() {
                    @Override
                    public void accept(UITask uiTask) throws Exception {
                        uiTask.doOnUI();
                    }
                });

    }

    //io线程做操作
    public static void doOnThread(ThreadTask threadTask) {
        Observable.just(threadTask)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<ThreadTask>() {
                    @Override
                    public void accept(ThreadTask threadTask1) throws Exception {
                        threadTask1.doOnThread();
                    }
                });
    }

    /**
     * 每隔一秒执行next，计时结束则执行onComplete
     *
     * @param seconds 倒计时n秒
     * @param next
     */
    public static void countdown(final long seconds, final IRxNext next) {

        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(seconds+1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        mDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(@NonNull Long number) {
                        LogUtil.i(TAG,"countdown number is:"+number);
                        if (next != null) {
                            next.doNext(seconds - number);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        if (next != null) {
                            next.onComplete();
                        }
                    }
                });
    }



    /**
     * 取消订阅
     */
    public static void cancel() {
        for (int i = 0; i < mDisposable.size(); i++) {
            Disposable disposable = mDisposable.get(i);
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
                LogUtil.i(TAG, "====定时器取消 disposable " + disposable);
            }
        }
        mDisposable.clear();
    }

    public interface IRxNext {
        void doNext(long number);

        void onComplete();
    }

    public interface ThreadTask {
        void doOnThread();
    }

    public interface UITask {
        void doOnUI();
    }

}
