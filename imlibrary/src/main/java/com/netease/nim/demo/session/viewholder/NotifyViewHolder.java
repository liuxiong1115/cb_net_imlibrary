package com.netease.nim.demo.session.viewholder;

import com.netease.nim.demo.R;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;

/**
 * Created by mike on 2019/3/22.
 */

public class NotifyViewHolder extends MsgViewHolderBase {
    public NotifyViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.view_notify;
    }

    @Override
    protected void inflateContentView() {

    }

    @Override
    protected void bindContentView() {

    }
}
