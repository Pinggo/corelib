package com.dev.pipi.commlib.util;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/05/16
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class PermissionUtils {
    /**
     * 此方法为了解决easypermission申请多条时的权限bug
     * 申请多条时权限1同意,权限2拒绝.再次请求时如果权限1点拒绝就会崩溃
     * 这时只需要申请权限2
     * 以下参数同EasyPermissions
     *
     * @param fragment
     * @param rationale
     * @param requestCode
     * @param perms
     */
    public static void requestPermissions(@NonNull Fragment fragment, @NonNull String rationale,
                                          int requestCode, @NonNull String... perms) {
        if (fragment.getContext() == null) {
            throw new RuntimeException("fragment's context is null");
        }
        if (perms.length > 1) {
            List<String> permissions = new ArrayList<>();
            for (String permisson : perms) {
                if (!EasyPermissions.hasPermissions(fragment.getContext(), permisson)) {
                    permissions.add(permisson);
                }
            }
            EasyPermissions.requestPermissions(fragment, rationale,
                    requestCode, permissions.toArray(new String[permissions.size()]));
        } else {
            EasyPermissions.requestPermissions(fragment, rationale,
                    requestCode, perms);
        }
    }

    /**
     * 此方法为了解决easypermission申请多条时的权限bug
     * 申请多条时权限1同意,权限2拒绝.再次请求时如果权限1点拒绝就会崩溃
     * 这时只需要申请权限2
     * 以下参数同EasyPermissions
     *
     * @param activity
     * @param rationale
     * @param requestCode
     * @param perms
     */
    public static void requestPermissions(@NonNull AppCompatActivity activity, @NonNull String rationale,
                                          int requestCode, @NonNull String... perms) {
        if (perms.length > 1) {
            List<String> permissions = new ArrayList<>();
            for (String permisson : perms) {
                if (!EasyPermissions.hasPermissions(activity, permisson)) {
                    permissions.add(permisson);
                }
            }
            EasyPermissions.requestPermissions(activity, rationale,
                    requestCode, permissions.toArray(new String[permissions.size()]));
        } else {
            EasyPermissions.requestPermissions(activity, rationale,
                    requestCode, perms);
        }
    }
}
