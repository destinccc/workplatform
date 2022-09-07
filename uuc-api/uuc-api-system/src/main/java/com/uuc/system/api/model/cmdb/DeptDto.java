package com.uuc.system.api.model.cmdb;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: fxm
 * @date: 2022-06-22
 * @description:
 **/
@Data
public class DeptDto implements Serializable {
    @ApiModelProperty("组织id")
    private String id;

    @ApiModelProperty("父组织id")
    private String parentId;

    @ApiModelProperty("组织名称")
    private String deptName;

    @ApiModelProperty("是否有组织所有权限")
    private Boolean isAdmin;

    @ApiModelProperty("子组织")
    private List<DeptDto> children = new ArrayList<>();
}
