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
@ApiModel("云平台资源详情")
public class ResourceInfo {
    @ApiModelProperty("虚拟cpu总量,单位“核”注：单位“C”")
    private Integer vcpu;
    @ApiModelProperty("虚拟cpu使用量,单位“核”")
    private Integer vcpuUsed;
    @ApiModelProperty("虚拟cpu使用率,cpu使用量/cpu总量,单位“%”,保留两位小数")
    private Double vcpuUsedRate;
    @ApiModelProperty("内存总量,单位“GB”注：单位“G”")
    private Double memory;
    @ApiModelProperty("内存使用量,单位“GB”")
    private Double memoryUsed;
    @ApiModelProperty("内存使用率,内存使用量/内存总量,单位“%”,保留两位小数")
    private Double memoryUsedRate;
    @ApiModelProperty("存储总量,单位 TB,保留两位小数")
    private Double storage;
    @ApiModelProperty("存储使用量,单位 TB,保留两位小数")
    private Double storageUsed;
    @ApiModelProperty("存储使用率,存储使用量/内存总量,单位“%”,保留两位小数")
    private Double storageUsedRate;
    @ApiModelProperty("备注")
    private String remark;
}
