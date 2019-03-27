package com.dev.pipi.commlib.base.recyclerView;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dev.pipi.commlib.base.recyclerView.bean.BaseSection;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/03/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */


public abstract class BaseSectionAdapter<T extends BaseSection> extends BaseAdapter<T> {
    protected static final int SECTION_VIEW = 0x0005;
    private int sectionResId;

    public BaseSectionAdapter(Context context, int layoutId, int sectionResId) {
        this(context, layoutId, sectionResId, null);
    }

    public BaseSectionAdapter(Context context, int layoutId, int sectionResId, List<T> data) {
        super(context, layoutId, data);
        this.sectionResId = sectionResId;
    }

    @Override
    protected int getItemDifViewType(int position) {
        int index = position - getHeaderCount();
        return mData.get(index).isHeader ? SECTION_VIEW : NORMAL_VIEW;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SECTION_VIEW) {
            return BaseViewHolder.getViewHolder(parent, sectionResId);
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == SECTION_VIEW) {
            int index = position - getHeaderCount();
            convertSection(holder, mData.get(index));
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int layoutPosition = holder.getLayoutPosition();
        setFullSpan(holder, getItemViewType(layoutPosition) == SECTION_VIEW);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager manager = (GridLayoutManager) layoutManager;
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int itemViewType = getItemViewType(position);
                    return itemViewType == SECTION_VIEW ? manager.getSpanCount() : 1;
                }
            });
        }
    }

    protected abstract void convertSection(BaseViewHolder holder, T t);

    @Override
    public abstract void convert(BaseViewHolder holder, T t);

    public List<T> getHeaders() {
        List<T> headers = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            T t = mData.get(i);
            if (t.isHeader) {
                headers.add(t);
            }
        }
        return headers;
    }
}
