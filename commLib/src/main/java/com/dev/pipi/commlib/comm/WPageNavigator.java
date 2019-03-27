package com.dev.pipi.commlib.comm;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/03/29
 *     desc   : 分页管理类
 *     version: 1.0
 * </pre>
 */

public class WPageNavigator {
    private int pageNum = 1;
    private int pageSize = 10;
    private long total;
    private static final String PAGE_NUM = "pageNum";
    private static final String CURRENT_PAGE = "currentPage";
    private static final String PAGE_SIZE = "pageSize";
    private Map<String, Object> params;
    public WPageNavigator() {
        params = new HashMap<>();
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public void addParam(String key, Object vaule) {
        params.put(key, vaule);
    }
    public void addAll(Map<String,? extends Object> map) {
        params.putAll(map);
    }

    public void removeParam(String key) {
        params.remove(key);
    }

    public void clearParam() {
        params.clear();
    }

    /**
     * 当前是否是加载更多
     * @return true:加载更多 反之亦然
     */
    public boolean isLoadMore() {
        return pageNum != 1;
    }

    /**
     * 当前页是最后一页
     * @return true:最后一页 反之亦然
     */
    public boolean isLastPage() {
        return pageNum * pageSize >= total;
    }
    public boolean isLastPage(long total) {
        this.total = total;
        return pageNum * pageSize >= total;
    }

    public Map<String, Object> getParams() {
        addParam(PAGE_NUM, pageNum);
        addParam(PAGE_SIZE, pageSize);
        return params;
    }
    public Map<String, Object> getCurrentParams() {
        addParam(CURRENT_PAGE, pageNum);
        addParam(PAGE_SIZE, pageSize);
        return params;
    }

    public Map<String, Object> getNoPageParams() {
        return params;
    }
}
