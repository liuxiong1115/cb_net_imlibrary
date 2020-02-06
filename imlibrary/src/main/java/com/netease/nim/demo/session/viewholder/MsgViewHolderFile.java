package com.netease.nim.demo.session.viewholder;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nim.demo.R;
import com.netease.nim.demo.file.FileIcons;
import com.netease.nim.demo.session.activity.FileDownloadActivity;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.CommonUtil;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.util.C;
import com.netease.nim.uikit.common.util.file.AttachmentStore;
import com.netease.nim.uikit.common.util.file.FileUtil;
import com.netease.nim.uikit.common.util.log.sdk.util.FileUtils;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.File;
import java.util.Map;

/**
 * Created by zhoujianghua on 2015/8/6.
 */
public class MsgViewHolderFile extends MsgViewHolderBase {
    public ImageView fileIcon;
    public TextView fileNameLabel;
    public TextView fileStatusLabel;
    public ProgressBar progressBar;

    public RelativeLayout layout;
    public FileAttachment msgAttachment;

    public MsgViewHolderFile(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_file;
    }

    @Override
    protected void inflateContentView() {
        fileIcon = (ImageView) view.findViewById(R.id.message_item_file_icon_image);
        fileNameLabel = (TextView) view.findViewById(R.id.message_item_file_name_label);
        fileStatusLabel = (TextView) view.findViewById(R.id.message_item_file_status_label);
        progressBar = (ProgressBar) view.findViewById(R.id.message_item_file_transfer_progress_bar);
        layout = view.findViewById(R.id.message_item_file_detail_layout);
    }

    @Override
    protected void bindContentView() {
        msgAttachment = (FileAttachment) message.getAttachment();
        String path = msgAttachment.getPath();
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

        //是否是接收消息
        if (isReceivedMessage()) {
            layout.setBackgroundResource(NimUIKitImpl.getOptions().messageLeftBackground);
            layout.setPadding(ScreenUtil.dip2px(15), ScreenUtil.dip2px(8), ScreenUtil.dip2px(10), ScreenUtil.dip2px(8));
        }else {
            layout.setBackgroundResource(NimUIKitImpl.getOptions().messageRightWhiteBackground);
            layout.setPadding(ScreenUtil.dip2px(15), ScreenUtil.dip2px(10), ScreenUtil.dip2px(15), ScreenUtil.dip2px(10));
        }
    }

    private void loadImageView() {
        fileStatusLabel.setVisibility(View.VISIBLE);
        // 文件长度
        StringBuilder sb = new StringBuilder();
        sb.append(FileUtil.formatFileSize(msgAttachment.getSize()));
        fileStatusLabel.setText(sb.toString());

        progressBar.setVisibility(View.GONE);
    }

    private void initDisplay() {
        int iconResId = FileIcons.smallIcon(msgAttachment.getDisplayName());
        fileIcon.setImageResource(iconResId);
        fileNameLabel.setText(msgAttachment.getDisplayName());
    }

    private void updateFileStatusLabel() {
        fileStatusLabel.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        // 文件长度
        StringBuilder sb = new StringBuilder();
        sb.append(FileUtil.formatFileSize(msgAttachment.getSize()));
        sb.append("  ");
        // 下载状态
        String path = msgAttachment.getDisplayName();
        if (FileUtils.isFileExist(path)) {
            sb.append(context.getString(R.string.file_transfer_state_downloaded));
        } else {
            sb.append(context.getString(R.string.file_transfer_state_undownload));
        }
        fileStatusLabel.setText(sb.toString());
        CommonUtil.setonDealMediaUrlListener(new CommonUtil.onDealMediaUrlListener() {
            @Override
            public void onDealMediaUrl() {
                FileDownloadActivity.start(context, message,"");
            }
        });
    }

    @Override
    protected void onItemClick() {


        if (CommonUtil.role == CommonUtil.SELLER) {
            Map<String, Object> map = message.getRemoteExtension();
            if (map != null) {
                String wxMsgId = (String) map.get("wxMsgId");
                if (!TextUtils.isEmpty(wxMsgId)) {
                    FileAttachment fileAttachment = (FileAttachment) message.getAttachment();
                    boolean isExit = FileUtils.isFileExist(fileAttachment.getDisplayName());
                    if (!isExit) {
                        CommonUtil.onGetMediaUrlListener onGetMediaUrlListener = CommonUtil.getMediaUrlListener;
                        if (onGetMediaUrlListener != null) {
                            onGetMediaUrlListener.onMediaUrl(message,context,wxMsgId);
                            return;
                        }
                    }
                }
            }
        }
            FileDownloadActivity.start(context, message,"");
    }

    @Override
    protected int rightBackground() {
        return 0;
    }

    /*
    @Override
    protected int leftBackground() {
        return R.drawable.nim_message_left_white_bg;
    }

    @Override
    protected int rightBackground() {
        return R.drawable.nim_message_right_blue_bg;
    }*/

}
