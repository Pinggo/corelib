package com.dev.pipi.commlib.comm;

import android.Manifest;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/05/16
 *     desc   : 权限名称
 *     version: 1.0
 * </pre>
 */

public interface Permissions {
    String[] PERMISSIONS_PHOTO = {Manifest.permission.READ_EXTERNAL_STORAGE};
    String[] PERMISSIONS_CAMERA = {Manifest.permission.READ_EXTERNAL_STORAGE,
           Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};
    String[] PERMISSIONS_AUDIO = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
}
