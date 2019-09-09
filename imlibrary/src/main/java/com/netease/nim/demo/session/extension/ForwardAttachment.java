package com.netease.nim.demo.session.extension;


import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.nim.uikit.business.session.module.model.Message;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;

/**
 * Created by mike on 2019/9/6.
 */

public class ForwardAttachment extends CustomAttachment {
    private String content;

    public List<Message> getMessageList() {
        return messageList;
    }

    private List<Message> messageList;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<IMMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<IMMessage> messages) {
        this.messages = messages;
    }

    private List<IMMessage> messages;

    private final static String KEY_CONTENT = "content";
    private final static String KEY_MESSAGE = "message";

    public ForwardAttachment() {
        super(CustomAttachmentType.ForWardMsg);
    }

    @Override
    protected void parseData(JSONObject data) {
        String s = data.getString(KEY_MESSAGE);
        messageList = new Gson().fromJson(s,new TypeToken<List<Message>>() {}.getType());
        content = data.getString(KEY_CONTENT);
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(KEY_MESSAGE,messages);
        jsonObject.put(KEY_CONTENT,content);
        return jsonObject;
    }
}
