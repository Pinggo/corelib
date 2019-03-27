package com.dev.pipi.commlib.comm;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/05/15
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class EventMsg<T>{
    private int code;
    private T t;

    public EventMsg(int code, T t) {
        this.code = code;
        this.t = t;
    }

    public EventMsg(int code) {
        this.code = code;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
