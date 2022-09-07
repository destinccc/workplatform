package com.uuc.alarm.domain;

import com.uuc.common.core.annotation.Excel;
import com.uuc.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 【请填写功能名称】对象 user_nosys
 *
 * @author uuc
 * @date 2022-08-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserNosys extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Integer id;

    /** use in cookie */
    @Excel(name = "use in cookie")
    private String uuid;

    /** login name, cannot rename */
    @Excel(name = "login name, cannot rename")
    private String username;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String password;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String passwords;

    /** display name, chinese name */
    @Excel(name = "display name, chinese name")
    private String dispname;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String phone;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String email;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String im;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String portrait;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String intro;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String organization;

    /** 0: long-term account; 1: temporary account */
    @Excel(name = "0: long-term account; 1: temporary account")
    private Integer typ;

    /** 0: active, 1: inactive, 2: locked, 3: frozen, 5: writen-off */
    @Excel(name = "0: active, 1: inactive, 2: locked, 3: frozen, 5: writen-off")
    private Integer status;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Integer isRoot;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Integer leaderId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String leaderName;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Integer loginErrNum;

    /** $column.columnComment */
    private Long activeBegin;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long activeEnd;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long lockedAt;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long updatedAt;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long pwdUpdatedAt;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long loggedAt;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date createAt;

    /** 是否为系统用户：1为非系统用户 */
    @Excel(name = "是否为系统用户：1为非系统用户")
    private Integer userType;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long creatorId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String creator;

    /** 告警组id */
    @Excel(name = "告警组id")
    private String organizationId;
}
