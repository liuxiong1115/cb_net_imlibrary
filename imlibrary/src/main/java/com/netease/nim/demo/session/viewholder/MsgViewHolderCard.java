package com.netease.nim.demo.session.viewholder;

import android.support.v7.widget.AppCompatTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.netease.nim.demo.R;
import com.netease.nim.demo.session.extension.CardAttachment;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.CommonUtil;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;

/**
 * Created by mike on 2020/2/19.
 */

public class MsgViewHolderCard extends MsgViewHolderBase {
    private ImageView thumb;
    private AppCompatTextView title;
    private LinearLayout layout;
    public MsgViewHolderCard(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }
    @Override
    protected int getContentResId() {
        return R.layout.msg_view_card_message;
    }

    @Override
    protected void inflateContentView() {
        thumb = findViewById(R.id.ivThumb);
        title = findViewById(R.id.tvNameTitle);
        layout = findViewById(R.id.lvCardLayout);
    }

    @Override
    protected void bindContentView() {
      CardAttachment cardAttachment = (CardAttachment) message.getAttachment();
      if (cardAttachment != null && cardAttachment.getCardMessage() != null) {
          if (cardAttachment.getCardMessage().getContent() != null &&
                  cardAttachment.getCardMessage().getContent().getBusinessCardEntity() != null) {
              Glide.with(context).load(cardAttachment.getCardMessage().getContent().getBusinessCardEntity().getSmallheadimgurl()== null ? "" :
                      cardAttachment.getCardMessage().getContent().getBusinessCardEntity().getSmallheadimgurl()).into(thumb);
              title.setText(cardAttachment.getCardMessage().getContent().getBusinessCardEntity().getNickname() == null ? "" :
                      cardAttachment.getCardMessage().getContent().getBusinessCardEntity().getNickname());
          }
      }
    }

    @Override
    protected void onItemClick() {
        super.onItemClick();
        CommonUtil.onAddCardMessageListener listener = CommonUtil.addCardMessageListener;
        if (listener != null) {
            listener.onAddCardMessage(context,message.getAttachment());
        }
    }
}
