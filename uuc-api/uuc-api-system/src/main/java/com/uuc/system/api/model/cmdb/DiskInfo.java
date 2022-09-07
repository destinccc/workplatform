package com.uuc.system.api.model.cmdb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author deng
 * @date 2022/7/15 0015
 * @description 磁盘对象
 */
@Data
@ApiModel("磁盘对象")
public class DiskInfo {
    @ApiModelProperty("磁盘名称")
    private String name;
    @ApiModelProperty("磁盘ID")
    private String id;
    @ApiModelProperty("磁盘容量,单位GB")
    private int capacity;
    @ApiModelProperty("磁盘分类")
    private String category;
    @ApiModelProperty("磁盘类型")
    private String type;
    @ApiModelProperty("磁盘挂载点")
    private String mountPoint;

}
