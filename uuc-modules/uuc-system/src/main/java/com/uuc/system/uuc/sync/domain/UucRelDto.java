package com.uuc.system.uuc.sync.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class UucRelDto {

    @ApiModelProperty("源模型标识")
    private String sourceModelKey;

    @ApiModelProperty("变化的关系标识")
    private String relationKey;

    @ApiModelProperty("目标模型标识")
    private String targetModelKey;

    @ApiModelProperty("变化后的模型标识集合")
    private List<String> sourceModelCodes;

    @ApiModelProperty("变化后的模型标识集合")
    private List<String> targetModelCodes;

}
