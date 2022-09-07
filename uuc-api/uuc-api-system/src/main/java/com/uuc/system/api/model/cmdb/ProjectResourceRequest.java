package com.uuc.system.api.model.cmdb;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProjectResourceRequest implements Serializable {

    @ApiModelProperty("用户id")
    private String userCode;

    @ApiModelProperty("项目编码")
    private List<String> projectCode;

    @ApiModelProperty("资源类型-模型标识")
    private List<String> resType;

    @ApiModelProperty("是否管理员")
    private Boolean adminFlag = false;


}