package com.dev.pipi.commui.dropDownSpinner;

import android.content.Context;
import android.widget.TextView;
import com.blankj.utilcode.util.ConvertUtils;
import com.dev.pipi.commlib.R;
import com.dev.pipi.commlib.base.recyclerView.BaseAdapter;
import com.dev.pipi.commlib.base.recyclerView.BaseViewHolder;


/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/09/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class SpinnerAdapter extends BaseAdapter<String> {

    private int textColor;
    private int textSize;
    private int backgroundColor;
    private boolean isHideDiv;
    public SpinnerAdapter(Context context) {
        super(context, R.layout.item_spinner);
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setHideDiv(boolean hideDiv) {
        isHideDiv = hideDiv;
    }

    @Override
    public void convert(final BaseViewHolder holder, String s) {
        holder.setVisible(R.id.view_div, holder.getAdapterPosition() != getItemCount() - 1);
        if (isHideDiv) {
            holder.getView(R.id.view_div).setBackgroundColor(backgroundColor);
        }
        TextView tv = holder.getView(R.id.tv);
        tv.setTextSize(ConvertUtils.px2sp(textSize));
        tv.setText(s);
        tv.setTextColor(textColor);
        holder.getItemView().setBackgroundColor(backgroundColor);
    }
}
