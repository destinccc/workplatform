package com.uuc.system.api.model.cmdb;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserProjectRequest implements Serializable {

    @ApiModelProperty("人员编码")
    private String userCode;

    @ApiModelProperty("是否是管理员")
    private Boolean adminFlag = false;

}