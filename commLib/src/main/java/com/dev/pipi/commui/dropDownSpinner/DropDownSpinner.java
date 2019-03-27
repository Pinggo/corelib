package com.dev.pipi.commui.dropDownSpinner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.dev.pipi.commlib.R;
import com.dev.pipi.commlib.base.recyclerView.BaseAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/09/18
 *     desc   : 代替spinner控件
 *     version: 1.0
 * </pre>
 */

public class DropDownSpinner extends LinearLayout {
    private PopupWindow mPopupWindow;
    private TextView mTextView;
    private ImageView mImageView;
    private List<String> mData = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private SpinnerAdapter mAdapter;
    private static final String NODATA = "无数据";
    private ViewPropertyAnimator mPropertyAnimator;
    private int mSelectPostion;
    private boolean isHideArrow;
    private int mTextColor;
    private Drawable mArrowDrawable;
    private static final int DEFAULT_DURATION = 200;
    private static final int DEFAULT_COLOR = 0x8a000000;
    private static final int DEFAULT_TEXTSIZE = 15;
    private int mParentVerticalOffset;
    private int mTextSize;
    private int mDropBackgroundColor;
    private boolean isHideDiv;
    private boolean isLoadingData;

    public DropDownSpinner(Context context) {
        this(context, null);
    }

    public DropDownSpinner(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropDownSpinner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        View.inflate(context, R.layout.layout_spinner, this);
        mTextView = (TextView) findViewById(R.id.tv);
        mImageView = (ImageView) findViewById(R.id.iv);
        final View contentView = View.inflate(context, R.layout.layout_recyclerview, null);
        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DropDownSpinner);
        isHideArrow = ta.getBoolean(R.styleable.DropDownSpinner_hideArrow, false);
        isHideDiv = ta.getBoolean(R.styleable.DropDownSpinner_hideDiv, false);
        mTextColor = ta.getColor(R.styleable.DropDownSpinner_spinnerTextColor, DEFAULT_COLOR);
        mArrowDrawable = ta.getDrawable(R.styleable.DropDownSpinner_arrowDrawable);
        mTextSize = ta.getDimensionPixelSize(R.styleable.DropDownSpinner_spinnerTextSize, ConvertUtils.sp2px(DEFAULT_TEXTSIZE));
        mDropBackgroundColor = ta.getColor(R.styleable.DropDownSpinner_DropDownBackground, Color.WHITE);
        ta.recycle();
        mTextView.setTextColor(mTextColor);
        mTextView.setTextSize(ConvertUtils.px2sp(mTextSize));
        if (isHideArrow) {
            mImageView.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mTextView.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            mTextView.setLayoutParams(layoutParams);
        }
        if (mArrowDrawable != null) {
            mImageView.setImageDrawable(mArrowDrawable);
        }
        mPropertyAnimator = mImageView.animate().setDuration(DEFAULT_DURATION).setInterpolator(new LinearOutSlowInInterpolator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mPopupWindow = new PopupWindow(contentView);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindow.setElevation(16);
            mPopupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg_spinner_drop));
        } else {
            mPopupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg_spinner_drop));
        }
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                showArrow(false);
            }
        });
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoadingData) {
                    ToastUtils.showShort(context.getString(R.string.refreshing_tip));
                    return;
                }
                if (!mPopupWindow.isShowing()) {
                    showPopWindwow();
                } else {
                    closePopWindow();
                }
            }
        });
    }

    private int getDisplayHeight() {
        return getContext().getResources().getDisplayMetrics().heightPixels;
    }

    private int getParentVerticalOffset() {
      /*  if (mParentVerticalOffset > 0) {
            return mParentVerticalOffset;
        }*/
        int[] locationOnScreen = new int[2];
        getLocationOnScreen(locationOnScreen);
        mParentVerticalOffset = locationOnScreen[1];
        return mParentVerticalOffset;
    }
    private void measurePopUpDimension() {
        int widthSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(
                getDisplayHeight() - getParentVerticalOffset() - getMeasuredHeight(),
                MeasureSpec.AT_MOST);
        mRecyclerView.measure(widthSpec, heightSpec);
        mPopupWindow.setWidth(mRecyclerView.getMeasuredWidth());
        mPopupWindow.setHeight(mRecyclerView.getMeasuredHeight() - 10);
    }
    private void showArrow(boolean isDropDown) {
        if (isHideArrow) {
            return;
        }
        if (isDropDown) {
            mPropertyAnimator.rotation(180);
        } else {
            mPropertyAnimator.rotation(0);
        }
    }

    private void closePopWindow() {
        mPopupWindow.dismiss();
        showArrow(false);
    }

    private void showPopWindwow() {
        measurePopUpDimension();
        mPopupWindow.showAsDropDown(this);
        showArrow(true);
    }

    public void setAdapter(SpinnerAdapter adapter) {
        mAdapter = adapter;
        mAdapter.setBackgroundColor(mDropBackgroundColor);
        mAdapter.setTextColor(mTextColor);
        mAdapter.setTextSize(mTextSize);
        mAdapter.setHideDiv(isHideDiv);
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                closePopWindow();
                if (!getText().equals(mData.get(position))) {
                    setSelection(position);
                    if (mListener != null) {
                        mListener.onItemSelected(mData.get(position), position);
                    }
                }
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * 在setAdapter之后调用
     *
     * @param data
     */
    public void setData(List<String> data) {
        if (mAdapter == null) {
            setAdapter(new SpinnerAdapter(getContext()));
        }
        mData = data;
        mAdapter.setNewData(data);
        setSelection(0);
    }

    /**
     *
     * @param loadingData true则不显示popwindow
     */
    public void setLoadingData(boolean loadingData) {
        isLoadingData = loadingData;
    }

    public void setSelection(int position) {
        if (mData == null || mData.size() == 0) {
            throw new RuntimeException(NODATA);
        }
        mSelectPostion = position;
        mTextView.setText(mData.get(position));
    }

    public int getSelection() {
        return mSelectPostion;
    }

    public String getText() {
        return mTextView.getText().toString().trim();
    }
    private OnItemSelectedListener mListener;

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.mListener = onItemSelectedListener;
    }

    public interface OnItemSelectedListener {
        void onItemSelected(String text, int position);
    }
}
