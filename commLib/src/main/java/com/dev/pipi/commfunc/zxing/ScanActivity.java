package com.dev.pipi.commfunc.zxing;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dev.pipi.commlib.R;
import com.dev.pipi.commlib.base.BaseActivity;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import pub.devrel.easypermissions.AfterPermissionGranted;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/05/15
 *     desc   :使用如下:
 *      new IntentIntegrator(getActivity())
            .setCaptureActivity(ScanActivity.class)
            .initiateScan(); // 初始化扫描
// Get the results:
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
    if(result != null) {
        if(result.getContents() == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
        }
    } else {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
 *     version: 1.0
 * </pre>
 */

public class ScanActivity extends BaseActivity implements DecoratedBarcodeView.TorchListener{
    private static final int RC_CAMERA_PERM = 515;
    DecoratedBarcodeView mDbv;
    TextView mTvTitle;
    Toolbar mToolbar;
    ImageButton mIvOpen;
    TextView mTvOpen;
    private CaptureManager captureManager;     //捕获管理器
    private Bundle savedInstanceState;
    private boolean isLightOn = false;

    @Override
    protected void init() {
        mDbv = findViewById(R.id.dbv);
        mTvTitle = findViewById(R.id.tv_title);
        mToolbar = findViewById(R.id.toolbar);
        mIvOpen = findViewById(R.id.iv_flash);
        mTvOpen = findViewById(R.id.tv_open);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.iconmback);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvTitle.setText(getString(R.string.routescan));
        mIvOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLightOn) {
                    mDbv.setTorchOff();
                    mIvOpen.setSelected(false);
                    mTvOpen.setText(getString(R.string.open_flashlight));
                } else {
                    mDbv.setTorchOn();
                    mIvOpen.setSelected(true);
                    mTvOpen.setText(getString(R.string.close_flashlight));
                }
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        startScan();
    }

    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void startScan() {
        if (hasPermission(Manifest.permission.CAMERA)) {
            mDbv.setTorchListener(this);
            if(!hasFlash()) {
                mIvOpen.setVisibility(View.GONE);
            }
            captureManager = new CaptureManager(this, mDbv);
            captureManager.initializeFromIntent(getIntent(), savedInstanceState);
            captureManager.decode();
        } else {
            requestPermissions(getString(R.string.permission_camera),RC_CAMERA_PERM, Manifest.permission.CAMERA);
        }
    }

    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scanner;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (captureManager != null) {
            captureManager.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (captureManager != null) {
            captureManager.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (captureManager != null) {
            captureManager.onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if (captureManager != null) {
            captureManager.onSaveInstanceState(outState);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mDbv.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPermissionsDenied() {
        super.onPermissionsDenied();
        finish();
    }

    @Override
    public void onTorchOn() {
        isLightOn = true;
    }

    @Override
    public void onTorchOff() {
        isLightOn = false;
    }
}