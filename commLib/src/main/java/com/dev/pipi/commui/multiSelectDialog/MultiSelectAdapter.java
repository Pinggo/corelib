package com.dev.pipi.commui.multiSelectDialog;

import android.content.Context;
import com.dev.pipi.commlib.R;
import com.dev.pipi.commlib.base.recyclerView.BaseAdapter;
import com.dev.pipi.commlib.base.recyclerView.BaseViewHolder;
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

public class MultiSelectAdapter extends BaseAdapter<MultiSelectData> {
    public MultiSelectAdapter(Context context) {
        super(context, R.layout.item_multiselect);
    }

    @Override
    public void convert(BaseViewHolder holder, MultiSelectData multiSelectData) {
        LabelView labelView = holder.getView(R.id.labelView);
        labelView.setTitle(multiSelectData.getTitle());
        labelView.setDatas(multiSelectData.getLabelDatas());
        labelView.openItems();
    }

    public void reset() {
        for (MultiSelectData selectData : mData) {
            List<LabelData> labelDatas = selectData.getLabelDatas();
            int size = labelDatas.size();
            for (int i = 0; i < size; i++) {
                LabelData labelData = labelDatas.get(i);
                if (labelData.isSelect()) {
                    labelData.setSelect(false);
                }
            }
        }
        notifyDataSetChanged();
    }

    public List<MultiSelectData> getAllDatas() {
        return mData;
    }
}
