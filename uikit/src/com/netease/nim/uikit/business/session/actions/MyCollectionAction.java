package com.netease.nim.uikit.business.session.actions;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.CommonUtil;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.File;

/**
 * Created by hzxuwen on 2015/6/12.
 */
public class MyCollectionAction extends BaseAction {

    public MyCollectionAction() {
        super(R.drawable.nim_action_schedule, R.string.input_panel_collection);
    }


    @Override
    public void onClick() {
        CommonUtil.onClickMyCollectionListener listener = CommonUtil.myCollectionListener;
        if (listener != null) {
            listener.onClickCollection(getActivity(),sessionId);
        }
    }
}

