package com.dev.pipi.commlib.exception;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/04/04
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class HttpResultFunc<T> implements Function<Throwable,ObservableSource<T>> {
    @Override
    public ObservableSource<T> apply(Throwable throwable) throws Exception {
        return Observable.error(ExceptionEngine.handleException(throwable));
    }
}
