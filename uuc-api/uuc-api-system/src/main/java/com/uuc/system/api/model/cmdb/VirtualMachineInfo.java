package com.uuc.system.api.model.cmdb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author deng
 * @date 2022/7/15 0015
 * @description
 */
@Data
@ApiModel("虚拟机详情")
public class VirtualMachineInfo {

    @ApiModelProperty("虚机唯一标识")
    private String id;
    @ApiModelProperty("虚机名称")
    private String name;
    @ApiModelProperty("状态,RUNNING运行、SHUTDOWN关机、ABNORMAL异常、SUSPENDING挂起")
    private String state;
    @ApiModelProperty("用途说明")
    private String purpose;
    @ApiModelProperty("cpu核数")
    private Integer vcpuCount;
    @ApiModelProperty("内存大小,单位GB,保留两位小数,注：单位G")
    private double memorySize;
    @ApiModelProperty("系统盘类型,SSD云盘,LOCAL_SSD 本地盘,SATA 高效云盘")
    private String rootDiskType;
    @ApiModelProperty("系统盘大小,单位GB")
    private Integer rootDiskCapacity;
    @ApiModelProperty("数据盘")
    private List<DataDisk> dataDisks;
    @ApiModelProperty("数据盘总大小,单位GB")
    private Integer dataDiskTotalSize;
    @ApiModelProperty("GPU数量")
    private Integer gpuCount;
    @ApiModelProperty("GPU型号")
    private String gpuType;
    @ApiModelProperty("内部IP")
    private String fixIp;
    @ApiModelProperty("弹性ip")
    private String eip;
    @ApiModelProperty("弹性ip带宽")
    private Integer bandWidth;
    @ApiModelProperty("创建时间")
    private String createDate;
    @ApiModelProperty("到期时间")
    private String expireDate;
    @ApiModelProperty("操作系统")
    private String osVersion;
    @ApiModelProperty("可用区")
    private String availabilityZoneName;
    @ApiModelProperty("计费方式,PAY_BY_YEAR包年,PAY_BY_MONTH包月,PAY_BY_HOUR按小时计费,PAY_BY_VOLUME按量计费")
    private String chargeType;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("是否有exporter")
    private Boolean hasExpoter=false;
}

