package com.uuc.resource.apis.inner.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author deng
 * @date 2022/7/18 0018
 * @description 组织树结构
 */
@ApiModel("组织树结构")
@Data
public class DeptTreeVo {
    @ApiModelProperty("组织Id")
    private String deptId;
    @ApiModelProperty("父级组织Id")
    private String parentId;
    @ApiModelProperty("组织编码")
    private String deptCode;
    @ApiModelProperty("组织名称")
    private String deptName;
    @ApiModelProperty("组织级别")
    private long level;
    @ApiModelProperty("备注")
    private String remark;
    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<DeptTreeVo> children;
}
