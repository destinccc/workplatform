package com.uuc.alarm.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uuc.common.core.annotation.Excel;
import com.uuc.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 【请填写功能名称】对象 team
 *
 * @author uuc
 * @date 2022-08-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Team extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Integer id;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String ident;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String name;

    /** 备注 */
    @Excel(name = "备注")
    private String note;

    /** 0: member manage; 1: admin manage */
    @Excel(name = "0: member manage; 1: admin manage")
    private Integer mgmt;

    /** 创建人 */
    @Excel(name = "创建人")
    private Integer creator;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastUpdated;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;
}
