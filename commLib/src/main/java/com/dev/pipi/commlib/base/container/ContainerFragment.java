package com.dev.pipi.commlib.base.container;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dev.pipi.commfunc.zxing.ToolUtils;
import com.dev.pipi.commlib.R;
import com.dev.pipi.commlib.base.BaseFragment;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/03/23
 *     desc   : 用于标准模式启动的Activity，不用注册Activity.
 *              带toolbar的基类,继承此类布局中必须包含layout_title
 *     version: 1.0
 * </pre>
 */

public abstract class ContainerFragment extends BaseFragment {
    protected Toolbar mToolbar;
    protected TextView mTitle;
    protected ActionBar mActionBar;
    protected Button btnRight;

    @Override
    protected void init() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity == null) {
            return;
        }
        mTitle = mRootView.findViewById(R.id.tv_title);
        mToolbar = mRootView.findViewById(R.id.toolbar);
        btnRight = mRootView.findViewById(R.id.btnRight);
        mToolbar.setTitle("");
        mTitle.setText(getTitle());
        activity.setSupportActionBar(mToolbar);
        setHasOptionsMenu(true);
        mActionBar = activity.getSupportActionBar();
        if (showNavigation()) {
            mToolbar.setNavigationIcon(getNavigationIcon());
        }
        setToolbarRight(null, null ,null);
        initFm();
    }

    /**
     * @param text
     * @param resid  drawable
     * @param btnClick
     */
    protected void setToolbarRight(String text,@Nullable Integer resid, View.OnClickListener btnClick){
        if(text!=null) {
            btnRight.setText(text);
        }
        if (resid != null) {
            btnRight.setBackgroundResource(resid.intValue());
            ViewGroup.LayoutParams linearParams = btnRight.getLayoutParams();
            linearParams.height= ToolUtils.dp2px(getActivity(),26);
            linearParams.width=ToolUtils.dp2px(getActivity(),26);
            btnRight.setLayoutParams(linearParams);
        }
        btnRight.setOnClickListener(btnClick);
    }


    protected int getNavigationIcon() {
        return R.mipmap.iconmback;
    }

    /**
     * 不需要返回键的界面重写此方法
     * @return true带返回键
     */
    protected boolean showNavigation() {
        return true;
    }
    protected abstract void initFm();
    @NonNull
    protected abstract String getTitle();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            back();
        }
        return true;
    }

}

