package com.dev.pipi.commfunc.zxing;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

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

public class ToolUtils {
    /**
     * dp2px
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px2dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    /**
     *根据设备信息获取当前分辨率下指定单位对应的像素大小；
     * px,dip,sp -> px
     */
    public float getRawSize(Context c, int unit, float size) {
        Resources r;
        if (c == null){
            r = Resources.getSystem();
        }else{
            r = c.getResources();
        }
        return TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
    }
}
