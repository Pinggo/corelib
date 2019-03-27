package com.dev.pipi.commui.multiSelectDialog;

import android.content.Context;
import android.view.View;
import com.dev.pipi.commlib.R;
import com.dev.pipi.commlib.base.recyclerView.BaseAdapter;
import com.dev.pipi.commlib.base.recyclerView.BaseViewHolder;
import java.util.ArrayList;
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

public class LabelAdapter extends BaseAdapter<LabelData> {
    public LabelAdapter(Context context) {
        super(context, R.layout.item_label);
    }

    @Override
    public void convert(final BaseViewHolder holder, final LabelData labelData) {
        holder.setText(R.id.tv_name, labelData.getName());
        if (labelData.isSelect()) {
            holder.setBackgroundColor(R.id.rl, mContext.getResources().getColor(R.color.pink_light))
                    .setTextColor(R.id.tv_name, mContext.getResources().getColor(R.color.font_pink))
                    .setVisible(R.id.view_delete, true);
        } else {
            holder.setBackgroundColor(R.id.rl, mContext.getResources().getColor(R.color.gray))
                    .setTextColor(R.id.tv_name, mContext.getResources().getColor(R.color.font_gray))
                    .setVisible(R.id.view_delete, false);
        }
        holder.getView(R.id.rl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                labelData.setSelect(!labelData.isSelect());
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
    }

    /**
     *
     * @return 选中的列表
     */
    public List<String> getContent() {
        List<String> contents = new ArrayList<>();
        for (LabelData labelData : mData) {
            if (labelData.isSelect()) {
                contents.add(labelData.getName());
            }
        }
        return contents;
    }
}
