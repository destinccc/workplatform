package com.uuc.system.api.model;

import com.uuc.common.core.annotation.Excel;
import com.uuc.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 【请填写功能名称】对象 uuc_project_dept
 * 
 * @author uuc
 * @date 2022-05-23
 */
public class UucProjectDept extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 组织编码 */
    @Excel(name = "组织编码")
    private String deptCode;

    /** 项目编码 */
    @Excel(name = "项目编码")
    private String projectCode;

    public void setDeptCode(String deptCode) 
    {
        this.deptCode = deptCode;
    }

    public String getDeptCode() 
    {
        return deptCode;
    }
    public void setProjectCode(String projectCode) 
    {
        this.projectCode = projectCode;
    }

    public String getProjectCode() 
    {
        return projectCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("deptCode", getDeptCode())
            .append("projectCode", getProjectCode())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
