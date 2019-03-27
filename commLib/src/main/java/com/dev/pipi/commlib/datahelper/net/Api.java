package com.dev.pipi.commlib.datahelper.net;

import android.util.Log;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/09/03
 *     desc   : 使用前配置IP,最好是写好ApiService接口后再封装HttpService取得单例的ApiService
 *     version: 1.0
 * </pre>
 */

public class Api {
    private static final String TAG = Api.class.getSimpleName();
    private static final int DEFAULT_CONN_TIMEOUT = 60;
    private static final int DEFAULT_READ_TIMEOUT = 60;
    private static final int DEFAULT_WRITE_TIMEOUT = 60;
    private static Map<String, Retrofit> sRetrofitCaches = new HashMap<>();
    private static Map<String, String> sParams = new HashMap<>();

    private static Retrofit getRetrofit(String url) {
        Gson gson = new GsonBuilder().
                registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory<>())
                .create();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_CONN_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(getHttpLoggingInterceptor())
                .addInterceptor(new BaseInterceptor(sParams));
        return new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();
    }
    public static void addParam(String key, String vaule) {
        sParams.put(key, vaule);
    }

    public static void removeParam(String key) {
        sParams.remove(key);
    }

    public static void clearParams() {
        sParams.clear();
    }

    public static Retrofit getInstance() {
        String host = SPUtils.getInstance().getString(BaseUrl.IP);
        Retrofit retrofit = sRetrofitCaches.get(host);
        if (retrofit == null) {
            retrofit = getRetrofit(host);
            sRetrofitCaches.put(host, retrofit);
        }
        return retrofit;
    }
    //日志拦截器
    private static HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
                new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.d(TAG, "log = " + message);
                    }

                });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }
}
