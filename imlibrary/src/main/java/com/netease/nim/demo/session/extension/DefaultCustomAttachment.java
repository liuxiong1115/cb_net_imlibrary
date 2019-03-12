package com.netease.nim.demo.session.extension;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhoujianghua on 2015/4/10.
 */
public class DefaultCustomAttachment extends CustomAttachment {

    private String title;
    private String content;
    private int msgType;
    private String messageId;  //消息传递id
    private String imgUrl;

    private static final String KEY_CONTENT = "subtitle";
    private static final String KEY_ID = "messageId";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TYPE = "type";
    private static final String KEY_URL = "imgUrl";

    public DefaultCustomAttachment() {
        super(0);
    }
    @Override
    protected void parseData(JSONObject data) {
        content = data.getString(KEY_CONTENT);
        title = data.getString(KEY_TITLE);
        msgType = data.getInteger(KEY_TYPE);
        messageId = data.getString(KEY_ID);
        imgUrl = data.getString(KEY_URL);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        try {
            data.put(KEY_CONTENT, content);
            data.put(KEY_ID, messageId);
            data.put(KEY_TITLE, title);
            data.put(KEY_TYPE, type);
            data.put(KEY_URL, imgUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public int getMsgType() {
        return msgType;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
