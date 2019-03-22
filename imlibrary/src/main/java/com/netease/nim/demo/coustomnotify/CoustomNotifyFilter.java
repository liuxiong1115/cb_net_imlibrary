package com.netease.nim.demo.coustomnotify;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.netease.nim.demo.DemoCache;
import com.netease.nim.demo.session.activity.NotifyActivity;
import com.netease.nim.demo.session.extension.NotifyAttchment;
import com.netease.nim.demo.session.extension.RedPacketOpenedAttachment;
import com.netease.nim.uikit.common.CommonUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 自定义消息通知观察者
 */


public class CoustomNotifyFilter {
    private static Context context;
    private static Map<String, IMMessage> delete = new HashMap<>();
    private static Observer<List<IMMessage>> messageObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(final List<IMMessage> imMessages) {
            if (imMessages != null) {
                Iterator<IMMessage> iterator = imMessages.iterator();
                while (iterator.hasNext()) {
                    final IMMessage imMessage = iterator.next();
                    if (shouldFilter(imMessage)) {
                        delete.put(imMessage.getUuid(), imMessage);
                        // 过滤掉，其他观察者不会再收到了
                        iterator.remove();
                  //      NotifyAttchment attachment = (NotifyAttchment) imMessages.get(0).getAttachment();
                        if (CommonUtil.role == CommonUtil.SELLER) {  //新访客权限 -- 销售
                            KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
                            if (km.inKeyguardRestrictedInputMode()) {
                                // 处于锁屏状态
                                if (isForeground(context, NotifyActivity.class.getName()) == false) {
                                    Intent intent = new Intent(context, NotifyActivity.class);
                                    context.startActivity(intent);
                                }
                            }

                        }
                    }
                }
            }
        }
    };

    private static boolean shouldFilter(IMMessage message) {
        if (message != null && message.getAttachment() instanceof NotifyAttchment) {
            return true;
        }
        return false;
    }

    public static void startFilter(Context c) {
        context = c;
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(messageObserver, true);
    }

    public static void stopFilter() {
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(messageObserver, false);
    }

    /**
     * 判断某个界面是否在前台,返回true，为显示,否则不是
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName()))
                return true;
        }
        return false;
    }
}
