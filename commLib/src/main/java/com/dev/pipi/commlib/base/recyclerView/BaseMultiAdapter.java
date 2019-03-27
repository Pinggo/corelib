package com.dev.pipi.commlib.base.recyclerView;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.cjt2325.cameralibrary.util.LogUtil;
import com.dev.pipi.commlib.base.recyclerView.bean.BaseMultiItem;

import java.util.List;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/10/08
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public abstract class BaseMultiAdapter<T extends BaseMultiItem> extends BaseAdapter<T>{
    private SparseIntArray layouts;
    public BaseMultiAdapter(Context context) {
        super(context);
    }
    public BaseMultiAdapter(Context context, List<T> list) {
        super(context,list);
    }

    @Override
    protected int getItemDifViewType(int position) {
        return mData.get(position).getItemType();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = layouts.get(viewType,-1);
        if (layoutId != -1) {
            return BaseViewHolder.getViewHolder(parent, layoutId);
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    protected void addItemType(int type, int layoutResId) {
        if (layouts == null) {
            layouts = new SparseIntArray();
        }
        layouts.put(type, layoutResId);
    }

    @Override
    public void convert(BaseViewHolder holder, T t) {
        convertItem(holder, t);
    }
    protected abstract void convertItem(BaseViewHolder helper, T item);
}
