package com.netease.nim.uikit.business.session.helper;

import android.text.TextUtils;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nimlib.sdk.msg.attachment.NotificationAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.constant.TeamAllMuteModeEnum;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.constant.VerifyTypeEnum;
import com.netease.nimlib.sdk.team.model.MemberChangeAttachment;
import com.netease.nimlib.sdk.team.model.MuteMemberAttachment;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.UpdateTeamAttachment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统消息描述文本构造器。主要是将各个系统消息转换为显示的文本内容。<br>
 * Created by huangjun on 2015/3/11.
 */
public class TeamNotificationHelper {

    //课程状态码
    public static final int CODE_1 = 1; //未处理
    public static final int CODE_2 = 2;  //待审核
    public static final int CODE_4 = 4;  //待确定
    public static final int CODE_8 = 8;  //待规划
    public static final int CODE_16 = 16;  //规划中(订单第一次超时等待运营接单)
    public static final int CODE_32 = 32; //规划中(订单第二次超时等待运营主管强制指派)
    public static final int CODE_64 = 64;  //规划中(运营接单待指派，正在处理)
    public static final int CODE_128 = 128;  //待排课
    public static final int CODE_256 = 256;  //已排课
    public static final int CODE_512 = 512;  //待结课
    public static final int CODE_1024 = 1024;  //已结课
    public static final int CODE_2048 = 2048;  //已强制结课
    public static final int CODE_4096 = 4096;  //已取消
    public static final int CODE_8192 = 8192;  //坏单申请中
    public static final int CODE_16384 = 16384;  //坏单已确认结束
    public static final int CODE_32768 = 32768;  //销售被更换
    public static final int CODE_65536 = 65536;  //事故单申请中
    public static final int CODE_131072 = 131072;  //事故单已处理
    public static final int CODE_262144 = 262144;  //事故单更换教师

    private static ThreadLocal<String> teamId = new ThreadLocal<>();

    public static String getMsgShowText(final IMMessage message) {
        String content = "";
        String messageTip = message.getMsgType().getSendMessageTip();
        if (messageTip.length() > 0) {
            content += "[" + messageTip + "]";
        } else {
            if (message.getSessionType() == SessionTypeEnum.Team && message.getAttachment() != null) {
                content += getTeamNotificationText(message, message.getSessionId());
            } else {
                content += message.getContent();
            }
        }

        return content;
    }

    public static String getTeamNotificationText(IMMessage message, String tid) {
        return getTeamNotificationText(message.getSessionId(), message.getFromAccount(), (NotificationAttachment) message.getAttachment());
    }

    public static String getTeamNotificationText(String tid, String fromAccount, NotificationAttachment attachment) {
        teamId.set(tid);
        String text = buildNotification(tid, fromAccount, attachment);
        teamId.set(null);
        return text;
    }

    private static String buildNotification(String tid, String fromAccount, NotificationAttachment attachment) {
        String text;
        switch (attachment.getType()) {
            case InviteMember:
                text = buildInviteMemberNotification(((MemberChangeAttachment) attachment), fromAccount);
                break;
            case KickMember:
                text = buildKickMemberNotification(((MemberChangeAttachment) attachment));
                break;
            case LeaveTeam:
                text = buildLeaveTeamNotification(fromAccount);
                break;
            case DismissTeam:
                text = buildDismissTeamNotification(fromAccount);
                break;
            case UpdateTeam:
                text = buildUpdateTeamNotification(tid, fromAccount, (UpdateTeamAttachment) attachment);
                break;
            case PassTeamApply:
                text = buildManagerPassTeamApplyNotification((MemberChangeAttachment) attachment);
                break;
            case TransferOwner:
                text = buildTransferOwnerNotification(fromAccount, (MemberChangeAttachment) attachment);
                break;
            case AddTeamManager:
                text = buildAddTeamManagerNotification((MemberChangeAttachment) attachment);
                break;
            case RemoveTeamManager:
                text = buildRemoveTeamManagerNotification((MemberChangeAttachment) attachment);
                break;
            case AcceptInvite:
                text = buildAcceptInviteNotification(fromAccount, (MemberChangeAttachment) attachment);
                break;
            case MuteTeamMember:
                text = buildMuteTeamNotification((MuteMemberAttachment) attachment);
                break;
            default:
                text = getTeamMemberDisplayName(fromAccount) + ": unknown message";
                break;
        }

        return text;
    }

    private static String getTeamMemberDisplayName(String account) {
        return TeamHelper.getTeamMemberDisplayNameYou(teamId.get(), account);
    }

    private static String buildMemberListString(List<String> members, String fromAccount) {
        StringBuilder sb = new StringBuilder();
        for (String account : members) {
            if (!TextUtils.isEmpty(fromAccount) && fromAccount.equals(account)) {
                continue;
            }
            sb.append(getTeamMemberDisplayName(account));
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    private static String buildInviteMemberNotification(MemberChangeAttachment a, String fromAccount) {
        StringBuilder sb = new StringBuilder();
        String selfName = getTeamMemberDisplayName(fromAccount);

        sb.append(selfName);
        sb.append("邀请 ");
        sb.append(buildMemberListString(a.getTargets(), fromAccount));
        Team team = NimUIKit.getTeamProvider().getTeamById(teamId.get());
        if (team == null || team.getType() == TeamTypeEnum.Advanced) {
            sb.append(" 加入群");
        } else {
            sb.append(" 加入讨论组");
        }

        return sb.toString();
    }

    private static String buildKickMemberNotification(MemberChangeAttachment a) {
        StringBuilder sb = new StringBuilder();
        sb.append(buildMemberListString(a.getTargets(), null));
        Team team = NimUIKit.getTeamProvider().getTeamById(teamId.get());
        if (team == null || team.getType() == TeamTypeEnum.Advanced) {
            sb.append(" 已被移出群");
        } else {
            sb.append(" 已被移出讨论组");
        }

        return sb.toString();
    }

    private static String buildLeaveTeamNotification(String fromAccount) {
        String tip;
        Team team = NimUIKit.getTeamProvider().getTeamById(teamId.get());
        if (team == null || team.getType() == TeamTypeEnum.Advanced) {
            tip = " 离开了群";
        } else {
            tip = " 离开了讨论组";
        }
        return getTeamMemberDisplayName(fromAccount) + tip;
    }

    private static String buildDismissTeamNotification(String fromAccount) {
        return getTeamMemberDisplayName(fromAccount) + " 解散了群";
    }

    private static String buildUpdateTeamNotification(String tid, String account, UpdateTeamAttachment a) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<TeamFieldEnum, Object> field : a.getUpdatedFields().entrySet()) {
            if (field.getKey() == TeamFieldEnum.Name) {
                sb.append("名称被更新为 " + field.getValue());
            } else if (field.getKey() == TeamFieldEnum.Introduce) {
                sb.append("群介绍被更新为 " + field.getValue());
            } else if (field.getKey() == TeamFieldEnum.Announcement) {
                sb.append(TeamHelper.getTeamMemberDisplayNameYou(tid, account) + " 修改了群公告");
            } else if (field.getKey() == TeamFieldEnum.VerifyType) {
                VerifyTypeEnum type = (VerifyTypeEnum) field.getValue();
                String authen = "群身份验证权限更新为";
                if (type == VerifyTypeEnum.Free) {
                    sb.append(authen + NimUIKit.getContext().getString(R.string.team_allow_anyone_join));
                } else if (type == VerifyTypeEnum.Apply) {
                    sb.append(authen + NimUIKit.getContext().getString(R.string.team_need_authentication));
                } else {
                    sb.append(authen + NimUIKit.getContext().getString(R.string.team_not_allow_anyone_join));
                }
            } else if (field.getKey() == TeamFieldEnum.Extension) {
                sb.append("群消息更新");
            } else if (field.getKey() == TeamFieldEnum.Ext_Server_Only) {
                Object value = field.getValue();
                String data = "";
                if (value != null) {
                    data = String.valueOf(value);
                }
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int code = jsonObject.getInt("courseStatus");
                    String content = initTip(code);
                    sb.append(content);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //    sb.append("群扩展字段(服务器)被更新为 " + field.getValue());
            } else if (field.getKey() == TeamFieldEnum.ICON) {
                sb.append("群头像已更新");
            } else if (field.getKey() == TeamFieldEnum.InviteMode) {
                sb.append("群邀请他人权限被更新为 " + field.getValue());
            } else if (field.getKey() == TeamFieldEnum.TeamUpdateMode) {
                sb.append("群资料修改权限被更新为 " + field.getValue());
            } else if (field.getKey() == TeamFieldEnum.BeInviteMode) {
                sb.append("群被邀请人身份验证权限被更新为 " + field.getValue());
            } else if (field.getKey() == TeamFieldEnum.TeamExtensionUpdateMode) {
                sb.append("群权限被更改");
            } else if (field.getKey() == TeamFieldEnum.AllMute) {
                TeamAllMuteModeEnum teamAllMuteModeEnum = (TeamAllMuteModeEnum) field.getValue();
                if (teamAllMuteModeEnum == TeamAllMuteModeEnum.Cancel) {
                    sb.append("取消群全员禁言");
                } else {
                    sb.append("群全员禁言");
                }
            } else {
                sb.append("群" + field.getKey() + "被更新为 " + field.getValue());
            }
            sb.append("\r\n");
        }
        if (sb.length() < 2) {
            return "未知通知";
        }
        return sb.delete(sb.length() - 2, sb.length()).toString();
    }

    private static String buildManagerPassTeamApplyNotification(MemberChangeAttachment a) {
        StringBuilder sb = new StringBuilder();
        sb.append("管理员通过用户 ");
        sb.append(buildMemberListString(a.getTargets(), null));
        sb.append(" 的入群申请");

        return sb.toString();
    }

    private static String buildTransferOwnerNotification(String from, MemberChangeAttachment a) {
        StringBuilder sb = new StringBuilder();
        sb.append(getTeamMemberDisplayName(from));
        sb.append(" 将群转移给 ");
        sb.append(buildMemberListString(a.getTargets(), null));

        return sb.toString();
    }

    private static String buildAddTeamManagerNotification(MemberChangeAttachment a) {
        StringBuilder sb = new StringBuilder();

        sb.append(buildMemberListString(a.getTargets(), null));
        sb.append(" 被任命为管理员");

        return sb.toString();
    }

    private static String buildRemoveTeamManagerNotification(MemberChangeAttachment a) {
        StringBuilder sb = new StringBuilder();

        sb.append(buildMemberListString(a.getTargets(), null));
        sb.append(" 被撤销管理员身份");

        return sb.toString();
    }

    private static String buildAcceptInviteNotification(String from, MemberChangeAttachment a) {
        StringBuilder sb = new StringBuilder();

        sb.append(getTeamMemberDisplayName(from));
        sb.append(" 接受了 ").append(buildMemberListString(a.getTargets(), null)).append(" 的入群邀请");

        return sb.toString();
    }

    private static String buildMuteTeamNotification(MuteMemberAttachment a) {
        StringBuilder sb = new StringBuilder();

        sb.append(buildMemberListString(a.getTargets(), null));
        sb.append("被管理员");
        sb.append(a.isMute() ? "禁言" : "解除禁言");

        return sb.toString();
    }

    /**
     * 服务器扩展字段tip信息
     *
     * @param code
     * @return
     */
    public static String initTip(int code) {
        String content = "";
        String BASE_CONTENT = "课程状态变更为:";
        switch (code) {
            case CODE_1:
                content = "未处理";
                break;
            case CODE_2:
                content = "待审核";
                break;
            case CODE_4:
                content = "待确定";
                break;
            case CODE_8:
                content = "待规划";
                break;
            case CODE_16:
                content = "规划中";
                break;
            case CODE_32:
                content = "规划中";
                break;
            case CODE_64:
                content = "规划中";
                break;
            case CODE_128:
                content = "待排课";
                break;
            case CODE_256:
                content = "已排课";
                break;
            case CODE_512:
                content = "待结课";
                break;
            case CODE_1024:
                content = "已结课";
                break;
            case CODE_2048:
                content = "已强制结课";
                break;
            case CODE_4096:
                content = "已取消";
                break;
            default:
                content = "异常";
                break;
        }
        return BASE_CONTENT + content;
    }
}
