//package com.netease.nim.uikit.business.session.viewholder;
//
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.TextView;
//
//import com.netease.nim.uikit.R;
//import com.netease.nim.uikit.common.ui.imageview.MsgThumbImageView;
//import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
//import com.netease.nim.uikit.common.util.media.BitmapDecoder;
//import com.netease.nim.uikit.common.util.media.ImageUtil;
//import com.netease.nim.uikit.common.util.string.StringUtil;
//import com.netease.nim.uikit.common.util.sys.ScreenUtil;
//import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
//import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
//import com.netease.nimlib.sdk.msg.attachment.VideoAttachment;
//import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
//import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
//import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
//
//import java.io.File;
//
///*
// * Created by zhoujianghua on 2015/8/4.
// */
//
//
//public abstract class MsgViewHolderThumbBase extends MsgViewHolderBase {
//
//    public MsgViewHolderThumbBase(BaseMultiItemFetchLoadAdapter adapter) {
//        super(adapter);
//    }
//
//    protected MsgThumbImageView thumbnail;
//    protected View progressCover;
//    protected TextView progressLabel;
//
//    @Override
//    protected void inflateContentView() {
//        thumbnail = findViewById(R.id.message_item_thumb_thumbnail);
//        progressBar = findViewById(R.id.message_item_thumb_progress_bar); // 覆盖掉
//        progressCover = findViewById(R.id.message_item_thumb_progress_cover);
//        progressLabel = findViewById(R.id.message_item_thumb_progress_text);
//    }
//
//    @Override
//    protected void bindContentView() {
//        FileAttachment msgAttachment = (FileAttachment) message.getAttachment();
//        //TODO  图片获取   获取本地路径path   url为远端路径
//        String path = msgAttachment.getUrl();
//        String thumbPath = msgAttachment.getThumbPath();
//        if (!TextUtils.isEmpty(path)) {
//            loadThumbnailImage(thumbFromSourceFile(path), true, msgAttachment.getExtension());
//        } else if (!TextUtils.isEmpty(thumbPath)) {
//            loadThumbnailImage(thumbPath, false, msgAttachment.getExtension());
//        } else {
//            loadThumbnailImage(null, false, msgAttachment.getExtension());
//            if (message.getAttachStatus() == AttachStatusEnum.transferred
//                    || message.getAttachStatus() == AttachStatusEnum.def) {
//                downloadAttachment();
//            }
//        }
//        refreshStatus();
//    }
//
//    private void refreshStatus() {
//        FileAttachment attachment = (FileAttachment) message.getAttachment();
//        if (TextUtils.isEmpty(attachment.getPath()) && TextUtils.isEmpty(attachment.getThumbPath())) {
//            if (message.getAttachStatus() == AttachStatusEnum.fail || message.getStatus() == MsgStatusEnum.fail) {
//                alertButton.setVisibility(View.VISIBLE);
//            } else {
//                alertButton.setVisibility(View.GONE);
//            }
//        }
//
//        if (message.getStatus() == MsgStatusEnum.sending
//                || (isReceivedMessage() && message.getAttachStatus() == AttachStatusEnum.transferring)) {
//            progressCover.setVisibility(View.VISIBLE);
//            progressBar.setVisibility(View.VISIBLE);
//            progressLabel.setVisibility(View.VISIBLE);
//            progressLabel.setText(StringUtil.getPercentString(getMsgAdapter().getProgress(message)));
//        } else {
//            progressCover.setVisibility(View.GONE);
//            progressBar.setVisibility(View.GONE);
//            progressLabel.setVisibility(View.GONE);
//        }
//    }
//
//    private void loadThumbnailImage(String path, boolean isOriginal, String ext) {
//        setImageSize(path);
//        if (path != null) {
//            //thumbnail.loadAsPath(thumbPath, getImageMaxEdge(), getImageMaxEdge(), maskBg());
//            thumbnail.loadAsPath(path, getImageMaxEdge(), getImageMaxEdge(), maskBg(), ext);
//        } else {
//            thumbnail.loadAsResource(R.drawable.nim_image_default, maskBg());
//        }
//    }
//
//    private void setImageSize(String thumbPath) {
//        int[] bounds = null;
//        if (thumbPath != null) {
//            bounds = BitmapDecoder.decodeBound(new File(thumbPath));
//        }
//        if (bounds == null) {
//            if (message.getMsgType() == MsgTypeEnum.image) {
//                ImageAttachment attachment = (ImageAttachment) message.getAttachment();
//                bounds = new int[]{attachment.getWidth(), attachment.getHeight()};
//            } else if (message.getMsgType() == MsgTypeEnum.video) {
//                VideoAttachment attachment = (VideoAttachment) message.getAttachment();
//                bounds = new int[]{attachment.getWidth(), attachment.getHeight()};
//            }
//        }
//
//        if (bounds != null) {
//            ImageUtil.ImageSize imageSize = ImageUtil.getThumbnailDisplaySize(bounds[0], bounds[1], getImageMaxEdge(), getImageMinEdge());
//            setLayoutParams(imageSize.width, imageSize.height, thumbnail);
//        }
//    }
//
//    private int maskBg() {
//        return R.drawable.nim_message_item_round_bg;
//    }
//
//    public static int getImageMaxEdge() {
//        return (int) (165.0 / 320.0 * ScreenUtil.screenWidth);
//    }
//
//    public static int getImageMinEdge() {
//        return (int) (76.0 / 320.0 * ScreenUtil.screenWidth);
//    }
//
//    protected abstract String thumbFromSourceFile(String path);
//}
package com.netease.nim.uikit.business.session.viewholder;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.ui.imageview.MsgThumbImageView;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.util.media.BitmapDecoder;
import com.netease.nim.uikit.common.util.media.ImageUtil;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.attachment.VideoAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;

import java.io.File;

/**
 * Created by zhoujianghua on 2015/8/4.
 */
public abstract class MsgViewHolderThumbBase extends MsgViewHolderBase {

    public MsgViewHolderThumbBase(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    protected MsgThumbImageView thumbnail;
    protected View progressCover;
    protected TextView progressLabel;

    @Override
    protected void inflateContentView() {
        thumbnail = findViewById(R.id.message_item_thumb_thumbnail);
        progressBar = findViewById(R.id.message_item_thumb_progress_bar); // 覆盖掉
        progressCover = findViewById(R.id.message_item_thumb_progress_cover);
        progressLabel = findViewById(R.id.message_item_thumb_progress_text);
    }

    @Override
    protected void bindContentView() {
        final FileAttachment msgAttachment = (FileAttachment) message.getAttachment();
        String path = msgAttachment.getPath();
        String thumbPath = msgAttachment.getThumbPath();
        if (!TextUtils.isEmpty(thumbPath)) {
            loadThumbnailImage(thumbPath, false, msgAttachment.getExtension(), msgAttachment);
        }else if (!TextUtils.isEmpty(path)) {
            loadThumbnailImage(thumbFromSourceFile(path), true, msgAttachment.getExtension(),msgAttachment);
        } else {
            loadThumbnailImage(null, false, msgAttachment.getExtension(),msgAttachment);
            if (message.getAttachStatus() == AttachStatusEnum.transferred ||
                    message.getAttachStatus() == AttachStatusEnum.def) {
                downloadAttachment(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        loadThumbnailImage(msgAttachment.getThumbPath(), false, msgAttachment.getExtension(),msgAttachment);
                        refreshStatus();
                    }

                    @Override
                    public void onFailed(int code) {
                        Log.e("code",code+"");
                    }

                    @Override
                    public void onException(Throwable exception) {
                        Log.e("code",exception+"");
                    }
                });
            }
        }
        refreshStatus();
    }

    private void refreshStatus() {
        FileAttachment attachment = (FileAttachment) message.getAttachment();
        if (TextUtils.isEmpty(attachment.getPath()) && TextUtils.isEmpty(attachment.getThumbPath())) {
            if (message.getAttachStatus() == AttachStatusEnum.fail || message.getStatus() == MsgStatusEnum.fail) {
                alertButton.setVisibility(View.VISIBLE);
            } else {
                alertButton.setVisibility(View.GONE);
            }
        }

        if (message.getStatus() == MsgStatusEnum.sending
                || (isReceivedMessage() && message.getAttachStatus() == AttachStatusEnum.transferring)) {
            progressCover.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            progressLabel.setVisibility(View.VISIBLE);
            progressLabel.setText(StringUtil.getPercentString(getMsgAdapter().getProgress(message)));
        } else {
            progressCover.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            progressLabel.setVisibility(View.GONE);
        }
    }

    private void loadThumbnailImage(String path, boolean isOriginal, String ext,FileAttachment attachment) {
        setImageSize(path);
        if (path != null) {
            //thumbnail.loadAsPath(thumbPath, getImageMaxEdge(), getImageMaxEdge(), maskBg());
            thumbnail.loadAsPath(path, getImageMaxEdge(), getImageMaxEdge(), maskBg(), ext);
        } else {
            if (attachment != null) {
                String url = attachment.getUrl();
                thumbnail.loadAsPath(attachment.getUrl() == null ?"": url, getImageMaxEdge(), getImageMaxEdge(), maskBg(), ext);
            } else {
                thumbnail.loadAsResource(R.drawable.nim_image_default, maskBg());
            }
        }
    }

    private void setImageSize(String thumbPath) {
        int[] bounds = null;
        if (thumbPath != null) {
            bounds = BitmapDecoder.decodeBound(new File(thumbPath));
        }
        if (bounds == null) {
            if (message.getMsgType() == MsgTypeEnum.image) {
                ImageAttachment attachment = (ImageAttachment) message.getAttachment();
                bounds = new int[]{attachment.getWidth(), attachment.getHeight()};
            } else if (message.getMsgType() == MsgTypeEnum.video) {
                VideoAttachment attachment = (VideoAttachment) message.getAttachment();
                bounds = new int[]{attachment.getWidth(), attachment.getHeight()};
            }
        }

        if (bounds != null) {
            ImageUtil.ImageSize imageSize = ImageUtil.getThumbnailDisplaySize(bounds[0], bounds[1], getImageMaxEdge(), getImageMinEdge());
            setLayoutParams(imageSize.width, imageSize.height, thumbnail);
        }
    }

    public int maskBg() {
        return R.drawable.nim_message_item_round_bg;
    }

    public static int getImageMaxEdge() {
        return (int) (165.0 / 320.0 * ScreenUtil.screenWidth);
    }

    public static int getImageMinEdge() {
        return (int) (76.0 / 320.0 * ScreenUtil.screenWidth);
    }

    protected abstract String thumbFromSourceFile(String path);
}
