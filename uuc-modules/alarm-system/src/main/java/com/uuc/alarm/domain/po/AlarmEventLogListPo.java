package com.uuc.alarm.domain.po;

import com.uuc.common.core.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * alarm_event_log对象 alarm_event_log
 *
 * @author uuc
 * @date 2022-08-01
 */
@Data
public class AlarmEventLogListPo {
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
}
