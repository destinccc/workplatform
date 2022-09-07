package com.uuc.system.api.model.cmdb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author deng
 * @date 2022/7/15 0015
 * @description
 */
@Data
@ApiModel("数据盘对象")
public class DataDisk {
    @ApiModelProperty("数据盘类型")
    private String diskType;//SSD云盘，LOCAL_SSD 本地盘，SATA 高效云盘
    @ApiModelProperty("数据盘大小")
    private int diskCapacity;//单位GB
}
