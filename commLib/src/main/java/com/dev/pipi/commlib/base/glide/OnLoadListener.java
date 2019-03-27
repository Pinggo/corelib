package com.dev.pipi.commlib.base.glide;

import android.graphics.Bitmap;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/03/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */


interface OnLoadListener {
    /**
     * 加载失败
     */
    void onLoadFailed(Exception e, boolean isFirstResource);

    /**
     * 加载成功
     */
    void onLoadSuccess(Bitmap resource, boolean isFirstResource);
}
