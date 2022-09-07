package com.uuc.system.api.model.cmdb;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: fxm
 * @date: 2022-06-22
 * @description:
 **/
@Data
public class UserDeptRequest implements Serializable {

    @ApiModelProperty("用户id")
    private String userCode;

    @ApiModelProperty("是否是管理员")
    private Boolean adminFlag = false;
}
