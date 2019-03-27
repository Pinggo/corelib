package com.dev.pipi.commlib.exception;

import com.dev.pipi.commlib.base.BaseHttpResult;

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

public class ServerResultFunc<T> implements Function<BaseHttpResult<T>,T> {
    @Override
    public T apply(BaseHttpResult<T> httpResult) throws Exception {
        if (!httpResult.isSuccess()) {
            if (httpResult.getMsg() == null) {
                throw new ApiException(httpResult.getCode(), "服务器发生错误");
            }
            throw new ApiException(httpResult.getCode(),httpResult.getMsg());
        }
        if (httpResult.getData() == null) {
            if (httpResult.getCode() == 0) {
                throw new ApiException(800, "暂无数据");
            }
            throw new ApiException(httpResult.getCode(), "暂无数据");
        }
        return httpResult.getData();
    }
}

