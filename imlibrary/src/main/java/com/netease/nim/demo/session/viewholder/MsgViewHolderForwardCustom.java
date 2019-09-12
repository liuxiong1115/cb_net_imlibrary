package com.netease.nim.demo.session.viewholder;

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
import com.netease.nim.demo.R;
import com.netease.nim.demo.file.FileIcons;
import com.netease.nim.demo.main.activity.MessageRecordActivity;
import com.netease.nim.demo.session.activity.FileDownloadActivity;
import com.netease.nim.demo.session.extension.ForwardAttachment;
import com.netease.nim.demo.session.extension.ReplyAttachment;
import com.netease.nim.demo.session.utils.GlideUtils;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.session.emoji.MoonUtil;
import com.netease.nim.uikit.business.session.emoji.StickerManager;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.util.file.FileUtil;
import com.netease.nim.uikit.common.util.log.sdk.util.FileUtils;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

/**
 * Created by zhoujianghua on 2015/8/4.
 */
public class MsgViewHolderForwardCustom extends MsgViewHolderBase {

    private AppCompatTextView title,line_one_account,line_two_account,line_three_account,line_one_content,line_two_content,line_three_content;
    private LinearLayout layout,line_two,line_three;
    public MsgViewHolderForwardCustom(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }


    @Override
    protected int getContentResId() {
        return R.layout.nim_message_custom_forward;
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
        layout = findViewById(R.id.forwardLayout);
        title = findViewById(R.id.title);
        line_one_account = findViewById(R.id.line_one_account);
        line_one_content = findViewById(R.id.line_one_content);
        line_two_account = findViewById(R.id.line_two_account);
        line_two_content = findViewById(R.id.line_two_content);
        line_three_account = findViewById(R.id.line_three_account);
        line_three_content = findViewById(R.id.line_three_content);
        line_two = findViewById(R.id.line_two);
        line_three = findViewById(R.id.line_three);
    }

    @Override
    protected void bindContentView() {
        final ForwardAttachment forwardAttachment = (ForwardAttachment) message.getAttachment();
        if (forwardAttachment != null) {
            //标题
            title.setText(forwardAttachment.getContent() == null ? "" : forwardAttachment.getContent());
            //内容
            if (forwardAttachment.getMessageList() != null && forwardAttachment.getMessageList().size() != 0) {
                for (int i=0;i<forwardAttachment.getMessageList().size();i++) {
                    if (i==0) {
                        line_one_account.setText(forwardAttachment.getMessageList().get(0).getFromNick() == null?"":forwardAttachment.getMessageList().get(0).getFromNick()+"：");
                        line_one_content.setText(forwardAttachment.getMessageList().get(0).getContent() == null?forwardAttachment.getMessageList().get(0).getMsgType()
                                ==null ? "" :forwardAttachment.getMessageList().get(0).getMsgType():forwardAttachment.getMessageList().get(0).getContent());
                        if (forwardAttachment.getMessageList().size() == 1) {
                         line_two.setVisibility(View.GONE);
                         line_three.setVisibility(View.GONE);
                        }
                    } else if (i == 1) {
                        line_two_account.setText(forwardAttachment.getMessageList().get(1).getFromNick() == null?"":forwardAttachment.getMessageList().get(1).getFromNick()+"：");
                        line_two_content.setText(forwardAttachment.getMessageList().get(1).getContent() == null?forwardAttachment.getMessageList().get(1).getMsgType()
                                ==null ? "" :forwardAttachment.getMessageList().get(1).getMsgType():forwardAttachment.getMessageList().get(1).getContent());
                        if (forwardAttachment.getMessageList().size() == 2) {
                            line_three.setVisibility(View.GONE);
                        }
                    } else if (i == 2){
                        line_three_account.setText(forwardAttachment.getMessageList().get(2).getFromNick() == null?"":forwardAttachment.getMessageList().get(2).getFromNick()+"：");
                        line_three_content.setText(forwardAttachment.getMessageList().get(2).getContent() == null?forwardAttachment.getMessageList().get(2).getMsgType()
                                ==null ? "" :forwardAttachment.getMessageList().get(2).getMsgType():forwardAttachment.getMessageList().get(2).getContent());
                    }
                }

            } else {
                line_one_account.setText("消息获取失败");
            }
        }
        //是否是接收消息
        if (isReceivedMessage()) {
            layout.setBackgroundResource(NimUIKitImpl.getOptions().messageLeftBackground);
            layout.setPadding(ScreenUtil.dip2px(15), ScreenUtil.dip2px(8), ScreenUtil.dip2px(10), ScreenUtil.dip2px(8));
        } else {
            layout.setBackgroundResource(NimUIKitImpl.getOptions().messageRightWhiteBackground);
            layout.setPadding(ScreenUtil.dip2px(10), ScreenUtil.dip2px(8), ScreenUtil.dip2px(15), ScreenUtil.dip2px(8));
        }

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageRecordActivity.start(context,forwardAttachment.getContent(),forwardAttachment.getMessageList(),message);
            }
        });
    }

    @Override
    protected int rightBackground() {
        return 0;
    }
}
