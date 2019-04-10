package com.netease.nim.demo.session.extension;

import com.alibaba.fastjson.JSONObject;

/**
 * 新访客通知自定义消息
 */

public class NotifyAttchment extends CustomAttachment{

    private static final String ID = "sysConsultLogId";
    private static final String  FROMACCESS = "fromAccessId";
    private Long userId;
    private String fromAccessId;
    NotifyAttchment() {
        super(0);
    }

    @Override
    protected void parseData(JSONObject data) {
        userId = data.getLong(ID);
        fromAccessId = data.getString(FROMACCESS);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        try {
            data.put(ID, userId);
            data.put(FROMACCESS,fromAccessId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    public Long getId () {
        return userId;
    }
    public String getFromAccessId(){
        return fromAccessId;
    }
}
