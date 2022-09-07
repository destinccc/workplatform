package com.uuc.system.api.model.cmdb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author deng
 * @date 2022/7/19 0019
 * @description
 */
@ApiModel("资源监控请求参数")
@Data
public class ServerMonitorVo {

    @ApiModelProperty("资源Id")
    private String resourceId;
    @ApiModelProperty("起始时间")
    private Long startTime;
    @ApiModelProperty("结束时间")
    private Long endTime;
    @ApiModelProperty("资源列表")
    private String [] resourceIds;
}
