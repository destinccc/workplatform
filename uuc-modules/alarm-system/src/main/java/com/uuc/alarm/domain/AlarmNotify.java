package com.uuc.alarm.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uuc.common.core.annotation.Excel;
import com.uuc.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * alarm_notify对象 alarm_notify
 *
 * @author uuc
 * @date 2022-08-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AlarmNotify extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    private Long eventId;

    /**
     * 告警事件发生时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "告警事件发生时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date eventTime;

    /**
     * 发送告警通知时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发送告警通知时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date notifyTime;

    /**
     * 告警发送内容
     */
    @Excel(name = "告警发送内容")
    private String content;

    /**
     * 通知状态
     */
    @Excel(name = "通知状态")
    private Integer status;

    /**
     * 告警消息发送方式
     */
    @Excel(name = "告警消息发送方式")
    private String notifyWay;

    /**
     * 告警消息发送人
     */
    @Excel(name = "告警消息发送人")
    private String notifyUsers;

    /**
     * 告警消息发送组
     */
    @Excel(name = "告警消息发送组")
    private String notifyGroups;

    /**
     * 告警接收人
     */
    @Excel(name = "告警接收人")
    private String receiver;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date created;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updated;
}
