package com.netease.nim.demo.session.viewholder;

import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.netease.nim.demo.R;
import com.netease.nim.demo.file.FileIcons;
import com.netease.nim.demo.session.activity.FileDownloadActivity;
import com.netease.nim.demo.session.extension.CustomAttachmentType;
import com.netease.nim.demo.session.extension.DefaultCustomAttachment;
import com.netease.nim.demo.session.extension.GuessAttachment;
import com.netease.nim.demo.session.extension.ReplyAttachment;
import com.netease.nim.demo.session.extension.StickerAttachment;
import com.netease.nim.demo.session.utils.GlideUtils;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.session.activity.WatchVideoActivity;
import com.netease.nim.uikit.business.session.emoji.MoonUtil;
import com.netease.nim.uikit.business.session.emoji.StickerManager;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.CommonUtil;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.util.file.FileUtil;
import com.netease.nim.uikit.common.util.log.sdk.util.FileUtils;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

import java.io.File;

/**
 * Created by zhoujianghua on 2015/8/4.
 */
public class MsgViewHolderReplyCustom extends MsgViewHolderBase {
    TextView account, replyAccount, content;   //自定义标题/内容
    ImageView imageView;  //图片
    LinearLayout layout, replyLayout;

    //文件
    private ImageView fileIcon;
    private TextView fileNameLabel;
    private TextView fileStatusLabel;
    private ProgressBar progressBar;
    private RelativeLayout relativeLayout;
    private ReplyAttachment attachment;

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
        replyLayout = findViewById(R.id.reply_layout);

        //文件
        fileIcon = (ImageView) view.findViewById(R.id.message_item_file_icon_image);
        fileNameLabel = (TextView) view.findViewById(R.id.message_item_file_name_label);
        fileStatusLabel = (TextView) view.findViewById(R.id.message_item_file_status_label);
        progressBar = (ProgressBar) view.findViewById(R.id.message_item_file_transfer_progress_bar);
        relativeLayout = view.findViewById(R.id.message_item_file_detail_layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick();
            }
        });
    }

    @Override
    protected void bindContentView() {
        attachment = (ReplyAttachment) message.getAttachment();
        //被回复人名称
        replyAccount.setText(attachment.getReplyAccount() == null ? "" : attachment.getReplyAccount());
        //图片或者是文本
        if (attachment.getMsgType() != null) {
            if (attachment.getMsgType().equals("text")) {
                relativeLayout.setVisibility(View.GONE);
                replyLayout.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                content.setVisibility(View.VISIBLE);
                MoonUtil.identifyFaceExpression(NimUIKit.getContext(), content, attachment.getReplyContent(), ImageSpan.ALIGN_BOTTOM);
                //     content.setText(attachment.getReplyContent() == null ? "" : attachment.getReplyContent());
            } else if (attachment.getMsgType().equals("file")) {
                if (attachment.getMsg() != null && attachment.getMsg().getAttachment() != null) {
                    replyLayout.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                    String path = attachment.getMsg().getAttachment().getPath() == null ? "" : attachment.getMsg().getAttachment().getPath();
                    initDisplay();
                    if (!TextUtils.isEmpty(path)) {
                        loadImageView();
                    } else {
                        AttachStatusEnum status = message.getAttachStatus();
                        switch (status) {
                            case def:
                                updateFileStatusLabel();
                                break;
                            case transferring:
                                fileStatusLabel.setVisibility(View.GONE);
                                progressBar.setVisibility(View.VISIBLE);
                                int percent = (int) (getMsgAdapter().getProgress(message) * 100);
                                progressBar.setProgress(percent);
                                break;
                            case transferred:
                            case fail:
                                updateFileStatusLabel();
                                break;
                        }
                    }
                } else {
                    replyLayout.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    content.setVisibility(View.VISIBLE);
                    content.setText("文件消息");
                }
            } else if (attachment.getMsgType().equals("custom")) {
                replyLayout.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                content.setVisibility(View.GONE);
                if (attachment.getReplyContent().equals("贴图消息")) {
                    if (attachment.getMsg() != null && attachment.getMsg().getAttachment() != null) {
                        Glide.with(context)
                                .load(StickerManager.getInstance().getStickerUri(attachment.getMsg().getAttachment().getCatalog() == null ? "" : attachment.getMsg().getAttachment().getCatalog(),
                                        attachment.getMsg().getAttachment().getChartlet() == null ? "" : attachment.getMsg().getAttachment().getChartlet()))
                                .apply(new RequestOptions()
                                        .error(com.netease.nim.uikit.R.drawable.nim_default_img_failed)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                                .into(imageView);
                    }
                }
            } else {
                replyLayout.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.GONE);
                content.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                GlideUtils.lxGlide(context, attachment.getUrl() == null ? "" : attachment.getUrl(), imageView);
            }
        }
        //回复消息
        MoonUtil.identifyFaceExpression(NimUIKit.getContext(), account, attachment.getContent() == null ? "" : attachment.getContent(), ImageSpan.ALIGN_BOTTOM);
       // account.setText(attachment.getContent() == null ? "" : attachment.getContent());
        //是否是接收消息
        if (isReceivedMessage()) {
            layout.setBackgroundResource(NimUIKitImpl.getOptions().messageLeftBackground);
            layout.setPadding(ScreenUtil.dip2px(15), ScreenUtil.dip2px(8), ScreenUtil.dip2px(10), ScreenUtil.dip2px(8));
        } else {
            layout.setBackgroundResource(NimUIKitImpl.getOptions().messageRightWhiteBackground);
            layout.setPadding(ScreenUtil.dip2px(10), ScreenUtil.dip2px(8), ScreenUtil.dip2px(15), ScreenUtil.dip2px(8));
        }

        //文件
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileDownloadActivity.start(context,message,"reply");
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (attachment.getMsgType().equals("video")) {
//                    WatchVideoActivity.start(context, message);
//                } else {}
            }
        });
    }

    @Override
    protected int rightBackground() {
        return 0;
    }

    //文件相关
    private void loadImageView() {
        fileStatusLabel.setVisibility(View.VISIBLE);
        // 文件长度
        StringBuilder sb = new StringBuilder();
        sb.append(FileUtil.formatFileSize(attachment.getMsg().getAttachment().getSize()));
        fileStatusLabel.setText(sb.toString());

        progressBar.setVisibility(View.GONE);
    }

    private void initDisplay() {
        int iconResId = FileIcons.smallIcon(attachment.getMsg().getAttachment().getDisplayName());
        fileIcon.setImageResource(iconResId);
        fileNameLabel.setText(attachment.getMsg().getAttachment().getDisplayName());
    }

    private void updateFileStatusLabel() {
        fileStatusLabel.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        // 文件长度
        StringBuilder sb = new StringBuilder();
        sb.append(FileUtil.formatFileSize(attachment.getMsg().getAttachment().getSize()));
        sb.append("  ");
        // 下载状态
        String path = attachment.getMsg().getAttachment().getDisplayName();
        if (FileUtils.isFileExist(path)) {
            sb.append(context.getString(R.string.file_transfer_state_downloaded));
        } else {
            sb.append(context.getString(R.string.file_transfer_state_undownload));
        }
        fileStatusLabel.setText(sb.toString());
    }
}
