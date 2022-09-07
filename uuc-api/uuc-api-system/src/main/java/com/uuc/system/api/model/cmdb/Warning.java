package com.uuc.system.api.model.cmdb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author deng
 * @date 2022/7/15 0015
 * @description
 */
@Data
@ApiModel("告警对象")
public class Warning {
    @ApiModelProperty("告警id")
    private String id;
    @ApiModelProperty("产生告警的策略id")
    private String strategyId;
    @ApiModelProperty("产生告警的规则id")
    private String ruleId;
    @ApiModelProperty("告警名称")
    private String name;
    @ApiModelProperty("告警类型")
    private String type;
    @ApiModelProperty("描述信息")
    private String description;
    @ApiModelProperty("告警级别,取值为 P0-灾难；P1-紧急；P2-告警；P3-通知")
    private String level;
    @ApiModelProperty("虚拟机的id")
    private String serverId;
    @ApiModelProperty("虚拟机的名称")
    private String serverName;
    @ApiModelProperty("告警生成时间")
    private String created;
    @ApiModelProperty("告警状态,ACTIVE活跃，CLOSE关闭")
    private String status;
    @ApiModelProperty("告警通知时间")
    private String notifyTime;
    @ApiModelProperty("告警内容摘要")
    private String detail;
}
