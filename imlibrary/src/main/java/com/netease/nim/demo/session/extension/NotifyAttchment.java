package com.netease.nim.demo.session.extension;

import com.alibaba.fastjson.JSONObject;

/**
 * 新访客通知自定义消息
 */

public class NotifyAttchment extends CustomAttachment{
    NotifyAttchment() {
        super(0);
    }

    @Override
    protected void parseData(JSONObject data) {

    }

    @Override
    protected JSONObject packData() {
        return null;
    }
}
