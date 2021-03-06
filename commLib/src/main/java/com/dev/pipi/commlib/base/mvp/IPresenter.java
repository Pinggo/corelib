package com.dev.pipi.commlib.base.mvp;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/03/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public interface IPresenter<T>{
    void attachView(T view);

    void detachView();
}
