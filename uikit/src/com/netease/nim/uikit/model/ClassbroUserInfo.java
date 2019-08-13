package com.netease.nim.uikit.model;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by mike on 2019/8/9.
 * 用户数据记录
 */

public class ClassbroUserInfo extends SugarRecord implements Serializable {


    /**
     * userId : 880
     * mobile : 12421322112
     * wxAccount : 231321312
     * studentName : 大厦就打算
     * countryId : 61
     * professionalId : 4
     * universityId : 355
     * eduId : 4
     * schoolYear : 3
     * schoolAccount : 2312132
     * schoolPws : 1123312
     * spareTime : 00:45-01:00,,|,,|,,|00:30-00:15,,|,,|,,|,,
     * mobileCode : 1
     */
    private Long userId;
    private String mobile;
    private String wxAccount;
    private String studentName;
    private int countryId;
    private int professionalId;
    private int universityId;
    private int eduId;
    private int schoolYear;
    private String schoolAccount;
    private String schoolPws;

    private String country; //国家
    private String major;  //专业
    private String grade;  //年级
    private String education;  //学历

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    private String school;  //学校
    public long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWxAccount() {
        return wxAccount;
    }

    public void setWxAccount(String wxAccount) {
        this.wxAccount = wxAccount;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(int professionalId) {
        this.professionalId = professionalId;
    }

    public int getUniversityId() {
        return universityId;
    }

    public void setUniversityId(int universityId) {
        this.universityId = universityId;
    }

    public int getEduId() {
        return eduId;
    }

    public void setEduId(int eduId) {
        this.eduId = eduId;
    }

    public int getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(int schoolYear) {
        this.schoolYear = schoolYear;
    }

    public String getSchoolAccount() {
        return schoolAccount;
    }

    public void setSchoolAccount(String schoolAccount) {
        this.schoolAccount = schoolAccount;
    }

    public String getSchoolPws() {
        return schoolPws;
    }

    public void setSchoolPws(String schoolPws) {
        this.schoolPws = schoolPws;
    }

}
