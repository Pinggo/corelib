package com.dev.pipi.commui.multiSelectDialog;

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

public interface NoticeDialogListener {
    /**
     * @param multiSelectData   选择后的数据
     */
    void onDismiss(List<MultiSelectData> multiSelectData);
}
