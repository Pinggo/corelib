package com.dev.pipi.commlib.base.mvp;

import com.dev.pipi.commlib.base.BaseActivity;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/09/03
 *     desc   : mvp基类activity
 *     version: 1.0
 * </pre>
 */

public abstract class BaseMvpActivity<V extends IView,T extends IPresenter<V>> extends BaseActivity implements IView{
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

    protected abstract T getPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBasePresenter != null) {
            mBasePresenter.detachView();
        }
    }
}
