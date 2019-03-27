package com.dev.pipi.commlib.datahelper.net;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/04/08
 *     desc   : 请求头
 *     version: 1.0
 * </pre>
 */

public class BaseInterceptor implements Interceptor {
    private Map<String, String> headers;
    public BaseInterceptor(Map<String, String> headers) {
        this.headers = headers;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request()
                .newBuilder();
        if (headers != null && headers.size() > 0) {
            Set<String> keys = headers.keySet();
            for (String headerKey : keys) {
                builder.addHeader(headerKey, headers.get(headerKey)).build();
            }
        }
        return chain.proceed(builder.build());
    }
}
