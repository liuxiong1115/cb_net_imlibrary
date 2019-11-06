package com.netease.nim.demo.session.viewholder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.netease.nim.avchatkit.AVChatKit;
import com.netease.nim.avchatkit.AVChatProfile;
import com.netease.nim.avchatkit.TeamAVChatProfile;
import com.netease.nim.avchatkit.activity.AVChatActivity;
import com.netease.nim.avchatkit.teamavchat.activity.TeamAVChatActivity;
import com.netease.nim.demo.DemoCache;
import com.netease.nim.demo.R;
import com.netease.nim.demo.file.FileIcons;
import com.netease.nim.demo.session.action.TeamAVChatAction;
import com.netease.nim.demo.session.activity.FileDownloadActivity;
import com.netease.nim.demo.session.extension.CustomAttachmentType;
import com.netease.nim.demo.session.extension.CustomAvChatAttachment;
import com.netease.nim.demo.session.extension.ReplyAttachment;
import com.netease.nim.demo.session.utils.GlideUtils;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.SimpleCallback;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.business.session.emoji.MoonUtil;
import com.netease.nim.uikit.business.session.emoji.StickerManager;
import com.netease.nim.uikit.business.session.module.Container;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.common.CommonUtil;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.util.file.FileUtil;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.log.sdk.util.FileUtils;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.model.AVChatChannelInfo;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.CustomNotificationConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.model.TeamMember;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoujianghua on 2015/8/4.
 */
public class MsgViewHolderAvchatCallCustom extends MsgViewHolderBase {
    protected TextView bodyTextView;
    private LaunchTransaction transaction;


    public MsgViewHolderAvchatCallCustom(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return com.netease.nim.uikit.R.layout.nim_message_item_text;
    }

    @Override
    protected void inflateContentView() {
        bodyTextView = findViewById(com.netease.nim.uikit.R.id.nim_message_item_text_body);
    }

    @Override
    protected void bindContentView() {
        layoutDirection();
        bodyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getDisplayText().contains(context.getResources().getString(R.string.avchat_initiate_call))) {
                    startAudioVideoCall(AVChatType.VIDEO);
                } else {
                    onItemClick();
                }
            }
        });
     //   if (getDisplayText().contains(context.getResources().getString((R.string.avchat_initiate_call)))) {
            bodyTextView.setText(Html.fromHtml(getDisplayText().replace("点击重拨","<font color=#3a9efb>点击重拨</font>")));
//        } else{
//            MoonUtil.identifyFaceExpression(NimUIKit.getContext(), bodyTextView, getDisplayText(), ImageSpan.ALIGN_BOTTOM);
//        }
        bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
//        bodyTextView.setOnLongClickListener(longClickListener);
    }

    private void layoutDirection() {
        if (isReceivedMessage()) {
            bodyTextView.setBackgroundResource(NimUIKitImpl.getOptions().messageLeftBackground);
            bodyTextView.setTextColor(Color.BLACK);
            bodyTextView.setPadding(ScreenUtil.dip2px(20), ScreenUtil.dip2px(10), ScreenUtil.dip2px(20), ScreenUtil.dip2px(10));
        } else {
            bodyTextView.setBackgroundResource(NimUIKitImpl.getOptions().messageLeftBackground);
        //    bodyTextView.setTextColor(Color.WHITE);
            bodyTextView.setPadding(ScreenUtil.dip2px(20), ScreenUtil.dip2px(10), ScreenUtil.dip2px(20), ScreenUtil.dip2px(10));
        }
    }

    @Override
    protected int leftBackground() {
        return 0;
    }

    @Override
    protected int rightBackground() {
        return 0;
    }

    protected String getDisplayText() {
        return message.getContent();
    }

    public void startAudioVideoCall(AVChatType avChatType) {

        if (AVChatProfile.getInstance().isAVChatting()) {
            Toast.makeText(context, "正在进行P2P视频通话，请先退出", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TeamAVChatProfile.sharedInstance().isTeamAVChatting()) {
            // 视频通话界面正在运行，singleTop所以直接调起来
            Intent localIntent = new Intent();
            localIntent.setClass(context, TeamAVChatActivity.class);
            localIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(localIntent);
            return;
        }

        if (transaction != null) {
            return;
        }

        final String tid = message.getSessionId();
        if (TextUtils.isEmpty(tid)) {
            return;
        }
        transaction = new LaunchTransaction();
        transaction.setTeamID(tid);

        // load 一把群成员
        NimUIKit.getTeamProvider().fetchTeamMemberList(tid, new SimpleCallback<List<TeamMember>>() {
            @Override
            public void onResult(boolean success, List<TeamMember> result, int code) {
                // 检查下 tid 是否相等
                if (!checkTransactionValid()) {
                    return;
                }
                final List<String> studList = new ArrayList<>();
                final List<String> teacList = new ArrayList<>();
                if (success && result != null) {
                    for (int i = 0; i < result.size(); i++) {
                        if (result.get(i).getAccount().toLowerCase().startsWith("stud")) {
                            studList.add(result.get(i).getAccount());
                        } else if (result.get(i).getAccount().toLowerCase().startsWith("teac")) {
                            teacList.add(result.get(i).getAccount());
                        }
                    }
                    if (studList.size() == 0 || studList.size() > 1) {
                        Toast.makeText(context, "特殊订单暂不支持语音通话", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    final String roomName = StringUtil.get32UUID();
                    LogUtil.ui("create room " + roomName);
                    // 创建房间
                    AVChatManager.getInstance().createRoom(roomName, null, new AVChatCallback<AVChatChannelInfo>() {
                        @Override
                        public void onSuccess(AVChatChannelInfo avChatChannelInfo) {
                            LogUtil.ui("create room " + roomName + " success !");
                            if (!checkTransactionValid()) {
                                return;
                            }
                            if (CommonUtil.role == CommonUtil.TEAC) {
                                onCreateRoomSuccess(roomName, studList);
                            } else {
                                onCreateRoomSuccess(roomName, teacList);
                            }
                            transaction.setRoomName(roomName);

                            String teamName = TeamHelper.getTeamName(transaction.getTeamID());

                            TeamAVChatProfile.sharedInstance().setTeamAVChatting(true);
                            if (studList.size() == 0 || teacList.size() == 0) {
                                ToastHelper.showToast(context, "暂无可通话人员");
                            } else {
                                AVChatKit.outgoingTeamCall(context, false, transaction.getTeamID(), roomName, studList, teacList, teamName, CommonUtil.role);
                                transaction = null;
                            }
                        }

                        @Override
                        public void onFailed(int code) {
                            if (!checkTransactionValid()) {
                                return;
                            }
                            //onCreateRoomFail();
                        }

                        @Override
                        public void onException(Throwable exception) {
                            if (!checkTransactionValid()) {
                                return;
                            }
                            // onCreateRoomFail();
                        }
                    });
//                    if (result.size() < 2) {
//                        transaction = null;
//                        Toast.makeText(getActivity(), getActivity().getString(R.string.t_avchat_not_start_with_less_member), Toast.LENGTH_SHORT).show();
//                    } else {
//                        NimUIKit.startContactSelector(getActivity(), getContactSelectOption(tid), TeamRequestCode.REQUEST_TEAM_VIDEO);
//                    }
                }
            }
        });
    }
    private void onCreateRoomSuccess(String roomName, List<String> accounts) {
        String teamID = transaction.getTeamID();
        // 在群里发送tip消息
//        IMMessage message = MessageBuilder.createTipMessage(teamID, SessionTypeEnum.Team);
//        CustomMessageConfig tipConfig = new CustomMessageConfig();
//        tipConfig.enableHistory = false;
//        tipConfig.enableRoaming = false;
//        tipConfig.enablePush = false;
        String teamNick = TeamHelper.getDisplayNameWithoutMe(teamID, DemoCache.getAccount());
        //    message.setContent(teamNick + getActivity().getString(R.string.t_avchat_start));
//        message.setConfig(tipConfig);
//        sendMessage(message);
        // 对各个成员发送点对点自定义通知
        String teamName = TeamHelper.getTeamName(transaction.getTeamID());
        String content = TeamAVChatProfile.sharedInstance().buildContent(roomName, teamID, accounts, teamName);
        CustomNotificationConfig config = new CustomNotificationConfig();
        config.enablePush = true;
        config.enablePushNick = true;
        config.enableUnreadCount = true;
        for (String account : accounts) {
            CustomNotification command = new CustomNotification();
            command.setSessionId(account);
            command.setSessionType(SessionTypeEnum.P2P);
            command.setConfig(config);
            command.setContent(content);
            command.setApnsText(teamNick + context.getString(R.string.t_avchat_push_content));
            command.setSendToOnlineUserOnly(false);
            NIMClient.getService(MsgService.class).sendCustomNotification(command);
        }
    }
    public void onSelectedAccountFail() {
        transaction = null;
    }

    public void onSelectedAccountsResult(final ArrayList<String> accounts) {
        LogUtil.ui("start teamVideo " + context + " accounts = " + accounts);

        if (!checkTransactionValid()) {
            return;
        }

        final String roomName = StringUtil.get32UUID();
        LogUtil.ui("create room " + roomName);
        // 创建房间
        AVChatManager.getInstance().createRoom(roomName, null, new AVChatCallback<AVChatChannelInfo>() {
            @Override
            public void onSuccess(AVChatChannelInfo avChatChannelInfo) {
                LogUtil.ui("create room " + roomName + " success !");
                if (!checkTransactionValid()) {
                    return;
                }
                onCreateRoomSuccess(roomName, accounts);
                transaction.setRoomName(roomName);

                String teamName = TeamHelper.getTeamName(transaction.getTeamID());

                TeamAVChatProfile.sharedInstance().setTeamAVChatting(true);
                //       AVChatKit.outgoingTeamCall(getActivity(), false, transaction.getTeamID(), roomName, accounts, teamName);
                transaction = null;
            }

            @Override
            public void onFailed(int code) {
                if (!checkTransactionValid()) {
                    return;
                }
                onCreateRoomFail();
            }

            @Override
            public void onException(Throwable exception) {
                if (!checkTransactionValid()) {
                    return;
                }
                onCreateRoomFail();
            }
        });
    }

    private boolean checkTransactionValid() {
        if (transaction == null) {
            return false;
        }
        if (transaction.getTeamID() == null || !transaction.getTeamID().equals(message.getSessionId())) {
            transaction = null;
            return false;
        }
        return true;
    }
    private void onCreateRoomFail() {
        // 本地插一条tip消息
        IMMessage message = MessageBuilder.createTipMessage(transaction.getTeamID(), SessionTypeEnum.Team);
        message.setContent(context.getString(R.string.t_avchat_create_room_fail));
        LogUtil.i("status", "team action set:" + MsgStatusEnum.success);
        message.setStatus(MsgStatusEnum.success);
        NIMClient.getService(MsgService.class).saveMessageToLocal(message, true);
    }

    private class LaunchTransaction implements Serializable {
        private String teamID;
        private String roomName;

        public String getRoomName() {
            return roomName;
        }

        public String getTeamID() {
            return teamID;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }

        public void setTeamID(String teamID) {
            this.teamID = teamID;
        }
    }
}
