package com.dev.pipi.commlib.comm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.dev.pipi.commlib.base.container.ContainerActivity;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/03/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class FmManager {
    public static final String FRAGMENT_NAME = "fragment_name";
    public static final String BUNDLE_DATA = "bundle_data";

    /**
     * 跳转到ContainerActivity
     * @param context
     * @param destName 目的fragment的全类名
     */
    public static void startAct(Context context, String destName) {
        startAct(context,destName,null);
    }

    /**
     *
     * 跳转到ContainerActivity
     * @param context
     * @param destName    目的fragment的全类名
     * @param bundle      携带的数据
     */
    public static void startAct(Context context, String destName, Bundle bundle) {
        startAct(context, destName, bundle, ContainerActivity.class);
    }
    /**
     * @param context
     * @param destName    目的fragment的全类名
     * @param bundle      携带的数据
     * @param clazz       目标类名
     */
    private static void startAct(Context context, String destName, Bundle bundle, Class clazz) {
        Intent intent = new Intent();
        intent.setClass(context, clazz);
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString(FRAGMENT_NAME, destName);
        intent.putExtra(BUNDLE_DATA, bundle);
        context.startActivity(intent);
    }
    /**
     * 根据类名取fragment
     * @param fragmentName
     * @return fragment
     */
    public static Fragment getFragmentByName(String fragmentName) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) Class.forName(fragmentName).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fragment;
    }
}
