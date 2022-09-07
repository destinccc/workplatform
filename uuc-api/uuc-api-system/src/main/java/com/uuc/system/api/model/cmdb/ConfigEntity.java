package com.uuc.system.api.model.cmdb;


import com.uuc.common.core.annotation.Excel;
import com.uuc.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 配置实体对象 config_entity
 *
 * @author ruoyi
 * @date 2021-09-23
 */
@ApiModel("配置实体")
@Data
@ToString
public class ConfigEntity extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 实体ID */
    private Long entityId;

    /** 模型ID */
//    @Excel(name = "模型ID")
    @ApiModelProperty(value = "模型ID")
    private Long modelId;

    /** 实体名称 */
//    @Excel(name = "实体名称")
    @ApiModelProperty(value = "实体名称")
    private String entityName;

    /** 实体表单数据 */
    @Excel(name = "实体表单数据")
    @ApiModelProperty(value = "属性表单")
    private String entityForm;

    /** 删除标志（0代表存在 1代表删除） */
    @ApiModelProperty(value = "删除标志（0代表存在 1代表删除）")
    private String delFlag;

    /** 创建方式（1手工创建 2后台导入） */
    @ApiModelProperty(value = "创建方式（1手工创建 2后台导入 3 系统集成接入）")
    private String createType;

    @ApiModelProperty(value = "系统集成适配数据标识")
    private String integrationDataKey = " ";

    @ApiModelProperty(value = "数据来源账号")
    private String sourceKey = " ";


}
