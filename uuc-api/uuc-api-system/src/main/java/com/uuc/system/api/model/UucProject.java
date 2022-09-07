package com.uuc.system.api.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.uuc.common.core.annotation.CMDBField;
import com.uuc.common.core.annotation.Excel;
import com.uuc.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;



/**
 * 项目信息对象 uuc_project
 *
 * @author uuc
 * @date 2022-04-01
 */
public class UucProject extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 项目ID */
    @JsonSerialize(using = ToStringSerializer.class)
    @CMDBField(name = "ModelCode",isModelCode = 1)
    private Long id;

    /** 项目编码 */
    @Excel(name = "项目编码")
    private String projectCode;

    /** 项目全称 */
    @Excel(name = "项目全称")
    @CMDBField(name = "ModelName")
    private String projectName;

    /** 项目简称 */
    @Excel(name = "项目简称")
    @CMDBField(name = "ShortName")
    private String shortName;

//    /** 项目状态 */
//    @Excel(name = "项目状态")
//    private String projectStatus;

    /** 立项日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "立项日期", width = 30, dateFormat = "yyyy-MM-dd")
    @CMDBField(name = "StartDate",isDateFormat = 1)
    private Date projectStartDate;

    /** 结项日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结项日期", width = 30, dateFormat = "yyyy-MM-dd")
    @CMDBField(name = "EndDate",isDateFormat = 1)
    private Date projectEndDate;

    /** 项目描述 */
    @Excel(name = "项目描述")
    @CMDBField(name = "Description")
    private String projectDesc;

    /** 项目归属管理组织 */
    @Excel(name = "项目归属管理组织")
    private String ownDeptCode;

    /** 流程编号 */
    @Excel(name = "流程编号")
    @CMDBField(name = "ApprovalNo")
    private String projectArchiveCode;

//    /** 父项目编码 */
//    @Excel(name = "父项目编码")
//    private String parentCode;
//
//    /** 层级 */
//    @Excel(name = "层级")
//    private Integer level;
//
//    /** 祖级列表 */
//    @Excel(name = "祖级列表")
//    private String ancestors;
//
//    /** 显示顺序 */
//    @Excel(name = "显示顺序")
//    private Integer orderNum;
//
//    /** 项目经理 */
//    @Excel(name = "项目经理")
//    private String pmUserCode;

    /** 项目目标 */
    @Excel(name = "项目目标")
    @CMDBField(name = "Target")
    private String projectGoal;

    /** 项目预算 */
    @Excel(name = "项目预算")
    @CMDBField(name = "Budget")
    private Long projectBudget;

    /** 项目归属管理组织名称 */
    private String deptName;

    /** 项目类型（
     商机 commercial opportunityor
     合同contract
     内部 internal project） */
    private String projectType;

    /** 项目来源 **/
    @CMDBField(name = "Source")
    private String projectSource;

    /** 项目分级 **/
    @CMDBField(name = "Level")
    private String projectLevel;

    /** 立项申请人（名字） **/
    @CMDBField(name = "Applicant")
    private String applicant;

    /** 项目版本 **/
    @CMDBField(name = "Version")
    private String version;

    /** 备注 **/
    @CMDBField(name = "Remark")
    private String remark;

    /** 项目归属使用组织 **/
    private List<UucProjectDept> useDeptList;

    /** 项目归属使用组织名称 **/
    private String useDeptNames;

    public String getUseDeptNames() {
        return useDeptNames;
    }

    public void setUseDeptNames(String useDeptNames) {
        this.useDeptNames = useDeptNames;
    }

    public List<UucProjectDept> getUseDeptList() {
        return useDeptList;
    }

    public void setUseDeptList(List<UucProjectDept> useDeptList) {
        this.useDeptList = useDeptList;
    }

    public String getProjectSource() {
        return projectSource;
    }

    public void setProjectSource(String projectSource) {
        this.projectSource = projectSource;
    }

    public String getProjectLevel() {
        return projectLevel;
    }

    public void setProjectLevel(String projectLevel) {
        this.projectLevel = projectLevel;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /** 项目关联用户列表 **/
    private List<UucProjectUser> userList;

    public List<UucProjectUser> getUserList() {
        return userList;
    }

    public void setUserList(List<UucProjectUser> userList) {
        this.userList = userList;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setProjectCode(String projectCode)
    {
        this.projectCode = projectCode;
    }

    public String getProjectCode()
    {
        return projectCode;
    }
    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
    }

    public String getProjectName()
    {
        return projectName;
    }
    public void setShortName(String shortName)
    {
        this.shortName = shortName;
    }

    public String getShortName()
    {
        return shortName;
    }

    public void setProjectStartDate(Date projectStartDate)
    {
        this.projectStartDate = projectStartDate;
    }

    public Date getProjectStartDate()
    {
        return projectStartDate;
    }
    public void setProjectEndDate(Date projectEndDate)
    {
        this.projectEndDate = projectEndDate;
    }

    public Date getProjectEndDate()
    {
        return projectEndDate;
    }
    public void setProjectDesc(String projectDesc)
    {
        this.projectDesc = projectDesc;
    }

    public String getProjectDesc()
    {
        return projectDesc;
    }
    public void setOwnDeptCode(String ownDeptCode)
    {
        this.ownDeptCode = ownDeptCode;
    }

    public String getOwnDeptCode()
    {
        return ownDeptCode;
    }
    public void setProjectArchiveCode(String projectArchiveCode)
    {
        this.projectArchiveCode = projectArchiveCode;
    }

    public String getProjectArchiveCode()
    {
        return projectArchiveCode;
    }
    public void setProjectGoal(String projectGoal)
    {
        this.projectGoal = projectGoal;
    }

    public String getProjectGoal()
    {
        return projectGoal;
    }
    public void setProjectBudget(Long projectBudget)
    {
        this.projectBudget = projectBudget;
    }

    public Long getProjectBudget()
    {
        return projectBudget;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("projectCode", getProjectCode())
                .append("projectName", getProjectName())
                .append("shortName", getShortName())
//                .append("projectStatus", getProjectStatus())
                .append("projectStartDate", getProjectStartDate())
                .append("projectEndDate", getProjectEndDate())
                .append("projectDesc", getProjectDesc())
                .append("ownDeptCode", getOwnDeptCode())
                .append("projectArchiveCode", getProjectArchiveCode())
//                .append("parentCode", getParentCode())
//                .append("level", getLevel())
//                .append("ancestors", getAncestors())
//                .append("orderNum", getOrderNum())
//                .append("pmUserCode", getPmUserCode())
                .append("projectGoal", getProjectGoal())
                .append("projectBudget", getProjectBudget())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("updateTime", getUpdateTime())
                .append("updateBy", getUpdateBy())
                .toString();
    }
}
