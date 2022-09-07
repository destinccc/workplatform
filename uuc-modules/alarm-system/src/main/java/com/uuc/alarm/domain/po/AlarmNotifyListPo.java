package com.uuc.alarm.domain.po;

import lombok.Data;

import java.util.Date;

/**
 * @author HI
 */
@Data
public class AlarmNotifyListPo {

    /** id */
    private Long id;

    /** 事件id */
    private Long eventId;

    /** 告警事件发生时间 */
    private Date eventTime;

    /** 发送告警通知时间 */
    private Date notifyTime;

    /** 告警发送内容 */
    private String content;

    /** 通知状态 */
    private Integer status;

    /** 告警消息发送方式 */
    private String notifyWay;

    /** 告警消息发送人 */
    private String notifyUsers;

    /** 告警消息发送组 */
    private String notifyGroups;
}
