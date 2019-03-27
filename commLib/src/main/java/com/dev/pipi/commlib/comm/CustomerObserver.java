package com.dev.pipi.commlib.comm;

import com.dev.pipi.commlib.exception.ApiException;
import com.dev.pipi.commlib.exception.ExceptionEngine;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/04/04
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public abstract class CustomerObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onComplete() {

    }
    @Override
    public void onError(Throwable e) {
        if(e instanceof ApiException){
            onError((ApiException)e);
        }else{
            onError(new ApiException(e, ExceptionEngine.CUSTOMER_ERROR));
        }
    }

    protected abstract void onError(ApiException e);
}
