package com.netease.nim.uikit.business.session.viewholder;

import android.text.TextUtils;
import android.util.Log;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.activity.WatchVideoActivity;
import com.netease.nim.uikit.common.CommonUtil;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.util.log.sdk.util.FileUtils;
import com.netease.nim.uikit.common.util.media.BitmapDecoder;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.attachment.VideoAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.Map;

/**
 * Created by zhoujianghua on 2015/8/5.
 */
public class MsgViewHolderVideo extends MsgViewHolderThumbBase {

    public MsgViewHolderVideo(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);

    }

    @Override
    protected int getContentResId() {
        CommonUtil.setonDealVideoMediaUrlListener(new CommonUtil.onDealVideoMediaUrlListener() {
            @Override
            public void onDealVideoMediaUrl(IMMessage imMessage) {
                message = imMessage;
                WatchVideoActivity.start(context, message);
            }
        });
        return R.layout.nim_message_item_video;
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
                        onGetMediaUrlListener.onMediaUrl(message, context, wxMsgId);
                        return;

                    }
                }
            }
            WatchVideoActivity.start(context, message);
        } else {
            WatchVideoActivity.start(context, message);
        }
    }

    @Override
    protected String thumbFromSourceFile(String path) {
        VideoAttachment attachment = (VideoAttachment) message.getAttachment();
        String thumb = attachment.getThumbPathForSave();
        return BitmapDecoder.extractThumbnail(path, thumb) ? thumb : null;
    }
}
