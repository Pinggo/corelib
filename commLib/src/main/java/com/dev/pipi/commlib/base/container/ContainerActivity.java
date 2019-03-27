package com.dev.pipi.commlib.base.container;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.dev.pipi.commlib.R;
import com.dev.pipi.commlib.base.BaseActivity;
import com.dev.pipi.commlib.comm.EventCode;
import com.dev.pipi.commlib.comm.EventMsg;
import com.dev.pipi.commlib.comm.FmManager;
import com.dev.pipi.commlib.util.EventBusUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/03/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class ContainerActivity extends BaseActivity {
    @Override
    protected void init() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(FmManager.BUNDLE_DATA);
        String fragmentName = bundle.getString(FmManager.FRAGMENT_NAME);
        Fragment fragment = FmManager.getFragmentByName(fragmentName);
        fragment.setArguments(bundle);
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.llContainer, fragment);
        transaction.commit();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void back() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        if (!fm.popBackStackImmediate()) {
            finish();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_container;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() != null) {
                EventBusUtils.post(new EventMsg<>(EventCode.ZXING_CODE,result.getContents()));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
