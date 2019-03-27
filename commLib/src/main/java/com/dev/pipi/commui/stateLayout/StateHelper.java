package com.dev.pipi.commui.stateLayout;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.View;

import com.dev.pipi.commlib.R;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/05/24
 *     desc   : 多状态切换管理
 *     version: 1.0
 * </pre>
 */

public class StateHelper {
    public static final int NO_LAYOUT_ID = 0;
    public static int BASE_LOADING_LAYOUT_ID = NO_LAYOUT_ID;
    public static int BASE_ERROR_LAYOUT_ID = NO_LAYOUT_ID;
    public static int BASE_EMPTY_LAYOUT_ID = NO_LAYOUT_ID;
    private StateLayout mStateLayout;
    private OnStateReloadListener mReloadListener;
    private StateHelper(Activity activity) {
        mStateLayout = StateLayout.wrap(activity);
    }
    private StateHelper(Fragment fragment) {
        mStateLayout = StateLayout.wrap(fragment);
    }
    private StateHelper(View view) {
        mStateLayout = StateLayout.wrap(view);
    }
    private StateHelper(Context context,@LayoutRes int layoutId) {
        mStateLayout = StateLayout.wrap(context,layoutId);
    }
    public static StateHelper wrap(Activity activity) {
        return new StateHelper(activity);
    }
    public static StateHelper wrap(Fragment fragment) {
        return new StateHelper(fragment);
    }
    public static StateHelper wrap(View view) {
        return new StateHelper(view);
    }
    public static StateHelper wrap(Context context,@LayoutRes int  layoutId) {
        return new StateHelper(context,layoutId);
    }
    /**
     * 用于还原视图
     * 解决viewpager和fragment中重复添加的问题,
     * 另外,fragment的rootView不复用也可以解决这个问题
     */
    public void detach() {
        if (mStateLayout == null) {
            return;
        }
        mStateLayout.detach();
    }

    public StateHelper setLoadingLayout(View view) {
        mStateLayout.setView(StateLayout.LOADING_VIEW,view);
        return this;
    }

    public StateHelper setLoadingLayout(@LayoutRes int layoutId) {
        mStateLayout.setView(StateLayout.LOADING_VIEW,layoutId);
        return this;
    }

    public StateHelper setEmptyLayout(View view) {
        mStateLayout.setView(StateLayout.EMPTYT_VIEW, view);
        return this;
    }

    public StateHelper setEmptyLayout(@LayoutRes int layoutId) {
        mStateLayout.setView(StateLayout.EMPTYT_VIEW,layoutId);
        return this;
    }
    public StateHelper setErrorLayout(View view) {
        mStateLayout.setView(StateLayout.ERROR_VIEW, view);
        return this;
    }

    public StateHelper setErrorLayout(@LayoutRes int layoutId) {
        mStateLayout.setView(StateLayout.ERROR_VIEW,layoutId);
        return this;
    }
    public StateHelper setContentLayout(View view) {
        mStateLayout.setView(StateLayout.CONTENT_VIEW, view);
        return this;
    }

    public StateHelper setContentLayout(@LayoutRes int layoutId) {
        mStateLayout.setView(StateLayout.CONTENT_VIEW,layoutId);
        return this;
    }

    public StateHelper showLoadingLayout() {
        mStateLayout.showView(StateLayout.LOADING_VIEW);
        return this;
    }
    public StateHelper showErrorLayout() {
        mStateLayout.showView(StateLayout.ERROR_VIEW);
        return this;
    }
    public StateHelper showEmptyLayout() {
        mStateLayout.showView(StateLayout.EMPTYT_VIEW);
        return this;
    }
    public StateHelper showContentLayout() {
        mStateLayout.showView(StateLayout.CONTENT_VIEW);
        return this;
    }

    /**
     * 显示添加的name的视图(setView中name一致)
     * @param name
     * @return this
     */
    public StateHelper showView(String name) {
        mStateLayout.showView(name);
        return this;
    }

    /**
     * 添加扩展视图
     * @param name
     * @param view
     * @return this
     */
    public StateHelper setView(String name, View view) {
        mStateLayout.setView(name, view);
        return this;
    }
    public StateHelper setView(String name,@LayoutRes int layoutId) {
        mStateLayout.setView(name, layoutId);
        return this;
    }

    public StateHelper setReloadListener(OnStateReloadListener reloadListener) {
        this.mReloadListener = reloadListener;
        View errorView = mStateLayout.getView(StateLayout.ERROR_VIEW);
        View view = errorView.findViewById(R.id.btn_reload);
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mReloadListener.reLoad();
                }
            });
        } else {
            mReloadListener.reLoad(mStateLayout.getView(StateLayout.ERROR_VIEW));
        }
        return this;
    }

    public StateLayout getStateLayout() {
        return mStateLayout;
    }

    public abstract static class OnStateReloadListener{
        protected abstract void reLoad();
        void reLoad(View view){

        }
    }
}
