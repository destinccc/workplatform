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
@ApiModel("物理机详情")
public class PhysicalMachineInfo {

    @ApiModelProperty("服务器的 id")
    private String id;
    @ApiModelProperty("服务器名称")
    private String name;
    @ApiModelProperty("系统主机名称")
    private String hostname;
    @ApiModelProperty("服务器的描述")
    private String description;
    @ApiModelProperty("服务器的操作系统名称")
    private String osName;
    @ApiModelProperty("服务器的管理 ip")
    private String ipAddress;
    @ApiModelProperty("设备型号")
    private String model;
    @ApiModelProperty("设备生产厂商")
    private String vendor;
    @ApiModelProperty("设备类型")
    private String deviceType;
    @ApiModelProperty("cpu 型号")
    private String cpuModel;
    @ApiModelProperty("cpu 插槽数,单位 个")
    private Integer cpuSocket;
    @ApiModelProperty("cpu 物理核数单位 个")
    private Integer cpuCore;
    @ApiModelProperty("cpu 逻辑核数（线程数）单位 个")
    private Integer cpuCount;
    @ApiModelProperty("cpu 频率,单位 GHz")
    private Integer cpuFrequence;
    @ApiModelProperty("内存大小,单位 GB")
    private Integer memorySize;
    @ApiModelProperty("存储容量,单位 GB")
    private Integer capacity;
    @ApiModelProperty("网卡信息列表")
    private List<Nic> nics;
    @ApiModelProperty("磁盘信息列表")
    private List<Disk> disks;
    @ApiModelProperty("IPMI地址")
    private String ipForIpmi;
    @ApiModelProperty("IPMI用户名")
    private String ipmiUserName;
    @ApiModelProperty("IPMI密码")
    private String ipmiPassword;
    @ApiModelProperty("单位Mbps,保留两位小数")
    private Double nicSpeed;
    @ApiModelProperty("电源型号")
    private String powerType;
    @ApiModelProperty("电源数量")
    private Integer powerCount;
    @ApiModelProperty("功耗(W)")
    private  Integer powerWatts;
    @ApiModelProperty("出厂日期")
    private String purchaseDate;
    @ApiModelProperty("质保到期日期")
    private String warrantyDate;
    @ApiModelProperty("服务器状态,OPERATION运行,IDLE闲置,REJECTED报废")
    private String status;
    @ApiModelProperty("U位信息")
    private String unumber;
    @ApiModelProperty("所属机柜信息")
    private String rack;
    @ApiModelProperty("备注信息")
    private String remark;
    @ApiModelProperty("是否有exporter")
    private Boolean hasExpoter=false;
}
