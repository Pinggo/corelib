package com.dev.pipi.commui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.blankj.utilcode.util.ConvertUtils;
import com.dev.pipi.commlib.R;
import java.util.List;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/09/18
 *     desc   : 分段选择器
 *     version: 1.0
 * </pre>
 */

public class SegmentView extends RadioGroup {
    private int mRadius;
    private int mStrokeWidth;
    private int mHighlightColor;
    private String[] mItemString;
    private float mTextSize;
    private int mTextColorChecked;
    private int mTextColorUnChecked;
    private OnTabChangeListener mOnTabChangeListener;

    public SegmentView(Context context) {
        this(context, null);
    }

    public SegmentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SegmentView);
        mHighlightColor = array.getColor(R.styleable.SegmentView_seg_border_color, context.getResources().getColor(R.color.colorPrimary));
        mStrokeWidth = array.getDimensionPixelSize(R.styleable.SegmentView_seg_border_width, 1);
        mRadius = array.getDimensionPixelOffset(R.styleable.SegmentView_seg_radius, 6);
        mTextColorChecked = array.getColor(R.styleable.SegmentView_seg_textColor_checked, Color.WHITE);
        mTextColorUnChecked = array.getColor(R.styleable.SegmentView_seg_textColor_unchecked,Color.DKGRAY);
        mTextSize = array.getDimensionPixelSize(R.styleable.SegmentView_seg_textSize, 14);
        int id = array.getResourceId(R.styleable.SegmentView_seg_items, 0);
        array.recycle();
        mItemString = context.getResources().getStringArray(id);
        addChidView(context, attrs);
        updateChildBackgound();
        this.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                updateChildBackgound();
                if (mOnTabChangeListener != null) {
                    mOnTabChangeListener.onCheckedChanged(getRadioCheckedPosition(group));
                }
            }
        });
        ((RadioButton)getChildAt(0)).setChecked(true);
    }

    private void addChidView(Context context, AttributeSet attrs) {
        if (mItemString == null || mItemString.length == 0) {
            return;
        }
        for (int i = 0; i < mItemString.length; i++) {
            String text = mItemString[i];
            RadioButton button = new RadioButton(context, attrs);
            button.setGravity(Gravity.CENTER);
            button.setButtonDrawable(android.R.color.transparent);
            button.setText(text);
            button.setId(R.id.seg_id+i);
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);
            addView(button, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        }
    }

    private int getRadioCheckedPosition(RadioGroup radioGroup) {
        int position = 0;
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton rb = (RadioButton) radioGroup.getChildAt(i);
            if (rb.isChecked()) {
                position = i;
                break;
            }
        }
        return position;
    }

    private void updateChildBackgound() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) child;
                radioButton.setBackground(generateTabBackground(i, mHighlightColor));
                if (radioButton.isChecked()) {
                    radioButton.setTextColor(mTextColorChecked);
                } else {
                    radioButton.setTextColor(mTextColorUnChecked);
                }
            }
        }
    }

    private Drawable generateTabBackground(int position, int color) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_checked}, generateDrawable(position, color));
        stateListDrawable.addState(new int[]{}, generateDrawable(position, Color.TRANSPARENT));
        return stateListDrawable;
    }

    private Drawable generateDrawable(int position, int color) {
        int raduis = ConvertUtils.dp2px(mRadius);
        float[] radiusArray;
        if (position == 0) {
            radiusArray = new float[]{
                    raduis, raduis,
                    0, 0,
                    0, 0,
                    raduis, raduis
            };
        } else if (position == getChildCount() - 1) {
            radiusArray = new float[]{
                    0, 0,
                    raduis, raduis,
                    raduis, raduis,
                    0, 0
            };
        } else {
            radiusArray = new float[]{
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0
            };
        }
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadii(radiusArray);
        shape.setColor(color);
        shape.setStroke(ConvertUtils.dp2px(mStrokeWidth), mHighlightColor);
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{shape});
        if (position != getChildCount() - 1) {
            layerDrawable.setLayerInset(0,0,0,-raduis,0);
        }
        return layerDrawable;
    }

    public void setItems(List<String> items) {
        if (items == null || items.size() == 0) {
            return;
        }
        if (mItemString == null || mItemString.length == 0) {
            mItemString = new String[items.size()];
            mItemString = items.toArray(mItemString);
        }
        updateChildBackgound();
    }

    public void setOnTabChangeListener(OnTabChangeListener onTabChangeListener) {
        this.mOnTabChangeListener = onTabChangeListener;
    }

    public interface OnTabChangeListener {
        void onCheckedChanged(int index);
    }
}

