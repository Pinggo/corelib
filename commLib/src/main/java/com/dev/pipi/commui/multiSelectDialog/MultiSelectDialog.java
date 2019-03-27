package com.dev.pipi.commui.multiSelectDialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.dev.pipi.commlib.R;
import java.util.List;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/09/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class MultiSelectDialog extends DialogFragment implements View.OnClickListener {
    public static final String MULTI_DIALOG = "multi_dialog";
    private MultiSelectAdapter mAdapter;
    private NoticeDialogListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(true);
        return inflater.inflate(R.layout.fragment_dialog_multiselect, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        view.findViewById(R.id.btn_reset).setOnClickListener(this);
        view.findViewById(R.id.btn_finish).setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new MultiSelectAdapter(getContext());
        recyclerView.setAdapter(mAdapter);
        List<MultiSelectData> selectDatas = getArguments().getParcelableArrayList(MULTI_DIALOG);
        if (selectDatas != null && selectDatas.size() != 0) {
            mAdapter.setNewData(selectDatas);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public void onStart() {
        WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        layoutParams.width = metrics.widthPixels * 4 / 5;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setGravity(Gravity.END);
        getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                MultiSelectDialog.super.dismiss();
                if (mListener != null) {
                    mListener.onDismiss(mAdapter.getAllDatas());
                }
            }
        });
        super.onStart();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (manager != null && (getDialog() == null || !getDialog().isShowing())) {
            try {
                super.show(manager, tag);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void dismiss() {
        if (getDialog() != null) {
            super.dismiss();
        }
    }

    public void setListener(NoticeDialogListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_reset) {
            mAdapter.reset();
        } else if (v.getId() == R.id.btn_finish) {
            dismiss();
        }
    }
}
