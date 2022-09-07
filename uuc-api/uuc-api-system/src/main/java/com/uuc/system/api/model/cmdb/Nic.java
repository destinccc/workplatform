package com.uuc.system.api.model.cmdb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author deng
 * @date 2022/7/15 0015
 * @description 网卡对象
 */
@Data
@ApiModel("网卡")
public class Nic {

    @ApiModelProperty("网卡名称")
    private String name;
    @ApiModelProperty("网卡id")
    private String id;
    @ApiModelProperty("网卡状态,取值为 up,down")
    private String status;
    @ApiModelProperty("MAC地址")
    private String macAddress;
    @ApiModelProperty("网卡的入口（下行）速率,保留小数点后2位,单位MbPS")
    private float inboundRate;//
    @ApiModelProperty("网卡的出口（上行）速率,保留小数点后2位,单位MbPS")
    private float outboundRate;
}
