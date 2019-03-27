package com.dev.pipi.commlib.exception;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/04/04
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class ApiException extends Exception{
    private int code;
    private String msg;

    public ApiException(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }

    public ApiException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
