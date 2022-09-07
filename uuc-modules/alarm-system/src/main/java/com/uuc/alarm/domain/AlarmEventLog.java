package com.uuc.alarm.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uuc.common.core.annotation.Excel;
import com.uuc.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * alarm_event_log对象 alarm_event_log
 *
 * @author uuc
 * @date 2022-08-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AlarmEventLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 告警事件ID */
    @Excel(name = "告警事件ID")
    private Long eventId;

    /** 告警扭转状态 */
    @Excel(name = "告警扭转状态")
    private Integer status;

    /** 扭转时间时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "扭转时间时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date logTime;

    /** 当前值 */
    @Excel(name = "当前值")
    private String value;

    /** 告警消息发送方式 */
    @Excel(name = "告警消息发送方式")
    private String notifyWay;

    /** 信息 */
    @Excel(name = "信息")
    private String content;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date created;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updated;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
}
