package com.uuc.alarm.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uuc.common.core.annotation.Excel;
import com.uuc.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * event对象 event
 *
 * @author uuc
 * @date 2022-08-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Event extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** sid */
    @Excel(name = "sid")
    private Long sid;

    /** name, 报警通知名称 */
    @Excel(name = "name, 报警通知名称")
    private String sname;

    /** node path */
    @Excel(name = "node path")
    private String nodePath;

    /** node id */
    @Excel(name = "node id")
    private Integer nid;

    /** endpoint */
    @Excel(name = "endpoint")
    private String endpoint;

    /** endpoint alias */
    @Excel(name = "endpoint alias")
    private String endpointAlias;

    /** cur_node_path */
    @Excel(name = "cur_node_path")
    private String curNodePath;

    /** cur_nid */
    @Excel(name = "cur_nid")
    private String curNid;

    /** 优先级 */
    @Excel(name = "优先级")
    private Integer priority;

    /** alert|recovery */
    @Excel(name = "alert|recovery")
    private String eventType;

    /** 1阈值 2智能 */
    @Excel(name = "1阈值 2智能")
    private Integer category;

    /** event status */
    @Excel(name = "event status")
    private Integer status;

    /** counter points pred_points 详情 */
    @Excel(name = "counter points pred_points 详情")
    private String detail;

    /** sid+counter hash */
    @Excel(name = "sid+counter hash")
    private String hashid;

    /** event ts */
    @Excel(name = "event ts")
    private Long etime;

    /** 当前值 */
    @Excel(name = "当前值")
    private String value;

    /** notify users */
    @Excel(name = "notify users")
    private String users;

    /** notify groups */
    @Excel(name = "notify groups")
    private String groups;

    /** runbook url */
    @Excel(name = "runbook url")
    private String runbook;

    /** strategy info */
    @Excel(name = "strategy info")
    private String info;

    /** strategy summary */
    @Excel(name = "strategy summary")
    private String summary;

    /** need upgrade */
    @Excel(name = "need upgrade")
    private Integer needUpgrade;

    /** alert upgrade */
    @Excel(name = "alert upgrade")
    private String alertUpgrade;

    /** created */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "created", width = 30, dateFormat = "yyyy-MM-dd")
    private Date created;

    /** 告警消息发送方式 */
    @Excel(name = "告警消息发送方式")
    private String notifySender;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date closeTime;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String closer;

    /** 资源id */
    @Excel(name = "资源id")
    private Long hostId;

    /** 告警次数 */
    @Excel(name = "告警次数")
    private Long alertTimes;

    /** 资源标识 */
    @Excel(name = "资源标识")
    private String hostIdent;

    /** 资源名称 */
    @Excel(name = "资源名称")
    private String hostName;

    /** 策略名称 */
    @Excel(name = "策略名称")
    private String strategy;

    /** 告警推送次数 */
    @Excel(name = "告警推送次数")
    private Integer notifyTimes;

    /** 最近告警推送时间 */
    @Excel(name = "最近告警推送时间")
    private Long notifyTime;
}
