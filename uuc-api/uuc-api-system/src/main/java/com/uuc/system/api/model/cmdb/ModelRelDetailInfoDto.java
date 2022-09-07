package com.uuc.system.api.model.cmdb;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ModelRelDetailInfoDto implements Serializable {

    @ApiModelProperty(value = "源模型标识")
    private String sourceModelKey;
    @ApiModelProperty(value = "目标模型标识")
    private String targetModelKey;
    @ApiModelProperty(value = "关系标识")
    private String relationKey;

    List<ModelDetailInfoDto> target = Lists.newArrayList();
    List<ModelDetailInfoDto> source = Lists.newArrayList();
//    private Long entityId;
//    private String entityForm;
//    private String entityName;
}