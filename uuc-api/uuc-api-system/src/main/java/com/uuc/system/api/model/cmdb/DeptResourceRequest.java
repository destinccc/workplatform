package com.uuc.system.api.model.cmdb;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DeptResourceRequest implements Serializable {

    @ApiModelProperty("组织编码")
    private List<String> deptCode;

    @ApiModelProperty("资源类型-模型标识")
    private List<String> resType;




}
