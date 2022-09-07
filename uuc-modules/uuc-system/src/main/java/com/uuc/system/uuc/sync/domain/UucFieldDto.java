package com.uuc.system.uuc.sync.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class UucFieldDto implements Serializable {

    @ApiModelProperty("cmdb对应元模型属性英文标识")
    private String vModel;

    @ApiModelProperty("属性变化后的值")
    private Object value;
}
