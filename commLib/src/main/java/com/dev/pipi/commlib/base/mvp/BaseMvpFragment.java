package com.dev.pipi.commlib.base.mvp;

import com.dev.pipi.commlib.base.BaseFragment;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/09/03
 *     desc   : mvp基类fragment
 *     version: 1.0
 * </pre>
 */

public abstract class BaseMvpFragment<V extends IView,T extends IPresenter<V>> extends BaseFragment implements IView{
    protected T mBasePresenter;
    @Override
    protected void init() {
        mBasePresenter = getPresenter();
        if (mBasePresenter == null) {
            throw new RuntimeException("presenter is null,you must initialization it");
        }
        mBasePresenter.attachView((V) this);
        initMvp();
    }

    protected abstract void initMvp();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBasePresenter != null) {
            mBasePresenter.detachView();
        }
    }

    protected abstract T getPresenter();

}
