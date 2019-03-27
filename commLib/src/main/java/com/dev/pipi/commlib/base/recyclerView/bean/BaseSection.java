package com.dev.pipi.commlib.base.recyclerView.bean;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/03/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */


public abstract class BaseSection<T> {
    public boolean isHeader;
    public T t;
    public String header;
    public BaseSection(boolean isHeader, String header) {
        this.isHeader = isHeader;
        this.header = header;
        this.t = null;
    }

    public BaseSection(T t) {
        this.isHeader = false;
        this.header = null;
        this.t = t;
    }
}
