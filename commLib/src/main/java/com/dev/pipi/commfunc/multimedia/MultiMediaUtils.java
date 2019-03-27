package com.dev.pipi.commfunc.multimedia;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.dev.pipi.commlib.R;
import com.dev.pipi.commlib.comm.PermissionCode;
import com.dev.pipi.commlib.comm.Permissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.util.EnumSet;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/05/16
 *     desc   : 多媒体选择工具类
 *     version: 1.0
 * </pre>
 */

public class MultiMediaUtils {
    public static void takePhoto(@NonNull Fragment fragment,int count) {
        if (fragment.getContext() == null) {
            throw new RuntimeException("fragment's context is null");
        }
        if (EasyPermissions.hasPermissions(fragment.getContext(), Permissions.PERMISSIONS_PHOTO)) {
            Matisse.from(fragment)
//                    .choose(MimeType.ofImage(), true)
                    .choose(EnumSet.of(MimeType.JPEG, MimeType.PNG), true)
                    .countable(true)
                    .maxSelectable(count)
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .imageEngine(new CustmoerGlideEngine())
                    .theme(R.style.Matisse_Zhihu)
                    .forResult(PermissionCode.PERMISSION_CODE_PHOTO);
        } else {
            LogUtils.e("some necessary permissions are not been granted");
        }
    }
    public static void takePhoto(@NonNull Activity activity,int count) {
        if (EasyPermissions.hasPermissions(activity, Permissions.PERMISSIONS_PHOTO)) {
            Matisse.from(activity)
                    .choose(MimeType.ofImage(), true)
                    .countable(true)
                    .maxSelectable(count)
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f)
                    .imageEngine(new CustmoerGlideEngine())
                    .theme(R.style.Matisse_Zhihu)
                    .forResult(PermissionCode.PERMISSION_CODE_PHOTO);
        } else {
            LogUtils.e("some necessary permissions are not been granted");
        }
    }

    public static void startCamera() {
        ActivityUtils.startActivity(CameraActivity.class);
    }
}
