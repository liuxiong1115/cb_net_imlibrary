package com.netease.nim.uikit.common;

import com.netease.nimlib.sdk.msg.model.RecentContact;

public class CommonUtil {

    //TODO Base URL 加载头像时使用
    public static String BaseUrl = "http://www.classbro.com/";

    public static String classbroRobot = "1008611";

    //当前已发送消息数量
    public static int sendMessageCount = 0;
    //是否开启留言  0-未开启   1-开启
    public static int isOPeenMessage = 0;


    public static String getAvatarUrl(String url){
        if(null == url){
            return url;
        }
        boolean sta = url.startsWith("http://");
        if(sta){
            return url;
        }
        return CommonUtil.BaseUrl + url;
    }


    public static void addTag(RecentContact recent, long tag) {
        tag = recent.getTag() | tag;
        recent.setTag(tag);
    }

    public static void removeTag(RecentContact recent, long tag) {
        tag = recent.getTag() & ~tag;
        recent.setTag(tag);
    }

    public static boolean isTagSet(RecentContact recent, long tag) {
        return (recent.getTag() & tag) == tag;
    }
}
