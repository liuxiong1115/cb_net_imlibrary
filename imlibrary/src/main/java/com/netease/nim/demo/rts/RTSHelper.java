package com.netease.nim.demo.rts;

/**
 * Created by winnie on 2018/3/26.
 */

public class RTSHelper {
    public static void init() {
        setRtsEventListener();
        setLogUtil();
        setUserInfoProvider();
    }

    // 设置rts事件监听器
    private static void setRtsEventListener() {
//        RTSKit.setRTSEventListener(new RTSEventListener() {
//            @Override
//            public void onRTSStartSuccess(String account) {
//                RTSAttachment attachment = new RTSAttachment((byte) 0);
//                IMMessage msg = MessageBuilder.createCustomMessage(account, SessionTypeEnum.P2P, attachment.getContent(), attachment);
//                MessageListPanelHelper.getInstance().notifyAddMessage(msg); // 界面上add一条
//                NIMClient.getService(MsgService.class).sendMessage(msg, false); // 发送给对方
//            }
//
//            @Override
//            public void onRTSFinish(String account, boolean selfFinish) {
//
//                RTSAttachment attachment = new RTSAttachment((byte) 1);
//
//                IMMessage msg = MessageBuilder.createCustomMessage(account, SessionTypeEnum.P2P, attachment.getContent(), attachment);
//                if (!selfFinish) {
//                    // 被结束会话，在这里模拟一条接收的消息
//                    msg.setFromAccount(account);
//                    msg.setDirect(MsgDirectionEnum.In);
//                }
//
//                msg.setStatus(MsgStatusEnum.success);
//
//                NIMClient.getService(MsgService.class).saveMessageToLocal(msg, true);
//            }
//        });
    }

    // 设置日志系统
    private static void setLogUtil() {
//        RTSKit.setiLogUtil(new ILogUtil() {
//            @Override
//            public void ui(String msg) {
//                LogUtil.ui(msg);
//            }
//
//            @Override
//            public void e(String tag, String msg) {
//                LogUtil.e(tag, msg);
//            }
//
//            @Override
//            public void i(String tag, String msg) {
//                LogUtil.i(tag, msg);
//            }
//
//            @Override
//            public void d(String tag, String msg) {
//                LogUtil.d(tag, msg);
//            }
//        });
    }

    // 设置用户相关资料提供者
    private static void setUserInfoProvider() {
//        RTSKit.setUserInfoProvider(new com.netease.nim.rtskit.api.IUserInfoProvider() {
//            @Override
//            public UserInfo getUserInfo(String account) {
//                return NimUIKit.getUserInfoProvider().getUserInfo(account);
//            }
//
//            @Override
//            public String getUserDisplayName(String account) {
//                return UserInfoHelper.getUserDisplayName(account);
//            }
//        });
    }
}
