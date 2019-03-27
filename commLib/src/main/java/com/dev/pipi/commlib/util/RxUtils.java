package com.dev.pipi.commlib.util;

import com.dev.pipi.commlib.base.BaseActivity;
import com.dev.pipi.commlib.base.BaseFragment;
import com.dev.pipi.commlib.base.mvp.IView;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/04/04
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public final class RxUtils {
    public static <T> ObservableTransformer<T, T> schedulersTransformer() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
    public static <T> ObservableTransformer<T, T> viewTransformer(final IView view, final boolean isShowLoading) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                if (view == null) {
                    return observable.doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            disposable.dispose();
                        }
                    });
                }
                LifecycleTransformer<T> transformer;
                if (view instanceof BaseFragment) {
                    transformer = ((BaseFragment) view).bindUntilEvent(FragmentEvent.DESTROY_VIEW);
                } else if (view instanceof BaseActivity) {
                    transformer = ((BaseActivity) view).bindUntilEvent(ActivityEvent.DESTROY);
                } else {
                    throw new RuntimeException("the Activity or Fragment must extends BaseActivity or BaseFragment");
                }
                return observable
                        .compose(RxUtils.<T>schedulersTransformer())
                        .compose(transformer)
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull Disposable disposable) throws Exception {
                                if (isShowLoading) {
                                    view.showLoading();
                                }
                            }
                        })
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                                if (isShowLoading) {
                                    view.hideLoading();
                                }
                            }
                        });
            }
        };
    }
    public static <T> ObservableTransformer<T, T> viewTransformer(final IView view) {
        return viewTransformer(view, false);
    }

    public static <T> Observable<T> transformer(Observable<T> observable,IView view, boolean isShowLoading) {
        return observable.compose(RxUtils.<T>viewTransformer(view, isShowLoading));
    }

    public static <T> Observable<T> transformer(Observable<T> observable,IView view) {
        return observable.compose(RxUtils.<T>viewTransformer(view, false));
    }
}
