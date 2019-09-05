package com.netease.nim.uikit.business.session.module.list;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.emoji.MoonUtil;
import com.netease.nim.uikit.business.session.helper.TeamNotificationHelper;
import com.netease.nim.uikit.business.session.module.input.InputPanel;
import com.netease.nim.uikit.common.CommonUtil;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseFetchLoadAdapter;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * 消息回复
 */
public class ReplyMsgPrompt {
    // 底部新消息提示条
    private View newMessageTipLayout;
    private TextView account,content;
    private ImageView image,delete;

    private Context context;
    private View view;

    public ReplyMsgPrompt(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    // 显示底部新信息提示条
    public void show(IMMessage newMessage) {
        if (newMessageTipLayout == null) {
            init();
        }
        if (newMessage != null) {
            account.setText(newMessage.getFromNick() == null ? "" : newMessage.getFromNick());
            if (newMessage.getMsgType() == MsgTypeEnum.custom || newMessage.getMsgType() == MsgTypeEnum.text) {
                content.setVisibility(View.VISIBLE);
                image.setVisibility(View.GONE);
                content.setText(newMessage.getContent() == null ? newMessage.getPushContent() == null?"":newMessage.getPushContent() : newMessage.getContent());
            } else {
                content.setVisibility(View.GONE);
                image.setVisibility(View.VISIBLE);
                lxGlide(context,((FileAttachment) newMessage.getAttachment()).getUrl() == null ? "" :((FileAttachment) newMessage.getAttachment()).getUrl(),image);
            }
        }
        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputPanel.isReply = false;
                close();
            }
        });
        newMessageTipLayout.setVisibility(View.VISIBLE);
    }

    public void close () {
        if (newMessageTipLayout != null) {
            newMessageTipLayout.setVisibility(View.GONE);
        }
    }
    public static void lxGlide(Context context, String url, ImageView imageView) {
        String dealUrl = url.startsWith("http") ?url: CommonUtil.BaseUrl +url;
        Glide.with(context).load(dealUrl).into(imageView);
    }

    // 初始化底部新信息提示条
    private void init() {
        ViewGroup containerView = (ViewGroup) view.findViewById(R.id.message_activity_list_view_container);
        View.inflate(context, R.layout.nim_reply_message_layout, containerView);
        newMessageTipLayout = containerView.findViewById(R.id.new_message_tip_layout);
        account = (TextView) newMessageTipLayout.findViewById(R.id.msg_account);
        content = (TextView) newMessageTipLayout.findViewById(R.id.msg_content);
        image = newMessageTipLayout.findViewById(R.id.msg_image);
        delete = newMessageTipLayout.findViewById(R.id.msg_delete);
    }
}
