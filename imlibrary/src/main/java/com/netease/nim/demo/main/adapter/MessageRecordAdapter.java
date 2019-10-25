package com.netease.nim.demo.main.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netease.nim.demo.R;
import com.netease.nim.demo.file.FileIcons;
import com.netease.nim.demo.session.extension.ReplyAttachment;
import com.netease.nim.demo.session.utils.GlideUtils;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.UIKitOptions;
import com.netease.nim.uikit.business.session.emoji.MoonUtil;
import com.netease.nim.uikit.business.session.emoji.StickerManager;
import com.netease.nim.uikit.business.session.module.model.Message;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.util.file.FileUtil;
import com.netease.nim.uikit.common.util.log.sdk.util.FileUtils;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;

import java.util.List;

/**
 * Created by mike on 2019/9/9.
 */

public class MessageRecordAdapter extends BaseQuickAdapter<Message, BaseViewHolder> {
    //文件
    private ImageView fileIcon;
    private TextView fileNameLabel;
    private TextView fileStatusLabel;
    private ProgressBar progressBar;
    private RelativeLayout fileLayout;
    private LinearLayout layout;
    private Context context;

    private AppCompatTextView content;
    private ImageView imageView,background;

    public MessageRecordAdapter(List<Message> list, Context context) {
        super(R.layout.adapter_message_record, list);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Message item) {
        //文件
        fileIcon = helper.getView(R.id.message_item_file_icon_image);
        fileNameLabel = helper.getView(R.id.message_item_file_name_label);
        fileStatusLabel = helper.getView(R.id.message_item_file_status_label);
        progressBar = helper.getView(R.id.message_item_file_transfer_progress_bar);
        fileLayout = helper.getView(R.id.message_item_file_detail_layout);
        layout = helper.getView(R.id.layout);
        content = helper.getView(R.id.content);
        imageView = helper.getView(R.id.image);
        background = helper.getView(R.id.background);

        //头像
        HeadImageView headImageView = helper.getView(R.id.avatar);
        headImageView.loadBuddyAvatar(item.getFromAccount() == null ? "" : item.getFromAccount());
        //名字
        helper.setText(R.id.account, item.getFromNick() == null ? "" : item.getFromNick());
        if (item.getMsgType() == null) {
            fileLayout.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            MoonUtil.identifyFaceExpression(NimUIKit.getContext(), helper.getView(R.id.content), item.getContent() == null ? "" : item.getContent(), ImageSpan.ALIGN_BOTTOM);
        } else {
            if (item.getMsgType().equals("text")) {
                layout.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                MoonUtil.identifyFaceExpression(NimUIKit.getContext(), helper.getView(R.id.content), item.getContent() == null ? "" : item.getContent(), ImageSpan.ALIGN_BOTTOM);
            } else if (item.getMsgType().equals("file")) {
                fileLayout.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
                String path = item.getAttachment().getPath() == null ? "" : item.getAttachment().getPath();
                initDisplay(item);
                if (!TextUtils.isEmpty(path)) {
                    loadImageView(item);
                } else {
                    updateFileStatusLabel(item);
//                    String status = item.getAttachStatus();
//                    switch (status) {
//                        case "def":
//                            updateFileStatusLabel(item);
//                            break;
//                        case "transferred":
//                        case "fail":
//                            updateFileStatusLabel(item);
//                            break;
//                    }
                }
            } else if (item.getMsgType().equals("custom")) {
                fileLayout.setVisibility(View.GONE);
                content.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                if (item.getContent().equals("贴图消息")) {
                    if (item != null && item.getAttachment() != null) {
                        Glide.with(context)
                                .load(StickerManager.getInstance().getStickerUri(item.getAttachment().getCatalog() == null ? "" : item.getAttachment().getCatalog(),
                                        item.getAttachment().getChartlet() == null ? "" : item.getAttachment().getChartlet()))
                                .apply(new RequestOptions()
                                        .error(com.netease.nim.uikit.R.drawable.nim_default_img_failed)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                                .into(imageView);
                    }
                }
            } else {
                layout.setVisibility(View.VISIBLE);
                fileLayout.setVisibility(View.GONE);
                content.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                GlideUtils.lxGlide(context, item.getAttachment().getUrl() == null ? "" : item.getAttachment().getUrl(), imageView);
            }
        }
        helper.addOnClickListener(R.id.message_item_file_detail_layout);
    }

    //文件相关
    private void loadImageView(Message item) {
        fileStatusLabel.setVisibility(View.VISIBLE);
        // 文件长度
        StringBuilder sb = new StringBuilder();
        sb.append(FileUtil.formatFileSize(item.getAttachment().getSize()));
        fileStatusLabel.setText(sb.toString());

        progressBar.setVisibility(View.GONE);
    }

    private void initDisplay(Message item) {
        int iconResId = FileIcons.smallIcon(item.getAttachment().getDisplayName());
        fileIcon.setImageResource(iconResId);
        fileNameLabel.setText(item.getAttachment().getDisplayName());
    }

    private void updateFileStatusLabel(Message item) {
        fileStatusLabel.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        // 文件长度
        StringBuilder sb = new StringBuilder();
        sb.append(FileUtil.formatFileSize(item.getAttachment().getSize()));
        sb.append("  ");
        // 下载状态
        String path = item.getAttachment().getDisplayName();
        if (FileUtils.isFileExist(path)) {
            sb.append(context.getString(R.string.file_transfer_state_downloaded));
        } else {
            sb.append(context.getString(R.string.file_transfer_state_undownload));
        }
        fileStatusLabel.setText(sb.toString());
    }
}
