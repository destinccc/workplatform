package com.uuc.alarm.domain.vo;

import lombok.Data;

/**
 * alarm_event_log对象 alarm_event_log
 *
 * @author uuc
 * @date 2022-08-01
 */
@Data
public class AlarmEventLogListVo {
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 告警事件ID */
    private Long eventId;

    /** 告警扭转状态 */
    private Integer status;

    /** 扭转时间时间 */
    private Long logTime;

    /** 当前值 */
    private String value;

    /** 告警消息发送方式 */
    private String notifyWay;

    /** 信息 */
    private String content;
}
