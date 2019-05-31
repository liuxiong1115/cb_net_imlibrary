package com.netease.nim.demo.session.extension;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachmentParser;

/**
 * Created by zhoujianghua on 2015/4/9.
 */
public class CustomAttachParser implements MsgAttachmentParser {

    private static final String KEY_TYPE = "type";
    private static final String KEY_DATA = "data";

    @Override
    public MsgAttachment parse(String json) {
        CustomAttachment attachment = null;
        try {
            JSONObject object = JSON.parseObject(json);
            int type = object.getInteger(KEY_TYPE);
            switch (type) {
                case CustomAttachmentType.Guess:
                    attachment = new GuessAttachment();
                    break;
                case CustomAttachmentType.SnapChat:
                    JSONObject data = object.getJSONObject(KEY_DATA);
                    return new SnapChatAttachment(data);
                case CustomAttachmentType.Sticker:
                    attachment = new StickerAttachment();
                    break;
                case CustomAttachmentType.RTS:
//                    attachment = new RTSAttachment();
                    break;
                case CustomAttachmentType.RedPacket:
                    attachment = new RedPacketAttachment();
                    break;
                case CustomAttachmentType.OpenedRedPacket:
                    attachment = new RedPacketOpenedAttachment();
                    break;
                case CustomAttachmentType.Notify: //新访客消息
                    attachment = new NotifyAttchment();
                    break;
                default:   //自定义
                    attachment = new DefaultCustomAttachment();
                    break;
            }
            if (attachment != null) {
                attachment.fromJson(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return attachment;
    }

    public static String packData(int type, JSONObject data) {
        JSONObject object = new JSONObject();
        object.put(KEY_TYPE, type);
        if (data != null) {
            object.put(KEY_DATA, data);
        }

        return object.toJSONString();
    }
}
