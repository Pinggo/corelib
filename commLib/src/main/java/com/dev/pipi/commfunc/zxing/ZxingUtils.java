package com.dev.pipi.commfunc.zxing;

import android.app.Activity;
import com.google.zxing.integration.android.IntentIntegrator;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/05/16
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class ZxingUtils {
    public static void startScan(Activity activity) {
        new IntentIntegrator(activity)
                .setCaptureActivity(ScanActivity.class)
                .initiateScan(); // 初始化扫描
    }
}
