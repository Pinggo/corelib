package com.dev.pipi.commlib.exception;

import android.net.ParseException;
import android.support.annotation.IntDef;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.ConnectException;

import retrofit2.HttpException;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/04/04
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class ExceptionEngine {
    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;
    private static final int PARSE_ERROR = 600;
    private static final int NETWORD_ERROR = 601;
    public static final int UNKNOWN = 602;
    public static final int CUSTOMER_ERROR = 603;
    public static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof HttpException) {             //HTTP错误
            HttpException httpException = (HttpException) e;
            ex = new ApiException(e, httpException.code());
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                    ex.setMsg("网络错误");
                    break;
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                    ex.setMsg("服务器内部错误");
                    break;
                default:
                    ex.setMsg("网络错误");  //均视为网络错误
                    break;
            }
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ApiException(e, PARSE_ERROR);
            ex.setMsg("解析错误");            //均视为解析错误
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ApiException(e, NETWORD_ERROR);
            ex.setMsg("连接失败");  //均视为网络错误
            return ex;
        } else {
            if (e instanceof ApiException) {
                return (ApiException)e;
            }
            ex = new ApiException(e, UNKNOWN);
            ex.setMsg(e.getMessage());
            return ex;
        }
    }
}
