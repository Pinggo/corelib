package com.dev.pipi.commui.multiSelectDialog;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class LabelView extends LinearLayout implements View.OnClickListener {
    private TextView mTvName;
    private ImageView mIvDown;
    private RecyclerView mRecyclerView;
    private boolean isOpen;
    private LabelAdapter mAdapter;
    public LabelView(Context context) {
        this(context,null);
    }

    public LabelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LabelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View.inflate(context, R.layout.layout_label, this);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mIvDown = (ImageView) findViewById(R.id.iv_down);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mAdapter = new LabelAdapter(context);
        mRecyclerView.setAdapter(mAdapter);
        findViewById(R.id.rl).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl) {
            if (!isOpen) {
                openItems();
            } else {
                closeItems();
            }
        }
    }


    /**
     * 关闭选项
     */
    private void closeItems() {
        isOpen = false;
        mIvDown.animate().rotation(0);
        mRecyclerView.setVisibility(View.GONE);
    }

    /**
     * 打开选项
     */
    public void openItems() {
        isOpen = true;
        mIvDown.animate().rotation(180);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public void setTitle(String title) {
        mTvName.setText(title);
    }

    public void setDatas(List<LabelData> datas) {
        mAdapter.setNewData(datas);
    }

    /**
     *
     * @return 选中的列表
     */
    public List<String> getContent() {
        return mAdapter.getContent();
    }
}
