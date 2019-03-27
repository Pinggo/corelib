package com.dev.pipi.commlib.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.dev.pipi.commlib.receiver.NetWorkReceiver;
import com.dev.pipi.commlib.util.NetUtil;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/05/15
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class NetWorkService extends Service {
    private NetWorkReceiver netWorkReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        netWorkReceiver = new NetWorkReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkReceiver, filter);
        netWorkReceiver.setListener(new NetWorkReceiver.NetWorkListener() {
            @Override
            public void onNetWorkListener(int netWorkType) {
                if (netWorkType == NetUtil.NETWORK_NONE) {
                    ToastUtils.showShort("网络断开,请检查网络连接!");
                }
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(netWorkReceiver);
    }
}
