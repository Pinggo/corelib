package com.dev.pipi.commlib.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.text.TextUtils;

import com.blankj.utilcode.util.NetworkUtils;
import com.dev.pipi.commlib.util.NetUtil;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/04/08
 *     desc   :网络状态监听广播
 *     version: 1.0
 * </pre>
 */

public class NetWorkReceiver extends BroadcastReceiver {
    private NetWorkListener listener;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (TextUtils.equals(intent.getAction(), ConnectivityManager.CONNECTIVITY_ACTION) && listener != null) {
            listener.onNetWorkListener(NetUtil.getNetWorkState(context));
        }
    }

    public void setListener(NetWorkListener listener) {
        this.listener = listener;
    }

    public interface NetWorkListener{
        void onNetWorkListener(int netWorkType);
    }
}
