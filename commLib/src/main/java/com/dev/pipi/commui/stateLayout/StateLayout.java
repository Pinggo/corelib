package com.dev.pipi.commui.stateLayout;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/05/22
 *     desc   : 多状态管理页面
 *     version: 1.0
 * </pre>
 */

public class StateLayout extends FrameLayout{
    private static final String TAG = StateLayout.class.getSimpleName();
    private Context mContext;
    private Map<String,View> mChildViews = new HashMap<>();
    public static final String CONTENT_VIEW = "content_view";
    public static final String EMPTYT_VIEW = "emptyt_view";
    public static final String ERROR_VIEW = "error_view";
    public static final String LOADING_VIEW = "loading_view";
    private List<String> mChidNames = new ArrayList<>();
    private int index;

    private StateLayout(@NonNull Context context) {
        this(context ,null);
    }

    private StateLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs ,0);
    }

    private StateLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
    /**
     * 控制页面的显示与否
     * @param name view的name
     */
    private void showViewInner(String name) {
        if (mChidNames.contains(name)) {
            for (String chidName : mChidNames) {
                View childView = mChildViews.get(chidName);
                childView.setVisibility(View.GONE);
            }
            View view = mChildViews.get(name);
            view.setVisibility(View.VISIBLE);
        }
    }

    public void showView(final String name) {
        if (isMainThread()) {
            showViewInner(name);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    showViewInner(name);
                }
            });
        }
    }

    public void setView(String name,@LayoutRes int layoutId) {
        if (layoutId == StateHelper.NO_LAYOUT_ID) {
            return;
        }
        View view = View.inflate(mContext, layoutId, null);
        setView(name,view);
    }

    public void setView(String name, View view) {
        if (view == null) {
            return;
        }
        if (!mChidNames.contains(name)) {
            mChidNames.add(name);
        }
        View chidView = mChildViews.get(name);
        if (chidView != null) {
            Log.w(TAG, "you have already set"+name+"!will replace"+name+"?");
        }
        removeView(chidView);
        addView(view);
        mChildViews.put(name, view);
    }

    public View getView(String name) {
        if (mChidNames.contains(name)) {
            return mChildViews.get(name);
        }
        throw new RuntimeException(name +"is not exist!");
    }

    /**
     * 用于还原视图
     * 解决viewpager和fragment中重复添加的问题,
     * 另外,fragment的rootView不复用也可以解决这个问题
     */
    public void detach() {
        ViewGroup parentView = (ViewGroup) this.getParent();
        ViewGroup.LayoutParams lp = this.getLayoutParams();
        View contentView = getView(CONTENT_VIEW);
        this.removeView(contentView);
        parentView.removeView(this);
        parentView.addView(contentView,index,lp);
    }
    public static StateLayout wrap(View view) {
        if (view == null) {
            throw new RuntimeException("view can not be null");
        }
        ViewGroup parent = (ViewGroup)view.getParent();
        if (parent == null) {
            throw new RuntimeException("parent view can not be null");
        }
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        int index = parent.indexOfChild(view);
        parent.removeView(view);
        StateLayout layout = new StateLayout(view.getContext());
        layout.setIndex(index);
        parent.addView(layout, index, lp);
        layout.setView(CONTENT_VIEW,view);
        layout.setView(EMPTYT_VIEW, StateHelper.BASE_EMPTY_LAYOUT_ID);
        layout.setView(ERROR_VIEW, StateHelper.BASE_ERROR_LAYOUT_ID);
        layout.setView(LOADING_VIEW, StateHelper.BASE_LOADING_LAYOUT_ID);
        layout.showView(CONTENT_VIEW);
        return layout;
    }

    public static StateLayout wrap(Context context,@LayoutRes int layoutId) {
        View view = View.inflate(context, layoutId, null);
        return wrap(view);
    }

    public static StateLayout wrap(Activity activity) {
        return wrap(((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0));
    }
    public static StateLayout wrap(Fragment fragment) {
        return wrap(fragment.getView());
    }

}
