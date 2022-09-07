package com.uuc.system.api.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: fxm
 * @date: 2022-07-01
 * @description:
 **/
@Data
public class UserAdminDeptVO implements Serializable {
    @ApiModelProperty("组织编码")
    private List<String> deptCode;

    @ApiModelProperty("用户编码")
    private String userCode;

}
