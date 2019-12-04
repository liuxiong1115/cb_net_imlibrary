package com.netease.nim.uikit.business.recent.model;

import java.util.List;

/**
 * Created by mike on 2019/12/2.
 */

public class UserInfoExtension {

    /**
     * userType : 1
     * logStatus : 0
     * userId : 895
     * groupId : 0
     * wxNo : wxid_niyeowtd168b22
     * activa : 0
     * isInternal : 1
     * toplist : ["stud2968"]
     */

    private int userType;
    private int logStatus;
    private int userId;
    private int groupId;
    private String wxNo;
    private int activa;
    private int isInternal;
    private List<String> toplist;

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getLogStatus() {
        return logStatus;
    }

    public void setLogStatus(int logStatus) {
        this.logStatus = logStatus;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getWxNo() {
        return wxNo;
    }

    public void setWxNo(String wxNo) {
        this.wxNo = wxNo;
    }

    public int getActiva() {
        return activa;
    }

    public void setActiva(int activa) {
        this.activa = activa;
    }

    public int getIsInternal() {
        return isInternal;
    }

    public void setIsInternal(int isInternal) {
        this.isInternal = isInternal;
    }

    public List<String> getToplist() {
        return toplist;
    }

    public void setToplist(List<String> toplist) {
        this.toplist = toplist;
    }
}
