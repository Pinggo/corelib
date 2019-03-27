package com.dev.pipi.commlib.base.mvp;

import com.dev.pipi.commlib.base.container.ContainerFragment;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/09/03
 *     desc   : 带title的mvp基类fragment
 *     version: 1.0
 * </pre>
 */

public abstract class BaseTitleMvpFragment<V extends IView,T extends IPresenter<V>> extends ContainerFragment implements IView{
    protected T mBasePresenter;
    @Override
    protected void initFm() {
        mBasePresenter = getPresenter();
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
