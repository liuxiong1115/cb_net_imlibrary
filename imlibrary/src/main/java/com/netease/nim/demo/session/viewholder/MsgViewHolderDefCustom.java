package com.netease.nim.demo.session.viewholder;

import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.netease.nim.demo.R;
import com.netease.nim.demo.session.extension.CustomAttachmentType;
import com.netease.nim.demo.session.extension.DefaultCustomAttachment;
import com.netease.nim.demo.session.utils.GlideUtils;
import com.netease.nim.uikit.business.session.emoji.MoonUtil;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.CommonUtil;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
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
        MoonUtil.identifyFaceExpressionAndATags(context, contentView, attachment.getSubTitle() == null ? "" : attachment.getSubTitle(), ImageSpan.ALIGN_BOTTOM);  //内容

        //图片
        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.appicon);
        GlideUtils.lxGlide(context, attachment.getImgUrl(), imageView,requestOptions);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.ChatItemOnClicklistener li = CommonUtil.clicklistener;
                if (li != null) {
                    li.onClick(v, attachment);
                } else {
                    System.err.println("ChatItemOnClicklistener is null !");
                }

//                if (attachment.getMsgType() == CustomAttachmentType.ConfirmLetter) {  //确认函
//                    Toast.makeText(context,"确认函",Toast.LENGTH_SHORT).show();
//                }else if(attachment.getMsgType() == CustomAttachmentType.Bill) {  //账单
//                    Toast.makeText(context,"账单",Toast.LENGTH_SHORT).show();
//
//                } else if(attachment.getMsgType() == CustomAttachmentType.Course) { //课程
//                    Toast.makeText(context,"课程",Toast.LENGTH_SHORT).show();
//
//                }else {  //课堂
//                    Toast.makeText(context,"课堂",Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

}
