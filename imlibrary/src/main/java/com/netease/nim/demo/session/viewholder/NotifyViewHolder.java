package com.netease.nim.demo.session.viewholder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.demo.R;
import com.netease.nim.demo.session.extension.NotifyAttchment;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.CommonUtil;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.impl.NimUIKitImpl;

/**
 * Created by mike on 2019/3/22.
 */

public class NotifyViewHolder extends MsgViewHolderBase {
    private TextView textView,accept;
    public NotifyViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.view_notify;
    }

    @Override
    protected void inflateContentView() {
        accept = findViewById(R.id.text_accept);
        textView = findViewById(R.id.notify_text);
    }

    @Override
    protected void bindContentView() {
        final NotifyAttchment attachment = (NotifyAttchment) message.getAttachment();
        if (attachment != null && attachment.getFromAccessId() != null) {
            textView.setText("访客："+attachment.getFromAccessId()+"等待咨询");
        } else {
            textView.setText("有新访客等待咨询");
        }
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (attachment.getId() != null && attachment.getFromAccessId() != null) {
                  //  li.onClick(attachment.getId(),attachment.getFromAccessId());
                    Toast.makeText(context, "成功", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, "当前数据有误！请前往访客列表继续操作", Toast.LENGTH_SHORT).show();
                }*/
                CommonUtil.SellerAcceptOnClicklistener li = CommonUtil.sellerAcceptOnClicklistener;
                if (li != null) {
                    if (attachment.getId() != null && attachment.getFromAccessId() != null) {
                        li.onClick(attachment.getId(),attachment.getFromAccessId());
                    } else {
                        Toast.makeText(context, "当前数据有误！请前往访客列表继续操作", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    System.err.println("SellerAcceptOnClicklistener is null !");
                }
            }
        });
    }

}
