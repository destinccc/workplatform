package com.uuc.system.api.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: fxm
 * @date: 2022-06-23
 * @description:
 **/
@Data
public class UucUserVO implements Serializable {

    @ApiModelProperty("当前页")
    private int pageNum;
    @ApiModelProperty("每页大小")
    private int pageSize;
    @ApiModelProperty("组织编码")
    private int deptCode;
}
