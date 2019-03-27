package com.dev.pipi.commui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.pipi.commlib.R;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/05/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class CustomerDialog extends DialogFragment {
    public static final String DATA ="data";
    private TextView textView;
    private boolean isCancelOutside = true;
    private static final String LOADING = "加载中...";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (getDialog() != null) {
            getDialog().setCanceledOnTouchOutside(isCancelOutside);
        }
        return inflater.inflate(R.layout.layout_custom_dialog, container, false);
    }
    public void setCancelOutside(boolean isCancelOutside) {
        this.isCancelOutside = isCancelOutside;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = (TextView) view.findViewById(R.id.tv);
        String content = LOADING;
        if (getArguments() != null) {
            String data = getArguments().getString(DATA);
            if (!TextUtils.isEmpty(data)) {
                content = data;
            }
        }
        textView.setText(content);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, R.style.CustomerDialogStyle);
    }

    public void setContent(String content) {
        Bundle bundle = new Bundle();
        bundle.putString(DATA, content);
        setArguments(bundle);
    }

    @Override
    public void onStart() {
        /*final WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        layoutParams.height = layoutParams.width;*/
        if (getDialog() != null) {
            getDialog().getWindow().setGravity(Gravity.CENTER);
            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_customer_white);
            getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    CustomerDialog.super.dismiss();
                }
            });
        }
        super.onStart();
    }
    @Override
    public void dismiss() {
        if (getDialog() != null) {
            try {
                if (isAdded()) {
                    super.dismiss();
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void show(FragmentManager manager, String tag) {
        if (manager != null && (getDialog() == null || !getDialog().isShowing())) {
            try {
                manager.executePendingTransactions();
                if (!this.isAdded()) {
                    super.show(manager, tag);
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }
}

