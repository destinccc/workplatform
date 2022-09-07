package com.uuc.system.api.model.cmdb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author deng
 * @date 2022/7/15 0015
 * @description 磁盘对象
 */
@Data
@ApiModel("磁盘对象")
public class Disk {
    @ApiModelProperty("磁盘名称")
    private String name;
    @ApiModelProperty("磁盘ID")
    private String id;
    @ApiModelProperty("磁盘容量,单位GB")
    private int capacity;
    @ApiModelProperty("磁盘使用量,单位GB")
    private float usage;
    @ApiModelProperty("磁盘使用率,保留小数点后2位,单位%")
    private float usageRate;
    @ApiModelProperty("磁盘读iops,单位 次")
    private int readIops;
    @ApiModelProperty("磁盘写iops,单位 次")
    private int writeIops;
    @ApiModelProperty("磁盘读吞吐量,保留小数点后2位，单位MB")
    private float readThroughput;
    @ApiModelProperty("磁盘写吞吐量,保留小数点后2位，单位MB")
    private float writeThroughput;
    @ApiModelProperty("磁盘读速率,保留小数点后2位，单位MB/S")
    private float readRate;
    @ApiModelProperty("磁盘写速率,保留小数点后2位，单位 MB/S")
    private float writeRate;
    @ApiModelProperty("磁盘分类")
    private String category;
    @ApiModelProperty("磁盘类型")
    private String type;
    @ApiModelProperty("磁盘挂载点")
    private String mountPoint;
    @ApiModelProperty("设备")
    private String device;

}
