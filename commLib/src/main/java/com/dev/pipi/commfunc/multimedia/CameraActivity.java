package com.dev.pipi.commfunc.multimedia;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.view.View;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.listener.ClickListener;
import com.cjt2325.cameralibrary.listener.ErrorListener;
import com.cjt2325.cameralibrary.listener.JCameraListener;
import com.dev.pipi.commlib.R;
import com.dev.pipi.commlib.base.BaseActivity;
import com.dev.pipi.commlib.comm.EventCode;
import com.dev.pipi.commlib.comm.EventMsg;
import com.dev.pipi.commlib.comm.PermissionCode;
import com.dev.pipi.commlib.comm.Permissions;
import com.dev.pipi.commlib.util.EventBusUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/05/16
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CameraActivity extends BaseActivity {
    JCameraView mJcameraview;
    private File mParentFile;

    @Override
    protected void init() {
        initView();
        checkCameraPermission();
    }

    private void initView() {
        mJcameraview = findViewById(R.id.jcameraview);
        mJcameraview.setTip(getString(R.string.presslong_camera));
    }

    @AfterPermissionGranted(PermissionCode.PERMISSION_CODE_CAMERA)
    public void checkCameraPermission() {
        if (hasPermission(Permissions.PERMISSIONS_CAMERA)) {
            initCamera();
        } else {
            requestPermissions(getString(R.string.permission_camera), PermissionCode.PERMISSION_CODE_CAMERA,
                    Permissions.PERMISSIONS_CAMERA);
        }
    }

    @Override
    protected void onPermissionsDenied() {
        super.onPermissionsDenied();
        finish();
    }

    private void initCamera() {
        mParentFile = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "JCamera");
        FileUtils.createOrExistsDir(mParentFile);
        //设置视频保存路径
        mJcameraview.setSaveVideoPath(mParentFile.getPath());
        //设置只能录像或只能拍照或两种都可以（默认两种都可以）
        mJcameraview.setFeatures(JCameraView.BUTTON_STATE_ONLY_RECORDER);
        //设置视频质量
        mJcameraview.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE);
        //JCameraView监听
        mJcameraview.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {
                LogUtils.i("open camera error");
            }

            @Override
            public void AudioPermissionError() {
                LogUtils.i("AudioPermissionError");
            }
        });

        mJcameraview.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
                File file = new File(mParentFile, "IMG_" + timeStamp + ".jpg");
                byte[] bytes = ImageUtils.bitmap2Bytes(bitmap, Bitmap.CompressFormat.JPEG);
                FileIOUtils.writeFileFromBytesByStream(file, bytes);
                EventBusUtils.post(new EventMsg<>(EventCode.CAMERA_PHOTO_CODE,file.getAbsolutePath()));
                finish();
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                EventBusUtils.post(new EventMsg<>(EventCode.CAMERA_VIDEO_CODE, url));
                finish();
            }
        });
        //左边按钮点击事件
        mJcameraview.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                CameraActivity.this.finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setFullScreen();
    }

    public void setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_camera;
    }
}
