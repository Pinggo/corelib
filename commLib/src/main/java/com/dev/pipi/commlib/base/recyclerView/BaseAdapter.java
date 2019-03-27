package com.dev.pipi.commlib.base.recyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.pipi.commlib.R;
import com.dev.pipi.commlib.base.recyclerView.bean.IExpandable;

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


public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    protected String TAG = BaseAdapter.class.getSimpleName();
    protected Context mContext;
    protected int layoutId;
    protected List<T> mData;
    protected static final int NORMAL_VIEW = 0x0001;
    protected static final int HEADER_VIEW = 0x0002;
    protected static final int FOOTER_VIEW = 0x0003;
    protected static final int EMPTY_VIEW = 0x0004;
    private List<View> mHeaderViews = new ArrayList<>();
    private List<View> mFooterViews = new ArrayList<>();
    private View mEmptyView;
    private boolean canLoad;

    public BaseAdapter(Context context, @LayoutRes int layoutId) {
        this(context, layoutId, null);
    }

    public BaseAdapter(Context context, @LayoutRes int layoutId, List<T> data) {
        this.mContext = context;
        this.layoutId = layoutId;
        this.mData = data == null ? new ArrayList<T>() : data;
    }

    public BaseAdapter(Context context, List<T> data) {
        this(context, 0, data);
    }

    public BaseAdapter(Context context) {
        this(context, 0, null);
    }

    public void addHeader(View header) {
        if (header == null) {
            throw new RuntimeException("header is null");
        }
        if (mHeaderViews.size() == 0) {
            mHeaderViews.add(header);
        }
        notifyDataSetChanged();
    }

    public void removeHeader() {
        if (mHeaderViews.size() != 0) {
            mHeaderViews.remove(0);
        }
        notifyDataSetChanged();
    }

    public void addFooter(View footer) {
        if (footer == null) {
            throw new RuntimeException("footer is null");
        }
        if (mFooterViews.size() == 0) {
            mFooterViews.add(footer);
        }
        notifyDataSetChanged();
    }

    public void removeFooter() {
        if (mFooterViews.size() != 0) {
            mFooterViews.remove(0);
        }
        notifyDataSetChanged();
    }
    public int getHeaderCount() {
        return mHeaderViews.size();
    }

    public int getFooterCount() {
        return mFooterViews.size();
    }

    public int getEmptyViewCount() {
        return mEmptyView == null ? 0 : 1;
    }

    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
    }

    /**
     * 默认的emptyView
     *
     * @param recyclerView 参数为需要添加的recyclerView
     */
    public void setEmptyView(RecyclerView recyclerView) {
        this.mEmptyView = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_empty, (ViewGroup) recyclerView.getParent(), false);
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    /**
     * 不好确定这个view只加一个，改为容器的话，层级过深
     * 后续处理
     *
     * @param parent
     * @param viewType
     * @return viewHolder
     */
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER_VIEW) {
            return BaseViewHolder.getViewHolder(parent, mHeaderViews.get(0));
        } else if (viewType == FOOTER_VIEW) {
            return BaseViewHolder.getViewHolder(parent, mFooterViews.get(0));
        } else if (viewType == EMPTY_VIEW) {
            return BaseViewHolder.getViewHolder(parent, mEmptyView);
        } else if (viewType == NORMAL_VIEW) {
            return BaseViewHolder.getViewHolder(parent, layoutId);
        } else {
            throw new RuntimeException("type error");
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemLongClickListener onItemLongClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position);
    }

    private OnItemChildClickListener onItemChildClickListener;

    /**
     * itemChild点击事件 需要配合BaseViewHolder里面的addOnClickListener使用
     *
     * @param onItemChildClickListener
     */
    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    public OnItemChildClickListener getOnItemChildClickListener() {
        return onItemChildClickListener;
    }

    public interface OnItemChildClickListener {
        void onItemChildClick(BaseAdapter adapter, View v, int position);
    }

    private OnItemChildLongClickListener onItemChildLongClickListener;

    /**
     * itemChildLong点击事件 需要配合BaseViewHolder里面的addOnLongClickListener使用
     *
     * @param onItemChildLongClickListener
     */
    public void setOnItemChildLongClickListener(OnItemChildLongClickListener onItemChildLongClickListener) {
        this.onItemChildLongClickListener = onItemChildLongClickListener;
    }

    public OnItemChildLongClickListener getOnItemChildLongClickListener() {
        return onItemChildLongClickListener;
    }

    public interface OnItemChildLongClickListener {
        boolean onItemChildLongClick(BaseAdapter adapter, View v, int position);
    }

    /**
     * item点击事件
     *
     * @param holder
     */
    private void initItemClick(final BaseViewHolder holder) {
        if (onItemClickListener != null) {
            holder.getItemView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition() - getHeaderCount();
                    if (holder.getAdapterPosition() != RecyclerView.NO_POSITION && position != -1 ) {
                        onItemClickListener.onItemClick(v, position);
                    }
                }
            });
        }
        if (onItemLongClickListener != null) {
            holder.getItemView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getAdapterPosition() - getHeaderCount();
                    return holder.getAdapterPosition() != RecyclerView.NO_POSITION && position != -1 && onItemLongClickListener.onItemLongClick(v, position);
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        int index;
        int type = getItemViewType(position);
        holder.setAdapter(this);
        if (type != HEADER_VIEW && type != FOOTER_VIEW && type != EMPTY_VIEW) {
//        if (type == NORMAL_VIEW) {
            index = position - getHeaderCount();
            if (mData.size() > index) {
                convert(holder, mData.get(index));
            } else {
                convert(holder);
            }
            initItemClick(holder);
        }
    }

    protected void convert(BaseViewHolder holder) {

    }

    protected abstract void convert(BaseViewHolder holder, T t);

    @Override
    public int getItemCount() {
        int count = mData.size() + getHeaderCount() + getFooterCount();
        if (count == 0) {
            count += getEmptyViewCount();
        }
        return count;
    }

    public void addAll(List<T> data) {
        int index = getItemCount() - 1 + getHeaderCount();
        this.mData.addAll(data);
        notifyItemRangeChanged(index, data.size());
    }

    public void setNewData(List<T> data) {
        if (data == null) {
            return;
        }
        this.mData = data;
        notifyDataSetChanged();
    }

    public void addData(int position, T item) {
        if (mData.size() > position) {
            int index = position + getHeaderCount();
            this.mData.add(position, item);
            notifyItemInserted(index);
        } else {
            addData(item);
        }
    }

    public void addData(T item) {
        this.mData.add(item);
        notifyItemInserted(getItemCount() - 1 - getFooterCount());
    }

    public void removeData(int position) {
        if (mData.size() > position) {
            int index = position + getHeaderCount();
            this.mData.remove(position);
            notifyItemRemoved(index);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mEmptyView != null && getItemCount() == 1 && mData.size() == 0) {
            return EMPTY_VIEW;
        } else if (mHeaderViews.size() != 0 && position < getHeaderCount()) {
            return HEADER_VIEW;
        } else if (mFooterViews.size() != 0 && position > getHeaderCount() + mData.size() - 1) {
            return FOOTER_VIEW;
        }
        return getItemDifViewType(position);
    }

    protected int getItemDifViewType(int position) {
        return NORMAL_VIEW;
    }

    protected void setFullSpan(BaseViewHolder holder, boolean fullSpan) {
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        if (fullSpan && params != null
                && params instanceof StaggeredGridLayoutManager.LayoutParams) {
            ((StaggeredGridLayoutManager.LayoutParams) params).setFullSpan(true);
        }
    }

    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int layoutPosition = holder.getAdapterPosition();
        int itemViewType = getItemViewType(layoutPosition);
        setFullSpan(holder, itemViewType == HEADER_VIEW || itemViewType == FOOTER_VIEW);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            Log.e(TAG, "onAttachedToRecyclerView: GridLayoutManager");
            final GridLayoutManager manager = (GridLayoutManager) layoutManager;
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int itemViewType = getItemViewType(position);
                    return (itemViewType == HEADER_VIEW ||
                            itemViewType == FOOTER_VIEW) ? manager.getSpanCount() : 1;
                }
            });
        }
        recyclerView.addOnScrollListener(mOnScrollListener);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        recyclerView.removeOnScrollListener(mOnScrollListener);
    }

    private boolean canScroll(RecyclerView recyclerView) {
        boolean canScroll = recyclerView.canScrollVertically(1);
        return !canScroll;
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE && canScroll(recyclerView)) {
                if (canLoad && mOnLoadMoreListener != null) {
                    canLoad = false;
                    mOnLoadMoreListener.loadMore();
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };
    private OnLoadMoreListener mOnLoadMoreListener;

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
        canLoad = true;
    }

    public interface OnLoadMoreListener {
        public void loadMore();
    }

    public void loadComplete() {
        canLoad = true;
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        int index = position - getHeaderCount();
        return mData.get(index);
    }

    private int recursiveExpand(int position, @NonNull List list) {
        int count = 0;
        int pos = position + list.size() - 1;
        for (int i = list.size() - 1; i >= 0; i--, pos--) {
            if (list.get(i) instanceof IExpandable) {
                IExpandable item = (IExpandable) list.get(i);
                if (item.isExpanded() && hasSubItems(item)) {
                    List subList = item.getSubItems();
                    mData.addAll(pos + 1, subList);
                    int subItemCount = recursiveExpand(pos + 1, subList);
                    count += subItemCount;
                }
            }
        }
        return count;
    }

    /**
     * Expand an expandable item
     *
     * @param position position of the item
     * @return the number of items that have been added.
     */
    public int expand(@IntRange(from = 0) int position) {
        T item = getItem(position);
        if (!isExpandable(item)) {
            return 0;
        }
        IExpandable expandable = (IExpandable) item;
        if (!hasSubItems(expandable)) {
            expandable.setExpanded(false);
            return 0;
        }
        int subItemCount = 0;
        if (!expandable.isExpanded()) {
            List list = expandable.getSubItems();
            mData.addAll(position + 1, list);
            subItemCount += recursiveExpand(position + 1, list);
            expandable.setExpanded(true);
            subItemCount += list.size();
        }
        notifyItemRangeInserted(position + 1, subItemCount);
        return subItemCount;
    }

    @SuppressWarnings("unchecked")
    private int recursiveCollapse(@IntRange(from = 0) int position) {
        T item = getItem(position);
        if (!isExpandable(item)) {
            return 0;
        }
        IExpandable expandable = (IExpandable) item;
        int subItemCount = 0;
        if (expandable.isExpanded()) {
            List<T> subItems = expandable.getSubItems();
            if (null == subItems) return 0;
            for (int i = subItems.size() - 1; i >= 0; i--) {
                T subItem = subItems.get(i);
                int pos = getItemPosition(subItem);
                if (pos < 0) {
                    continue;
                }
                if (subItem instanceof IExpandable) {
                    subItemCount += recursiveCollapse(pos);
                }
                mData.remove(pos);
                subItemCount++;
            }
        }
        return subItemCount;
    }

    /**
     * Collapse an expandable item that has been expanded..
     *
     * @param position the position of the item
     * @return the number of subItems collapsed.
     */
    public int collapse(@IntRange(from = 0) int position) {
        T item = getItem(position);
        if (!isExpandable(item)) {
            return 0;
        }
        IExpandable expandable = (IExpandable) item;
        int subItemCount = recursiveCollapse(position);
        expandable.setExpanded(false);
        notifyItemRangeRemoved(position + 1, subItemCount);
        return subItemCount;
    }

    private int getItemPosition(T item) {
        return item != null && mData != null && !mData.isEmpty() ? mData.indexOf(item) : -1;
    }

    private boolean hasSubItems(IExpandable item) {
        List list = item.getSubItems();
        return list != null && list.size() > 0;
    }

    private boolean isExpandable(T item) {
        return item != null && item instanceof IExpandable;
    }
}
