package com.netease.nim.uikit.business.session.viewholder;

import android.text.TextUtils;
import android.util.Log;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.activity.WatchMessagePictureActivity;
import com.netease.nim.uikit.common.CommonUtil;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.util.log.sdk.util.FileUtils;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;

import java.util.Map;

/**
 * Created by zhoujianghua on 2015/8/4.
 */
public class MsgViewHolderPicture extends MsgViewHolderThumbBase {

    public MsgViewHolderPicture(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        CommonUtil.setonDealImageMediaUrlListener(new CommonUtil.onDealImageMediaUrlListener() {
            @Override
            public void onDealImageMediaUrl() {
                WatchMessagePictureActivity.start(context, message);
            }
        });
        return R.layout.nim_message_item_picture;
    }

    @Override
    protected void onItemClick() {

       if (CommonUtil.role == CommonUtil.SELLER) {
           Map<String, Object> map = message.getRemoteExtension();
           if (map != null) {
               String wxMsgId = (String) map.get("wxMsgId");
               if (!TextUtils.isEmpty(wxMsgId)) {
                       CommonUtil.onGetMediaUrlListener onGetMediaUrlListener = CommonUtil.getMediaUrlListener;
                       if (onGetMediaUrlListener != null) {
                           onGetMediaUrlListener.onMediaUrl(message,context,wxMsgId);
                           return;
                       }
               }
           }
           WatchMessagePictureActivity.start(context, message);
       } else {
           WatchMessagePictureActivity.start(context, message);
       }

    }

    @Override
    protected String thumbFromSourceFile(String path) {
        return path;
    }
}
