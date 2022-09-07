package com.uuc.alarm.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @author HI
 */
@Data
public class AlarmNotifyListDto {

    /** 事件id */
    private Long eventId;

    /** 告警事件发生时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Long startEventTime;

    /** 告警事件发生时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Long endEventTime;

    /** 发送告警通知时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Long startNotifyTime;

    /** 发送告警通知时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Long endNotifyTime;

    /** 通知状态 */
    private Integer status;

    /** 告警消息发送方式 */
    private String notifyWay;

    private Integer pageNum;
    private Integer pageSize;
}
