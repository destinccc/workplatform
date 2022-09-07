package com.uuc.system.api.model.cmdb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author deng
 * @date 2022/7/15 0015
 * @description 虚机监控统计
 */
@Data
@ApiModel("监控统计对象")
public class ServerMonitor {

    @ApiModelProperty("对象id")
    private String serverId;
    @ApiModelProperty("运行时长,单位 小时")
    private Integer runningTime;
    @ApiModelProperty("指定时间区间内cpu平均使用率,保留小数点后2位,单位 %")
    private Float avgCpuUsageRate;
    @ApiModelProperty("指定时间区间内cpu最小使用率,保留小数点后2位,单位 %")
    private Float minCpuUsageRate;
    @ApiModelProperty("指定时间区间内cpu最大使用率,保留小数点后2位,单位 %")
    private Float maxCpuUsageRate;
    @ApiModelProperty("指定时间区间内内存平均使用率,保留小数点后2位,单位 %")
    private Float avgMemUsageRate;
    @ApiModelProperty("指定时间区间内内存最小使用率,保留小数点后2位,单位 %")
    private Float minMemUsageRate;
    @ApiModelProperty("指定时间区间内内存最大使用率,保留小数点后2位,单位 %")
    private Float maxMemUsageRate;
    @ApiModelProperty("指定时间区间内网卡的平均入口（下行）速率,保留小数点后2位,单位 kbPS")
    private Float avgInboundRate;
    @ApiModelProperty("指定时间区间内网卡的最小入口（下行）速率")
    private Float minInboundRate;
    @ApiModelProperty("指定时间区间内网卡的最大入口（下行）速率")
    private Float maxInboundRate;
    @ApiModelProperty("指定时间区间内网卡的平均出口（上行）速率")
    private Float avgOutboundRate;
    @ApiModelProperty("指定时间区间内网卡的最小出口（上行）速率")
    private Float minOutboundRate;
    @ApiModelProperty("指定时间区间内网卡的最大出口（上行）速率")
    private Float maxOutboundRate;
    @ApiModelProperty("指定时间区间内磁盘平均读 iops")
    private Integer avgDiskReadIops;
    @ApiModelProperty("指定时间区间内磁盘最小读 iops")
    private Integer minDiskReadIops;
    @ApiModelProperty("指定时间区间内磁盘最大读 iops")
    private Integer maxDiskReadIops;
    @ApiModelProperty("指定时间区间内磁盘平均写 iops")
    private Integer avgDiskWriteIops;
    @ApiModelProperty("指定时间区间内磁盘最小写 iops")
    private Integer minDiskWriteIops;
    @ApiModelProperty("指定时间区间内磁盘最大写 iops")
    private Integer maxDiskWriteIops;
    @ApiModelProperty("指定时间区间内磁盘平均读吞吐量")
    private Float avgDiskReadThroughput;
    @ApiModelProperty("指定时间区间内磁盘最小读吞吐量")
    private Float minDiskReadThroughput;
    @ApiModelProperty("指定时间区间内磁盘最大读吞吐量")
    private Float maxDiskReadThroughput;
    @ApiModelProperty("指定时间区间内磁盘平均写吞吐量")
    private Float avgDiskWriteThroughput;
    @ApiModelProperty("指定时间区间内磁盘最小写吞吐量")
    private Float minDiskWriteThroughput;
    @ApiModelProperty("指定时间区间内磁盘最大写吞吐量")
    private Float maxDiskWriteThroughput;
    @ApiModelProperty("指定时间区间内磁盘平均读字节数")
    private Float avgDiskReadNum;
    @ApiModelProperty("指定时间区间内磁盘最小读字节数")
    private Float minDiskReadNum;
    @ApiModelProperty("指定时间区间内磁盘最大读字节数")
    private Float maxDiskReadNum;
    @ApiModelProperty("指定时间区间内磁盘平均写字节数")
    private Float avgDiskWriteNum;
    @ApiModelProperty("指定时间区间内磁盘最小写字节数")
    private Float minDiskWriteNum;
    @ApiModelProperty("指定时间区间内磁盘最大写字节数")
    private Float maxDiskWriteNum;



}
