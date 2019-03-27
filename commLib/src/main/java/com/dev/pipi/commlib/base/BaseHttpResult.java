package com.dev.pipi.commlib.base;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/09/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public interface BaseHttpResult<T> {
    boolean isSuccess();

    int getCode();

    String getMsg();

    T getData();
}
