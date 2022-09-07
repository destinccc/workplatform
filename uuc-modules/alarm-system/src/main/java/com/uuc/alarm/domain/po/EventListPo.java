package com.uuc.alarm.domain.po;

import lombok.Data;

import java.util.Date;

/**
 * @description 告警列表PO
 * @author llb
 * @since 2022/7/29 10:06
 */
@Data
public class EventListPo {
    private Long id;
    private Long resourceId;
    private String resourceName;
    private Integer priority;
    private Long etime;
    private Long strategyId;
    private String strategy;
    private String summary;
    private Integer alarmTimes;
    private String eventType;
    private Integer notifyStatus;
    private String notifyGroup;
    private String endpoint;
    private Long notifyTime;
    private String notifyUser;
    private Date closeTime;
    private String closer;
}
