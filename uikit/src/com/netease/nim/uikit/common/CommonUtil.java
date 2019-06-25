package com.netease.nim.uikit.common;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.recent.adapter.RecentContactAdapter;
import com.netease.nimlib.sdk.msg.model.RecentContact;

public class CommonUtil {

    //TODO Base URL 加载头像时使用
    public static String BaseUrl = "http://www.classbro.com/";

    public static String classbroRobot = "1008611";
    public static String systemNotify = "10001";
    public static String appId = "";
    public static String appKey = "";
    public static String hwCertificateName= "";
    public static String xmCertificateName= "";
    public static String gcmCertificateName= "";

    public static int TEAC = 1;  //教师
    public static int STUD = 2;  //学生
    public static int SELLER= 3;//销售
    //当前已发送消息数量
    public static int sendMessageCount = 0;
    public static int role = 0;  //角色  1--教师   2--学生  3--销售

    //是否开启留言  0-未开启   1-开启
    public static int isOPeenMessage = 0;
    public static void setRole (int lable) {
        role = lable;
    }

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


    //自定义消息item点击事件回调
    public static ChatItemOnClicklistener clicklistener;

    public static void setChatItemOnClicklistener(ChatItemOnClicklistener listener){
        clicklistener = listener;
    }

    public interface ChatItemOnClicklistener{
        void onClick(View view , Object o);
    }


    //排课点击事件回调
    public static ScheduleClassOnClicklistener scheduleClassOnClicklistener;
    public static void setScheduleClassOnClicklistener(ScheduleClassOnClicklistener listener){
        scheduleClassOnClicklistener = listener;
    }
    public interface ScheduleClassOnClicklistener{
        void onClick(long courseId);
    }

    //销售接待回调
    public static SellerAcceptOnClicklistener sellerAcceptOnClicklistener;
    public static void setSellerAcceptOnClicklistener(SellerAcceptOnClicklistener listener){
        sellerAcceptOnClicklistener = listener;
    }
    public interface SellerAcceptOnClicklistener{
        void onClick(long courseId,String fromAccount);
    }

    //右滑删除回调
    public static DeletedItemListener delectedItemListener;
    public static void setDelectedItemListener(DeletedItemListener listener) {
       delectedItemListener = listener;
    }
    public interface DeletedItemListener {
        void deleted(RecentContactAdapter adapter, int position, RecentContact recentContact);
    }

    //结束咨询删除回调
    public static MenuDeleteListener menuDeleteListener;
    public static void setDelectedItemListener(MenuDeleteListener listener) {
        menuDeleteListener = listener;
    }
    public interface MenuDeleteListener {
        void deleted(String contactId);
    }
}
