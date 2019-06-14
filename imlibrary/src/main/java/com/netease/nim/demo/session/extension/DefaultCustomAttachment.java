package com.netease.nim.demo.session.extension;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhoujianghua on 2015/4/10.
 */
public class DefaultCustomAttachment extends CustomAttachment {

    private String title;
    private String subTitle;
    private int msgType;
    private String messageId;  //消息传递id
    private String imgUrl;
    private int picType;
    private String orderNo;
    private int courseId;
    private JSONObject content;
    private String shareUrl;
    private String icon;
    private String action;
    private String packName;
    private String desc;
    private String url;
    private String chatSid;
    private boolean taxed;
    private float money;
    private String body_url;

    private static final String KEY_SUBTITLE = "subtitle";
    private static final String KEY_ID = "messageId";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TYPE = "type";
    private static final String KEY_IMGURL = "imgUrl";
    private static final String KEY_PICTYPE = "picType";
    private static final String KEY_COURSEID = "courseId";
    private static final String KEY_ORDERNO = "orderNo";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_SHAREURL = "shareUrl";
    private static final String KEY_TAXED="taxed";   //是否缴费
    private static final String KEY_MONEY="money";  //讲师金额
    private static final String KEY_BODY_URL = "url";

    private static final String KEY_ICON = "icon";
    private static final String KEY_DESC= "description";
    private static final String KEY_ACTION= "action";
    private static final String KEY_URL= "url";
    private static final String KEY_PACK= "pageNameA";
    private static final String KEY_CHAT= "chartSessionId";



    public DefaultCustomAttachment() {
        super(0);
    }

    @Override
    protected void parseData(JSONObject data) {
        subTitle = data.getString(KEY_SUBTITLE);
        title = data.getString(KEY_TITLE);
        msgType = data.getInteger(KEY_TYPE);
        messageId = data.getString(KEY_ID);
        imgUrl = data.getString(KEY_IMGURL);
        picType = data.getInteger(KEY_PICTYPE);
        taxed = data.getBoolean(KEY_TAXED);
        money = data.getFloat(KEY_MONEY);
        body_url = data.getString(KEY_BODY_URL);

        content = data.getJSONObject(KEY_CONTENT);
        courseId = content.getInteger(KEY_COURSEID);
        orderNo = content.getString(KEY_ORDERNO);
        shareUrl = content.getString(KEY_SHAREURL);
        icon = content.getString(KEY_ICON);
        desc = content.getString(KEY_DESC);
        action = content.getString(KEY_ACTION);
        url = content.getString(KEY_URL);
        packName = content.getString(KEY_PACK);
        chatSid = content.getString(KEY_CHAT);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        try {
            data.put(KEY_CONTENT, subTitle);
            data.put(KEY_ID, messageId);
            data.put(KEY_TITLE, title);
            data.put(KEY_TYPE, type);
            data.put(KEY_IMGURL, imgUrl);
            data.put(KEY_PICTYPE, picType);
            data.put(KEY_COURSEID, courseId);
            data.put(KEY_ORDERNO, orderNo);
            data.put(KEY_SHAREURL,shareUrl);
            data.put(KEY_DESC,desc);
            data.put(KEY_ICON,icon);
            data.put(KEY_URL,url);
            data.put(KEY_CHAT,chatSid);
            data.put(KEY_PACK,packName);
            data.put(KEY_ACTION,action);
            data.put(KEY_CONTENT,content);
            data.put(KEY_TAXED,taxed);
            data.put(KEY_BODY_URL,body_url);
            data.put(KEY_MONEY,money);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public String getSubTitle() {
        return subTitle;
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

    public String getOrderNo() {
        return orderNo;
    }

    public int getPicType() {
        return picType;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getShareUrl() {
        return shareUrl;
    }



    public String getIcon() {
        return icon;
    }

    public String getAction() {
        return action;
    }

    public String getPackName() {
        return packName;
    }

    public String getDesc() {
        return desc;
    }

    public String getUrl() {
        return url;
    }

    public String getChatSid() {
        return chatSid;
    }

    public boolean isTaxed(){return taxed;}

    public String getBody_url () {return body_url;}

    public float getMoney () {return money;}
}
