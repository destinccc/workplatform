package com.uuc.system.api.model.cmdb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author deng
 * @date 2022/7/18 0018
 * @description
 */
@ApiModel("监控资源请求实体")
@Data
public class MonitorResourceVo {

    @ApiModelProperty("资源Id列表")
    private String [] resource_ids;
    @ApiModelProperty("cpu总量")
    private Integer vcpu;
    @ApiModelProperty(" 内存总量")
    private Double memory;
    @ApiModelProperty(" 存储总量")
    private Double storage;
    @ApiModelProperty("资源Id")
    private String resourceId;
    @ApiModelProperty("MAC地址数组")
    private List<String> macAddresses;
    @ApiModelProperty("挂载点")
    private List<String> mountPoints;
    @ApiModelProperty("设备列表")
    private List<String> devices;



}
