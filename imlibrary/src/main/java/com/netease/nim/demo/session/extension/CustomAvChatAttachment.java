package com.netease.nim.demo.session.extension;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.nim.uikit.business.session.module.model.Message;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * Created by zhoujianghua on 2015/4/9.
 */
public class CustomAvChatAttachment extends CustomAttachment {

    public CustomAvChatAttachment() { super(CustomAttachmentType.avChatCall);}

    public void setContent(String content) {
        this.content = content;
    }

    private String content; //内容

    public void setType(Integer type) {
        this.type = type;
    }

    private Integer type;

    private static final String KEY_CONTENT = "content";

    @Override
    protected void parseData(JSONObject data) {
        content = data.getString(KEY_CONTENT);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        try {
            data.put(KEY_CONTENT, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    public String getContent() {
        return content;
    }

}
