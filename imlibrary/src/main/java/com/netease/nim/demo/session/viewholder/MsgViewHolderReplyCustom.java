package com.netease.nim.demo.session.viewholder;

import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.netease.nim.demo.R;
import com.netease.nim.demo.session.extension.CustomAttachmentType;
import com.netease.nim.demo.session.extension.DefaultCustomAttachment;
import com.netease.nim.demo.session.extension.GuessAttachment;
import com.netease.nim.demo.session.extension.ReplyAttachment;
import com.netease.nim.demo.session.utils.GlideUtils;
import com.netease.nim.uikit.business.session.emoji.MoonUtil;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.CommonUtil;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

import java.io.File;

/**
 * Created by zhoujianghua on 2015/8/4.
 */
public class MsgViewHolderReplyCustom extends MsgViewHolderBase {
    TextView account, replyAccount, content;   //自定义标题/内容
    ImageView imageView;  //图片
    LinearLayout layout;

    public MsgViewHolderReplyCustom(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }


    @Override
    protected int getContentResId() {
        return R.layout.message_itme_reply;
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
        replyAccount = findViewById(R.id.reply_account);
        account = findViewById(R.id.account);
        content = findViewById(R.id.reply_desc);
        imageView = findViewById(R.id.reply_image);
        layout = findViewById(R.id.message_container);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick();
            }
        });
    }

    @Override
    protected void bindContentView() {
        final ReplyAttachment attachment = (ReplyAttachment) message.getAttachment();
        //被回复人名称
        replyAccount.setText(attachment.getReplyAccount() == null ? "" : attachment.getReplyAccount());
        //图片或者是文本
        if (attachment.getMsgType() != null) {
            if (attachment.getMsgType().equals("text") || attachment.getMsgType().equals("custom")) {
                imageView.setVisibility(View.GONE);
                content.setVisibility(View.VISIBLE);
                content.setText(attachment.getReplyContent() == null ? "" : attachment.getReplyContent());
            } else {
                content.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                GlideUtils.lxGlide(context, attachment.getUrl() == null?"":attachment.getUrl(), imageView);
            }
        }
        //回复消息
        account.setText(attachment.getContent() == null ? "" : attachment.getContent());
        //是否是接收消息
        if (isReceivedMessage()) {
            layout.setBackgroundResource(NimUIKitImpl.getOptions().messageLeftBackground);
            layout.setPadding(ScreenUtil.dip2px(15), ScreenUtil.dip2px(8), ScreenUtil.dip2px(10), ScreenUtil.dip2px(8));
        } else {
            layout.setBackgroundResource(NimUIKitImpl.getOptions().messageRightWhiteBackground);
            layout.setPadding(ScreenUtil.dip2px(10), ScreenUtil.dip2px(8), ScreenUtil.dip2px(15), ScreenUtil.dip2px(8));
        }
    }

    @Override
    protected int rightBackground() {
        return 0;
    }
}
