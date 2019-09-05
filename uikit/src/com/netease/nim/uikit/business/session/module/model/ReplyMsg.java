package com.netease.nim.uikit.business.session.module.model;

/**
 * Created by mike on 2019/9/4.
 */

public class ReplyMsg {

    private int type;

    public ReplyMsgData getData() {
        return data;
    }

    public void setData(ReplyMsgData data) {
        this.data = data;
    }

    private ReplyMsgData data;
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }



}
