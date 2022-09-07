package com.uuc.system.api.model.cmdb;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ResourceInfoDto implements Serializable {

    @ApiModelProperty("资源id")
    private String resourceId;
    @ApiModelProperty("资源类型")
    private String resType;

}