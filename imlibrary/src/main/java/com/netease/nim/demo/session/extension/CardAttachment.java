package com.netease.nim.demo.session.extension;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.netease.nim.demo.session.model.CardMessage;

/**
 * Created by mike on 2020/2/19.
 */

public class CardAttachment extends CustomAttachment {
    public CardMessage getCardMessage() {
        return cardMessage;
    }

    public void setCardMessage(CardMessage cardMessage) {
        this.cardMessage = cardMessage;
    }

    private CardMessage cardMessage;
    JSONObject jsonObject;
    public CardAttachment(){
        super(0);
    }
    @Override
    protected void parseData(JSONObject data) {
        jsonObject = data;
        cardMessage = JSON.parseObject(data.toJSONString(), new TypeReference<CardMessage>(){});
        Log.e("123","123");
    }

    @Override
    protected JSONObject packData() {
        return jsonObject;
    }
}
