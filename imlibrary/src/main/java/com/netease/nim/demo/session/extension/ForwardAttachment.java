package com.netease.nim.demo.session.extension;


import com.alibaba.fastjson.JSONObject;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;

/**
 * Created by mike on 2019/9/6.
 */

public class ForwardAttachment extends CustomAttachment {
    private String content;
    private List<IMMessage> messages;

    private final static String KEY_CONTENT = "content";
    private final static String KEY_MESSAGE = "message";

    public ForwardAttachment() {
        super(CustomAttachmentType.ForWardMsg);
    }

    @Override
    protected void parseData(JSONObject data) {

    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObject = new JSONObject();

        return null;
    }
}
