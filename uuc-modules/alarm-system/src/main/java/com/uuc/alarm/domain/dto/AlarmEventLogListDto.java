package com.uuc.alarm.domain.dto;

import lombok.Data;

/**
 * alarm_event_log对象 alarm_event_log
 *
 * @author uuc
 * @date 2022-08-01
 */
@Data
public class AlarmEventLogListDto {
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 告警事件ID */
    private Long eventId;

    /** 告警扭转状态 */
    private Integer status;

    /** 扭转时间时间 */
    private Long startTime;

    /** 扭转时间时间 */
    private Long endTime;

    /** 当前值 */
    private String value;

    /** 告警消息发送方式 */
    private String notifyWay;

    private Integer pageNum;

    private Integer pageSize;
}
