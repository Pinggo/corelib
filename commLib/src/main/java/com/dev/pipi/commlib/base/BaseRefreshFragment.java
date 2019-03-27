package com.dev.pipi.commlib.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.dev.pipi.commlib.R;
import com.dev.pipi.commlib.base.recyclerView.BaseAdapter;
import com.dev.pipi.commlib.comm.WPageNavigator;
import com.dev.pipi.commlib.exception.ApiException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;

import java.util.List;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/04/04
 *     desc   : 上拉下拉页面的公共类,T对应着CommAdapter中泛型T
 *     version: 1.0
 * </pre>
 */

public abstract class BaseRefreshFragment<T> extends BaseFragment implements BaseRefreshView<T> {
    RecyclerView mRecyclerView;
    SmartRefreshLayout mRefreshLayout;
    protected BaseAdapter<T> mAdapter;
    private boolean isLastPage;
    protected WPageNavigator mWPageNavigator = new WPageNavigator();

    @Override
    protected int getLayoutId() {
        return R.layout.layout_smart_refresh;
    }

    @NonNull
    protected abstract BaseAdapter<T> initAdapter();

    @Override
    protected void init() {
        mRefreshLayout = mRootView.findViewById(R.id.refresh_layout);
        mRecyclerView = mRootView.findViewById(R.id.recyclerView);
        mAdapter = initAdapter();
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BaseRefreshFragment.this.onItemClick(position);
            }
        });
        mAdapter.setEmptyView(mRecyclerView);
        initRefreshLayout(mRefreshLayout);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onFooterFinish(RefreshFooter footer, boolean success) {
                super.onFooterFinish(footer, success);
                if (isLastPage) {
                    mRefreshLayout.setLoadmoreFinished(true);
                }
            }

            @Override
            public void onHeaderFinish(RefreshHeader header, boolean success) {
                super.onHeaderFinish(header, success);
                if (isLastPage) {
                    mRefreshLayout.setLoadmoreFinished(true);
                }
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                super.onRefresh(refreshlayout);
                mWPageNavigator.setPageNum(1);
                mRefreshLayout.resetNoMoreData();
                BaseRefreshFragment.this.onRefresh();
            }

            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                super.onLoadmore(refreshlayout);
                mWPageNavigator.setPageNum(mWPageNavigator.getPageNum() + 1);
                BaseRefreshFragment.this.onLoadmore();
            }
        });
        initFragment();
    }

    @Override
    protected void loadData() {
        super.loadData();
        autoRefresh();
    }

    /**
     * 常用的条目点击事件
     *
     * @param position
     */
    protected void onItemClick(int position) {

    }

    protected abstract void initFragment();

    protected abstract void onRefresh();

    protected abstract void onLoadmore();

    protected void autoRefresh() {
        mRefreshLayout.autoRefresh();
    }

    public void onLoadDatas(List<T> datas, boolean isLastPage, boolean isLoadMore) {
        this.isLastPage = isLastPage;
        if (!isLoadMore) {
            mAdapter.setNewData(datas);
        } else {
            mAdapter.addAll(datas);
        }
        onLoadDatas(datas);
    }

    /**
     * 备用
     *
     * @param datas
     */
    protected void onLoadDatas(List<T> datas) {

    }

    public void onLoadDatasError(ApiException e) {
        ToastUtils.showShort(e.getMsg());
        cancelRefresh(mRefreshLayout);
    }

    @Override
    public void onLoadComplete() {
        cancelRefresh(mRefreshLayout);
    }

}
