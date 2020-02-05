package com.netease.nim.uikit.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.bumptech.glide.load.model.GlideUrl;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.recent.adapter.RecentContactAdapter;
import com.netease.nim.uikit.business.session.module.model.ReplyMsgData;
import com.netease.nim.uikit.common.ui.widget.MyToolbar;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.List;

public class CommonUtil {

    //TODO Base URL 加载头像时使用
    public static String BaseUrl = "http://www.classbro.com/";

    public static String fileName = ""; //im文件下载文件夹名称

    public static String classbroRobot = "1008611";
    public static String systemNotify = "10001";
    public static String appId = "";
    public static String appKey = "";
    public static String hwCertificateName = "teacherHwPush";
    public static String xmCertificateName = "";
    public static String gcmCertificateName = "";

    public static String mzCertificateName = "CLASSBRO_MZ_PUSH";
    public static String mzAppId = "111710";
    public static String mzappKey = "282bdd3a37ec4f898f47c5bbbf9d2369";

    public static int TEAC = 1;  //教师
    public static int STUD = 2;  //学生
    public static int SELLER = 3;//销售
    //当前已发送消息数量
    public static int sendMessageCount = 0;
    public static int role = 0;  //角色  1--教师   2--学生  3--销售

    public static MsgAttachment replyAttachment, forwardAttachment;
    //是否开启留言  0-未开启   1-开启
    public static int isOPeenMessage = 0;

    public static String userAccount; //当前登录用户

    public static void setRole(int lable) {
        role = lable;
    }

    public static String getAvatarUrl(String url) {
        if (null == url) {
            return url;
        }
        boolean sta = url.startsWith("http://");
        if (sta) {
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

    public static void setChatItemOnClicklistener(ChatItemOnClicklistener listener) {
        clicklistener = listener;
    }

    public interface ChatItemOnClicklistener {
        void onClick(View view, Object o);
    }


    //排课点击事件回调
    public static ScheduleClassOnClicklistener scheduleClassOnClicklistener;

    public static void setScheduleClassOnClicklistener(ScheduleClassOnClicklistener listener) {
        scheduleClassOnClicklistener = listener;
    }

    public interface ScheduleClassOnClicklistener {
        void onClick(long courseId);
    }

    //销售接待回调
    public static SellerAcceptOnClicklistener sellerAcceptOnClicklistener;

    public static void setSellerAcceptOnClicklistener(SellerAcceptOnClicklistener listener) {
        sellerAcceptOnClicklistener = listener;
    }

    public interface SellerAcceptOnClicklistener {
        void onClick(long courseId, String fromAccount);
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

    //添加讨论组成员回调
    public static AddMemberListener addMemberListener;

    public static void setAddMemberListener(AddMemberListener listener) {
        addMemberListener = listener;

    }

    public interface AddMemberListener {
        void addMember(Context context);
    }

    //添加用户信息回调
    public static AddUserInfoListener addUserInfoListener;

    public static void setAddUserInfoListener(AddUserInfoListener listener) {
        addUserInfoListener = listener;

    }

    public interface AddUserInfoListener {
        void addUserInfo(Activity context, String sessionId);
    }

    //查看聊天记录
    public static CheckHistoryMessageListener checkHistoryMessageListener;

    public static void setCheckHistoryMessageListener(CheckHistoryMessageListener listener) {
        checkHistoryMessageListener = listener;

    }

    public interface CheckHistoryMessageListener {
        void checkMessage(String wxNo, String sessionId);
    }

    //回复
    public static ReplyMessageListener replyMessageListener;

    public static void setReplyMessageListener(ReplyMessageListener listener) {
        replyMessageListener = listener;

    }

    public interface ReplyMessageListener {
        void replyMsg(IMMessage message, MsgTypeEnum msgType);
    }

    //回复
    public static ReplyListener replyListener;

    public static void getReplyeListener(ReplyListener listener) {
        replyListener = listener;

    }

    public interface ReplyListener {
        void getReply(ReplyMsgData replyMsgData);
    }

    //转发
    public static onForwardListener forwardListener;

    public static void setForwardListener(onForwardListener listener) {
        forwardListener = listener;

    }

    public interface onForwardListener {
        void onForward(IMMessage message, boolean isChecked);
    }

    //合并转发
    public static onMergeForwardListener mergeForwardListener;

    public static void setMergeForwardListener(onMergeForwardListener listener) {
        mergeForwardListener = listener;
    }

    public interface onMergeForwardListener {
        void mergeForward(List<IMMessage> messages, String content);
    }

    //未读数
    public static onUnReadNumListener unReadNumListener;

    public static void setUnReadNumListener(onUnReadNumListener listener) {
        unReadNumListener = listener;
    }

    public interface onUnReadNumListener {
        void underNum(int num);
    }

    //搜索联系人
    public static onSearchContactsListener onSearchContactsListener;

    public static void setOnSearchContactsListener(onSearchContactsListener listener) {
        onSearchContactsListener = listener;
    }

    public interface onSearchContactsListener {
        void onData(String query, Activity context);
    }

    //收藏
    public static onCollectionListener collectionListener;

    public static void setOnCollectionListener(onCollectionListener listener) {
        collectionListener = listener;
    }

    public interface onCollectionListener {
        void onCollection(IMMessage message, Activity context);
    }

    //获取好友的微信号
    public static onGetFriendWxIdListener getFriendWxIdListener;

    public static void setOnGetFriendWxIdListener(onGetFriendWxIdListener listener) {
        getFriendWxIdListener = listener;
    }

    public interface onGetFriendWxIdListener {
        void onFriendWxId(String wxNo, String sessionId, MyToolbar toolbar);
    }

    //媒体消息获取url
    public static onGetMediaUrlListener getMediaUrlListener;

    public static void setonGetMediaUrlListener(onGetMediaUrlListener listener) {
        getMediaUrlListener = listener;
    }

    public interface onGetMediaUrlListener {
        void onMediaUrl(IMMessage imMessage,Context context,String wxMsgId);
    }
}
