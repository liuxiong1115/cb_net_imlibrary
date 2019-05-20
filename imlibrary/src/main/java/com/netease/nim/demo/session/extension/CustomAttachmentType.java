package com.netease.nim.demo.session.extension;

/**
 * Created by zhoujianghua on 2015/4/9.
 */
public interface CustomAttachmentType {
    // 多端统一
    int Guess = 1;  //猜拳
    int SnapChat = 2; //阅后即焚
    int Sticker = 3;  //贴纸
    int RTS = 4;
    int RedPacket = 5;
    int OpenedRedPacket = 6;

    int ConfirmLetter = 301;  //确认函
    int Bill = 302;  //账单

    int ClassRoom = 303; //确认课堂
    int ClassRoom1 = 304; //更改课堂
    int ClassRoom2 = 305; //取消排课
    int ClassRoom3 = 306; //赠送时长
    int ClassRoom4 = 307;  //开始上课
    int Course = 308; //评价课程

    int notify = 309; //新访客

    int  findTeacher= 310;//钉下老师
    int  classHour = 311; //课时不足
    int NewOrder = 312; //新订单
    int Extend = 313; //扩展字段
}
