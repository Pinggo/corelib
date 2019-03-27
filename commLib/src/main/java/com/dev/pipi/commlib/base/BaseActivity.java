package com.dev.pipi.commlib.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.dev.pipi.commlib.R;
import com.dev.pipi.commlib.service.NetWorkService;
import com.dev.pipi.commlib.util.PermissionUtils;
import com.dev.pipi.commui.CircleDialog;
import com.dev.pipi.commui.CustomerDialog;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/03/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public abstract class BaseActivity extends RxAppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private CustomerDialog mCustomerDialog;
    private CircleDialog mCircleDialog;
    protected View mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        doSupportImmersion(isSupportImmersion());
        mView = View.inflate(this, getLayoutId(), null);
        setContentView(mView);
        startService(new Intent(this, NetWorkService.class));
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 支持沉浸式(布局延伸到状态栏sdk>21--->>5.0)需重写此方法
     *
     * @return true 支持沉浸式
     */
    protected boolean isSupportImmersion() {
        return false;
    }

    private void doSupportImmersion(boolean isImmersion) {
        if (!isImmersion) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    protected abstract void init();

    protected abstract int getLayoutId();

    protected void showCustomerLoading(String content) {
        if (mCustomerDialog == null) {
            mCustomerDialog = new CustomerDialog();
            if (!TextUtils.isEmpty(content)) {
                mCircleDialog.setContent(content);
            }
        }
        if (!mCustomerDialog.isAdded()) {
            mCustomerDialog.show(getSupportFragmentManager(), BaseActivity.class.getSimpleName());
        }
    }

    protected void showCustomerLoading() {
        showCustomerLoading("");
    }

    protected void hideCustomerLoading() {
        if (mCustomerDialog != null && mCustomerDialog.isAdded()) {
            mCustomerDialog.dismiss();
        }
    }

    protected void showCircleLoading(String content) {
        if (mCircleDialog == null) {
            mCircleDialog = new CircleDialog();
            if (!TextUtils.isEmpty(content)) {
                mCircleDialog.setContent(content);
            }
        }
        if (!mCircleDialog.isAdded()) {
            mCircleDialog.show(getSupportFragmentManager(), BaseActivity.class.getSimpleName());
        }
    }

    protected void showCircleLoading() {
        showCircleLoading("");
    }

    protected void hideCircleLoading() {
        if (mCircleDialog != null && mCircleDialog.isAdded()) {
            mCircleDialog.dismiss();
        }
    }

    //权限处理 AfterPermissionGranted中一定要做处理
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    /**
     * 此方法为了解决easypermission申请多条时的权限bug
     * 申请多条时权限1同意,权限2拒绝.再次请求时如果权限1点拒绝就会崩溃
     * 这时只需要申请权限2
     * 以下参数同EasyPermissions
     *
     * @param rationale
     * @param requestCode
     * @param perms
     */
    protected void requestPermissions(@NonNull String rationale,
                                      int requestCode, @NonNull String... perms) {
        PermissionUtils.requestPermissions(this, rationale, requestCode, perms);
    }

    protected boolean hasPermission(@NonNull String... perms) {
        return EasyPermissions.hasPermissions(this, perms);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder builder = new AppSettingsDialog.Builder(this);
            AppSettingsDialog dialog = builder.setTitle(getString(R.string.request_permission))
                    .setRationale(getString(R.string.permissions_requests))
                    .build();
            dialog.show();
        } else {
            Toast.makeText(this, getString(R.string.requests_deny), Toast.LENGTH_SHORT).show();
            onPermissionsDenied();
        }
    }

    protected void onPermissionsDenied() {

    }
}
