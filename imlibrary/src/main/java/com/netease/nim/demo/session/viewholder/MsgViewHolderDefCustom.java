package com.netease.nim.demo.session.viewholder;

import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.netease.nim.demo.R;
import com.netease.nim.demo.session.extension.CustomAttachParser;
import com.netease.nim.demo.session.extension.CustomAttachmentType;
import com.netease.nim.demo.session.extension.DefaultCustomAttachment;
import com.netease.nim.demo.session.utils.GlideUtils;
import com.netease.nim.uikit.business.session.emoji.MoonUtil;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.CommonUtil;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

/**
 * Created by zhoujianghua on 2015/8/4.
 */
public class MsgViewHolderDefCustom extends MsgViewHolderBase {
    TextView titleView, contentView;  //自定义标题/内容
    ImageView imageView;  //图片
    LinearLayout layout;

    public MsgViewHolderDefCustom(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }


    @Override
    protected int getContentResId() {
        return R.layout.message_itme_custom;
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
        titleView = findViewById(R.id.message_title);
        contentView = findViewById(R.id.message_desc);
        imageView = findViewById(R.id.message_image);
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
        final DefaultCustomAttachment attachment = (DefaultCustomAttachment) message.getAttachment();
        MoonUtil.identifyFaceExpressionAndATags(context, titleView, attachment.getTitle() == null ? "" : attachment.getTitle(), ImageSpan.ALIGN_BASELINE);  //标题
        if (attachment.getMsgType() == CustomAttachmentType.Extend) {  //313扩展消息
            MoonUtil.identifyFaceExpressionAndATags(context, contentView, attachment.getDesc() == null ? "" : attachment.getDesc(), ImageSpan.ALIGN_BOTTOM);  //内容
            //图片
            if (!TextUtils.isEmpty(attachment.getImgUrl())) {
                RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.appicon);
                GlideUtils.lxGlide(context, attachment.getIcon(), imageView, requestOptions);
            }
        } else {
            MoonUtil.identifyFaceExpressionAndATags(context, contentView, attachment.getSubTitle() == null ? "" : attachment.getSubTitle(), ImageSpan.ALIGN_BOTTOM);  //内容
            //图片
            if (!TextUtils.isEmpty(attachment.getImgUrl())) {
                RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.appicon);
                GlideUtils.lxGlide(context, attachment.getImgUrl(), imageView, requestOptions);
            }
        }

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.ChatItemOnClicklistener li = CommonUtil.clicklistener;
                if (li != null) {
                    li.onClick(v, attachment);
                } else {
                    System.err.println("ChatItemOnClicklistener is null !");
                }
            }
        });

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
