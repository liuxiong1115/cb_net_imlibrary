package com.netease.nim.uikit.business.uinfo;

import android.text.TextUtils;
import android.util.Log;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.common.CommonUtil;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;

public class UserInfoHelper {

    // 获取用户显示在标题栏和最近联系人中的名字
    public static String getUserTitleName(String id, SessionTypeEnum sessionType) {
        if (sessionType == SessionTypeEnum.P2P) {
            if (NimUIKit.getAccount().equals(id)) {
                return "我的电脑";
            } else {
                return getUserDisplayName(id);
            }
        } else if (sessionType == SessionTypeEnum.Team) {
            return TeamHelper.getTeamName(id);
        }
        return id;
    }

    /**
     * @param account 用户帐号
     * @return
     */
    public static String getUserDisplayName(String account) {
        if (CommonUtil.role == CommonUtil.SELLER) {
            if (CommonUtil.classbroRobot.equals(account)) {
                return "系统通知";
            }
        }
        String alias = NimUIKit.getContactProvider().getAlias(account);
        if (!TextUtils.isEmpty(alias)) {
            return alias;
        } else {
            NimUserInfo userInfo = (NimUserInfo) NimUIKit.getUserInfoProvider().getUserInfo(account);
            String content = userInfo.getExtension();
            Log.e("content",content == null ?"" :content);
            if (userInfo != null && !TextUtils.isEmpty(userInfo.getName())) {
                return userInfo.getName();
            } else {
                return account;
            }
        }

    }

    // 获取用户原本的昵称
    public static String getUserName(String account) {
        UserInfo userInfo = NimUIKit.getUserInfoProvider().getUserInfo(account);
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getName())) {
            return userInfo.getName();
        } else {
            return account;
        }
    }

    /**
     * @param account         账号
     * @param selfNameDisplay 如果是自己，则显示内容
     * @return
     */
    public static String getUserDisplayNameEx(String account, String selfNameDisplay) {
        if (account.equals(NimUIKit.getAccount())) {
            return selfNameDisplay;
        }

        return getUserDisplayName(account);
    }
}
