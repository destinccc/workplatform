package com.uuc.system.api.model.cmdb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author deng
 * @date 2022/7/15 0015
 * @description 接口请求体
 */
@Data
@ApiModel("请求参数实体")
public class RequestBodyVo {
    @ApiModelProperty("部门标识")
    private String deptCode;
    @ApiModelProperty("项目标识")
    private String projectCode;
    @ApiModelProperty("服务器唯一标识")
    private String serverId;
    @ApiModelProperty("开始时间")
    private Long startTime;
    @ApiModelProperty("结束时间")
    private Long endTime;
}
