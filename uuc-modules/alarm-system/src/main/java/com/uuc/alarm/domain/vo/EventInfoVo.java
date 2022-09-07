package com.uuc.alarm.domain.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class EventInfoVo {
    private Long id;
    /**
     * 告警策略
     */
    private String strategy;
    /**
     * 告警等级
     */
    private Integer priority;
    /**
     * 通知状态
     */
    private Integer notifyStatus;
    /**
     * 告警联系人
     */
    private List<String> users;
    /**
     * 告警组
     */
    private List<String> groups;
    /**
     * 告警状态
     */
    private String eventType;
    /**
     * 首次发生时间
     */
    private Date created;
    /**
     * 告警摘要描述
     */
    private String summary;
    /**
     * 告警策略id
     */
    private Long sid;
    /**
     * 告警策略组id
     */
    private Long straGroupId;
    /**
     * 关闭时间
     */
    private Long closeTime;
    /**
     * 关闭人
     */
    private String closer;
    /**
     * 指标名称
     */
    private String metricName;
}
