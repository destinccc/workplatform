package com.uuc.system.api.model.cmdb;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.compress.utils.Lists;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @description:
 * @author: Destin
 * @create: 2022-04-19 14:41
 */
@Data
public class SelectModelDto {

    @ApiModelProperty(value = "中心模型标识")
    @NotBlank(message = "模型标识不能为空")
    private String centerModelKey;

    @ApiModelProperty(value = "默认过滤,查询全部设置不过滤")
    private Boolean filter=false;

//    @ApiModelProperty(value = "过滤的标识")
//    private List<String> entityCodes = Lists.newArrayList();
//
//    @ApiModelProperty(value = "查询的关系")
//    private List<ModelRelationDto> modelRelLists = Lists.newArrayList();

}

