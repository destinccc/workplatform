package com.uuc.job.service.usercenter.model.org;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 */
@Data
public class RegionLimit implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "辖区名称")
    private String region_name;

    @ApiModelProperty(value = "辖区编码")
    private Number region_code;

    @ApiModelProperty(value = "辖区编码")
    private Number region_lev;


}
