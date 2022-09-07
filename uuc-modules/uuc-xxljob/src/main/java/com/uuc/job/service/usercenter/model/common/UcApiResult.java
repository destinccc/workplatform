package com.uuc.job.service.usercenter.model.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import java.io.Serializable;

/**
 * @description:
 */
@Data
public class UcApiResult<T> implements Serializable {

    private Integer code;

    private String message;

    private T data;

    @ApiModelProperty(value = "错误信息")
    private UcError error;

    @ApiModelProperty(value = "Meta")
    private UcMeta meta;
}
