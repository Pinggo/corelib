package com.dev.pipi.commui;

import android.animation.ObjectAnimator;
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
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.pipi.commlib.R;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/06/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CircleDialog extends DialogFragment {
    public static final String DATA = "data";
    private TextView textView;
    private ImageView imageView;
    private boolean isCancelOutside = true;
    private static final String LOADING = "加载中...";
    private ObjectAnimator rotationAnim;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(isCancelOutside);
        return inflater.inflate(R.layout.layout_circle_dialog, container, false);
    }

    public void setCancelOutside(boolean isCancelOutside) {
        this.isCancelOutside = isCancelOutside;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.tv);
        imageView = view.findViewById(R.id.iv);
        String content = LOADING;
        if (getArguments() != null) {
            String data = getArguments().getString(DATA);
            if (!TextUtils.isEmpty(data)) {
                content = data;
            }
        }
        textView.setText(content);
        showAnim();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //兼容低版本
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
        getDialog().getWindow().setGravity(Gravity.CENTER);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_customer_white);
        getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                CircleDialog.super.dismiss();
            }
        });
        super.onStart();
    }

    @Override
    public void dismiss() {
        if (getDialog() != null) {
            try {
                if (isAdded()) {
                    super.dismiss();
                    rotationAnim.cancel();
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

    private void showAnim() {
        rotationAnim = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 359f);
        rotationAnim.setRepeatCount(ObjectAnimator.INFINITE);
        rotationAnim.setInterpolator(new LinearInterpolator());
        rotationAnim.setDuration(1500);
        rotationAnim.start();
    }
}