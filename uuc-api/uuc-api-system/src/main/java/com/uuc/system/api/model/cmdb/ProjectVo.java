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
@ApiModel("项目")
public class ProjectVo {
    @ApiModelProperty("项目ID")
    private String projectId;
    @ApiModelProperty("项目编码")
    private String projectCode;
    @ApiModelProperty("项目名称")
    private String projectName;
    @ApiModelProperty("项目简称")
    private String shortName;
    @ApiModelProperty("项目版本号")
    private String version;
    @ApiModelProperty("项目负责人")
    private String owner;
    @ApiModelProperty("立项日期")
    private String startDate;
    @ApiModelProperty("结项日期")
    private String endDate;
    @ApiModelProperty("项目描述")
    private String description;
    @ApiModelProperty("备注")
    private String remark;
}
