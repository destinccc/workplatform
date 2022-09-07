package com.uuc.system.api.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.uuc.common.core.annotation.CMDBField;
import com.uuc.common.core.annotation.Excel;
import com.uuc.common.core.web.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;


/**
 * 组织信息对象 uuc_dept_info
 *
 * @author uuc
 * @date 2022-04-01
 */
@Data
public class UucDeptInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 组织ID */
    @JsonSerialize(using = ToStringSerializer.class)
    @CMDBField(name = "ModelCode",isModelCode =1 )
    private Long id;

//    /** 如果organ 为1 ，则机构编码执行这个虚拟组织对应的真实机构code */
//    @Excel(name = "如果organ 为1 ，则机构编码执行这个虚拟组织对应的真实机构code")
//    private String organFlag;

//    /** 组织编码 */
//    @Excel(name = "组织编码")
//    @JsonSerialize(using = ToStringSerializer.class)
//    private Long organCode;

    /** 组织编码 */
    @Excel(name = "组织编码")
    private String deptCode;

    /** 组织名称 */
    @Excel(name = "组织名称")
    @CMDBField(name = "ModelName")
    private String deptName;

    /** 父组织编码 */
    @Excel(name = "父组织编码")
    private String parentCode;

    /** 层级 */
    @Excel(name = "层级")
    @CMDBField(name = "OrgLevel")
    private Long level;

    /** 祖级列表 */
    @Excel(name = "祖级列表")
    private String ancestors;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Long orderNum;

    /** 负责人 */
    @Excel(name = "负责人")
    private String leaderUserCode;
    /** 负责人 */
    private String leaderName;

    /** 联系电话 */
    @Excel(name = "联系电话")
    @CMDBField(name = "OrgPhoneNumber")
    private String phone;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 组织状态(0正常1停用) */
    @Excel(name = "组织状态(0正常1停用)")
    private String status;

    /** 机构数据 */
    private UucOrganInfo organInfo;

    /** 钉钉组织编码 */
    private String dingDeptId;

    /** 组织类型(是否是同步组织) */
    private String deptType;

    /** 组织地址 */
    @CMDBField(name = "OrgAddress")
    private String address;

    public String getDingDeptId() {
        return dingDeptId;
    }

    public void setDingDeptId(String dingDeptId) {
        this.dingDeptId = dingDeptId;
    }

    public UucOrganInfo getOrganInfo() {
        return organInfo;
    }

    public void setOrganInfo(UucOrganInfo organInfo) {
        this.organInfo = organInfo;
    }

    /** 子部门 */
    private List<UucDeptInfo> children = new ArrayList<UucDeptInfo>();

    /** 组织领导人 */
    private List<UucUserDept> userDeptList = new ArrayList<UucUserDept>();


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
//            .append("organFlag", getOrganFlag())
//            .append("organCode", getOrganCode())
            .append("deptCode", getDeptCode())
            .append("deptName", getDeptName())
            .append("parentCode", getParentCode())
            .append("level", getLevel())
            .append("ancestors", getAncestors())
            .append("orderNum", getOrderNum())
            .append("leaderUserCode", getLeaderUserCode())
            .append("phone", getPhone())
            .append("email", getEmail())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
                .append("address", getAddress())
            .toString();
    }
}
