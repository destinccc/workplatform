package com.uuc.system.uuc.domain;

import com.uuc.common.core.annotation.Excel;
import com.uuc.common.core.web.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * API版本控制
 */
@Data
public class UucApiVersion extends BaseEntity {

    private static final long serialVersionUID = 1L;
    /** 自增id */
    private Long id;

    /** 接口版本号 */
    @Excel(name = "接口版本号")
    private String apiVersion;

    /** 是否激活 0 否 1 是 */
    @Excel(name = "是否激活 0 否 1 是")
    private String activate;

    /** 操作人 */
    @Excel(name = "操作人")
    private String operator;

}
