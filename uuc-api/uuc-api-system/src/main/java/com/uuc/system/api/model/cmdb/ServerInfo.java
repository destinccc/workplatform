package com.uuc.system.api.model.cmdb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author deng
 * @date 2022/7/15 0015
 * @description
 */
@Data
@ApiModel("虚拟机/物理机对象")
public class ServerInfo {
    @ApiModelProperty("对象id")
    private String serverId;
    @ApiModelProperty("cpu核数")
    private Integer cpuCoreNum;
    @ApiModelProperty("cpu负载")
    private Integer cpuLoad;
    @ApiModelProperty("cpu使用率,保留小数点后2位,单位 %")
    private Float cpuUsageRate;
    @ApiModelProperty("cpu利用率,保留小数点后2位,单位 %")
    private Float cpuUtilization;
    @ApiModelProperty("内存总量,单位GB")
    private Float memCapacity;
    @ApiModelProperty("内存总使用量,单位GB")
    private Float memUsage;
    @ApiModelProperty("内存总剩余量,单位GB")
    private Float memRemaining;
    @ApiModelProperty("内存总使用率,保留小数点后2位,单位 %")
    private Float memUsageRate;
    @ApiModelProperty("磁盘总量,单位GB")
    private Float diskCapacity;
    @ApiModelProperty("磁盘总使用量,单位GB")
    private Float diskUsage;
    @ApiModelProperty("磁盘总剩余量,单位GB")
    private Float diskRemaining;
    @ApiModelProperty("磁盘总使用率,保留小数点后2位,单位 %")
    private Float diskUsageRate;
    @ApiModelProperty("系统盘总量,单位GB")
    private Float osDiskCapacity;
    @ApiModelProperty("系统盘使用量,单位GB")
    private Float osDiskUsage;
    @ApiModelProperty("系统盘剩余量,单位GB")
    private Float osDiskRemaining;
    @ApiModelProperty("系统盘使用率,保留小数点后2位,单位 %")
    private Float osDiskUsageRate;
    @ApiModelProperty("运行时长,单位 小时")
    private Integer runningTime;
    @ApiModelProperty("网卡信息列表")
    private List<Nic> nics=new ArrayList<>();
    @ApiModelProperty("磁盘信息列表")
    private List<DiskInfo> diskInfos=new ArrayList<>();
    @ApiModelProperty("磁盘信息列表")
    private List<DiskMonitor> diskMonitors=new ArrayList<>();
    @ApiModelProperty("磁盘全部信息列表")
    private List<Disk> disks=new ArrayList<>();






}
