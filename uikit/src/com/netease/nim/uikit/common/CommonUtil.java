package com.netease.nim.uikit.common;

import com.netease.nimlib.sdk.msg.model.RecentContact;

public class CommonUtil {

    //TODO Base URL 加载头像时使用
    public static String BaseUrl = "http://www.classbro.com/";


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
