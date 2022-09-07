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
@ApiModel("部门")
public class DeptVo {
    @ApiModelProperty("部门ID")
    private String deptId;
    @ApiModelProperty("父类ID")
    private String parentId;
    @ApiModelProperty("部门编码")
    private String deptCode;
    @ApiModelProperty("部门层级")
    private int level;
    @ApiModelProperty("备注")
    private String remark;
}
