package com.uuc.system.uuc.sync.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.compress.utils.Lists;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class UucResourceDto implements Serializable {

    @ApiModelProperty("变更类型  新增insert 修改update 删除delete")
    private String changeType;
    @ApiModelProperty("模型标识")
    private String modelKey;
    @ApiModelProperty("实例唯一标识 修改")
    private String modelCode;
    @ApiModelProperty("属性键值 新增/修改")
    private List<UucFieldDto> filedChangeList = Lists.newArrayList();
    @ApiModelProperty("关系列表 新增/修改")
    private List<UucRelDto> relChangeList = Lists.newArrayList();
    @ApiModelProperty("实例唯一标识集合 删除")
    private List<String> deleteModelCodes;

}
