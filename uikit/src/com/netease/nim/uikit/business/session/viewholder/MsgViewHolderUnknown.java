package com.netease.nim.uikit.business.session.viewholder;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

import org.w3c.dom.Text;

/**
 * Created by zhoujianghua on 2015/8/6.
 */
public class MsgViewHolderUnknown extends MsgViewHolderBase {

    TextView title,content;
    ImageView imageView;
    LinearLayout layout;
    public MsgViewHolderUnknown(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_unknown;
    }

    @Override
    protected boolean isShowHeadImage() {
        if (message.getSessionType() == SessionTypeEnum.ChatRoom) {
            return false;
        }
        return true;
    }

    @Override
    protected void inflateContentView() {
        title = findViewById(R.id.message_item_unsupport_title);
        content = findViewById(R.id.message_item_unsupport_desc);
        imageView = findViewById(R.id.message_item_unsupport_image);
        layout = findViewById(R.id.message_item_unsupport_container);
    }

    @Override
    protected void bindContentView() {
        message.getAttachment();
    }
}
