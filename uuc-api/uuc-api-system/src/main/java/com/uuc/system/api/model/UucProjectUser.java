package com.uuc.system.api.model;

import com.uuc.common.core.annotation.Excel;
import com.uuc.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 用户项目对象 uuc_project_user
 * 
 * @author uuc
 * @date 2022-04-01
 */
public class UucProjectUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户编码 */
    @Excel(name = "用户编码")
    private String userCode;

    /** 项目编码 */
    @Excel(name = "项目编码")
    private String projectCode;

    /** 是否项目管理员(0否1是) */
    @Excel(name = "是否项目管理员(0否1是)")
    private String adminFlag;

    /** 是否项目管理员(0否1是) */
    @Excel(name = "是否项目管理员(0否1是)")
    private String maintenerFlag;

    private String userName;

    private String deptName;

    private String postName;

    private String phone;

    public String getMaintenerFlag() {
        return maintenerFlag;
    }

    public void setMaintenerFlag(String maintenerFlag) {
        this.maintenerFlag = maintenerFlag;
    }

    public void setUserCode(String userCode)
    {
        this.userCode = userCode;
    }

    public String getUserCode() 
    {
        return userCode;
    }
    public void setProjectCode(String projectCode) 
    {
        this.projectCode = projectCode;
    }

    public String getProjectCode() 
    {
        return projectCode;
    }
    public void setAdminFlag(String adminFlag) 
    {
        this.adminFlag = adminFlag;
    }

    public String getAdminFlag() 
    {
        return adminFlag;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userCode", getUserCode())
            .append("projectCode", getProjectCode())
            .append("adminFlag", getAdminFlag())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
