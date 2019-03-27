package com.dev.pipi.commlib.base;

import com.dev.pipi.commlib.base.mvp.IView;
import com.dev.pipi.commlib.exception.ApiException;

import java.util.List;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/04/04
 *     desc   : 对应上拉下拉页面的接口,T对应着CommAdapter中泛型T
 *     version: 1.0
 * </pre>
 */

public interface BaseRefreshView<T> extends IView{
    void onLoadDatas(List<T> datas, boolean isLastPage, boolean isLoadMore);

    void onLoadDatasError(ApiException e);

    void onLoadComplete();
}
