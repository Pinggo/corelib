package com.dev.pipi.commlib.exception;

import com.dev.pipi.commlib.base.BaseHttpResult;

import io.reactivex.functions.Function;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/09/17
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class ResultFunc<V extends BaseHttpResult<Object>> implements Function<V,V> {
    @Override
    public V apply(V v) throws Exception {
        if (!v.isSuccess()) {
            if (v.getMsg() == null) {
                throw new ApiException(v.getCode(), "服务器发生错误");
            }
            throw new ApiException(v.getCode(),v.getMsg());
        }
        return v;
    }
}

