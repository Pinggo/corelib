package com.dev.pipi.commui;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.dev.pipi.commlib.R;
import com.dev.pipi.commlib.base.container.ContainerFragment;
import com.dev.pipi.commlib.datahelper.net.BaseUrl;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/04/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class EditIpFragment extends ContainerFragment {
    EditText mEt;
    @Override
    protected void initFm() {
        mEt = mRootView.findViewById(R.id.et);
        String host = SPUtils.getInstance().getString(BaseUrl.IP);
        if (TextUtils.isEmpty(host)) {
            host = "";
        }
        mEt.setText(host);
        mEt.setSelection(host.length());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_operate, menu);
        MenuItem item = menu.findItem(R.id.menu_operate);
        TextView textView = item.getActionView().findViewById(R.id.tv);
        textView.setText(getString(R.string.save));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveIp();
            }
        });
    }

    private void saveIp() {
        String text = mEt.getText().toString().trim();
        if (!text.startsWith("http://")) {
            ToastUtils.showShort("地址以http开头");
            return;
        }
        if (!text.endsWith("/")) {
            ToastUtils.showShort("地址以/结尾...");
            return;
        }
        SPUtils.getInstance().put(BaseUrl.IP, text);
        back();
    }

    @NonNull
    @Override
    protected String getTitle() {
        return getString(R.string.config_ip);
    }



    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ip_edit;
    }

}
