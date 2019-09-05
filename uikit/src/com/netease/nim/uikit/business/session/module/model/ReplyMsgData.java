package com.netease.nim.uikit.business.session.module.model;

import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * Created by mike on 2019/9/4.
 */

public class ReplyMsgData {
    private String replyAccount;
    private String replyContent;
    private String content;
    private String url;
    private String msgType;

    public IMMessage getMessage() {
        return message;
    }

    public void setMessage(IMMessage message) {
        this.message = message;
    }

    private IMMessage message;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }



    public String getReplyAccount() {
        return replyAccount;
    }

    public void setReplyAccount(String replyAccount) {
        this.replyAccount = replyAccount;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
