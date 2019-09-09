package com.netease.nim.demo.session.extension;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.nim.uikit.business.session.module.model.Message;
import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * Created by zhoujianghua on 2015/4/10.
 */
public class ReplyAttachment extends CustomAttachment {
    private String replyAccount;  //被回复人名称
    private String replyContent;  //被回复人信息
    private String content; //回复内容
    private String url;
    private String msgType;
    private Integer type;
    private Message msg;

    public IMMessage getMessage() {
        return message;
    }

    public void setMessage(IMMessage message) {
        this.message = message;
    }

    private IMMessage message;

    private static final String KEY_REPLY_ACCOUNT = "replyAccount";
    private static final String KEY_REPLY_CONTENT = "replyContent";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_URL = "url";
    private static final String KEY_MSGTYPE = "msgType";
    private static final String KEY_MESSAGE = "message";

    @Override
    protected void parseData(JSONObject data) {
        content = data.getString(KEY_CONTENT);
        replyAccount = data.getString(KEY_REPLY_ACCOUNT);
        replyContent = data.getString(KEY_REPLY_CONTENT);
        url = (String) data.get(KEY_URL);
        msgType = (String) data.get(KEY_MSGTYPE);
        String s = data.getString(KEY_MESSAGE);
        msg = new Gson().fromJson(s,new TypeToken<Message>() {}.getType());
        //    message=new Gson().fromJson(s,new TypeToken<IMMessage>() {}.getType());
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        try {
            data.put(KEY_CONTENT, content);
            data.put(KEY_REPLY_CONTENT, replyContent);
            data.put(KEY_REPLY_ACCOUNT, replyAccount);
            data.put(KEY_URL, url);
            data.put(KEY_MSGTYPE, msgType);
            data.put(KEY_MESSAGE, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public String getReplyAccount() {
        return replyAccount;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public String getMsgType() {
        return msgType;
    }

    public ReplyAttachment() {
        super(CustomAttachmentType.ReplyMsg);
    }

    public void setReplyAccount(String replyAccount) {
        this.replyAccount = replyAccount;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    @Override
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Message getMsg () {
        return msg;
    }
}
