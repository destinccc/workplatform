package com.uuc.system.api.model.cmdb;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: fxm
 * @date: 2022-06-22
 * @description:
 **/
@Data
public class ProjectDto implements Serializable {

    @ApiModelProperty("项目id")
    private String id;

    @ApiModelProperty("项目名称")
    private String projectName;
}
