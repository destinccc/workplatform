package com.uuc.system.api.model.cmdb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author deng
 * @date 2022/7/18 0018
 * @description
 */
@Data
@ApiModel("部门简要信息")
public class DeptPo {

    @ApiModelProperty("部门id")
    private String deptId;
    @ApiModelProperty("部门code")
    private String deptCode;
    @ApiModelProperty("部门名称")
    private String deptName;
}
