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
@ApiModel("网络设备")
public class NetworkDevice {
    @ApiModelProperty("网络设备的 id")
    private String id;
    @ApiModelProperty("网络设备的名称")
    private String name;
    @ApiModelProperty("网络设备的描述")
    private String description;
    @ApiModelProperty("网络设备的管理 ip")
    private String ipAddress;
    @ApiModelProperty("设备型号")
    private String model;
    @ApiModelProperty("设备生产厂商")
    private String vendor;
    @ApiModelProperty("设备类型")
    private String deviceType;
    @ApiModelProperty("内存大小,单位 MB")
    private int memorySize;
    @ApiModelProperty("网卡数,单位 个")
    private int portNum;
}
