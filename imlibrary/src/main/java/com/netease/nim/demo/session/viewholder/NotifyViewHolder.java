package com.netease.nim.demo.session.viewholder;

import android.graphics.Color;
import android.widget.TextView;

import com.netease.nim.demo.R;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.impl.NimUIKitImpl;

/**
 * Created by mike on 2019/3/22.
 */

public class NotifyViewHolder extends MsgViewHolderBase {
    private TextView textView;

    public NotifyViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.view_notify;
    }

    @Override
    protected void inflateContentView() {
        textView = findViewById(R.id.text);
    }

    @Override
    protected void bindContentView() {
        if (isReceivedMessage()) {
            textView.setBackgroundResource(NimUIKitImpl.getOptions().messageLeftBackground);
            textView.setTextColor(Color.BLACK);
            textView.setPadding(ScreenUtil.dip2px(15), ScreenUtil.dip2px(8), ScreenUtil.dip2px(10), ScreenUtil.dip2px(8));
        } else {
            textView.setBackgroundResource(NimUIKitImpl.getOptions().messageRightBackground);
            textView.setTextColor(Color.WHITE);
            textView.setPadding(ScreenUtil.dip2px(10), ScreenUtil.dip2px(8), ScreenUtil.dip2px(15), ScreenUtil.dip2px(8));
        }
    }
}
