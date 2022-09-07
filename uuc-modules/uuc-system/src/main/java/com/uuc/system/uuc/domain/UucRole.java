package com.uuc.system.uuc.domain;

import com.uuc.common.core.annotation.Excel;
import com.uuc.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 角色信息对象 uuc_role
 * 
 * @author uuc
 * @date 2022-04-01
 */
public class UucRole extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 角色ID */
    private Long roleId;

    /** 角色名称 */
    @Excel(name = "角色名称")
    private String roleName;

    /** 角色权限字符串 */
    @Excel(name = "角色权限字符串")
    private String roleKey;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Long roleSort;

    /** 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限） */
    @Excel(name = "数据范围", readConverterExp = "1=：全部数据权限,2=：自定数据权限,3=：本部门数据权限,4=：本部门及以下数据权限")
    private String dataScope;

    /** 菜单树选择项是否关联显示 */
    @Excel(name = "菜单树选择项是否关联显示")
    private Integer menuCheckStrictly;

    /** 部门树选择项是否关联显示 */
    @Excel(name = "部门树选择项是否关联显示")
    private Integer deptCheckStrictly;

    /** 角色状态（0正常 1停用） */
    @Excel(name = "角色状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /**
     * 是否默认角色
     * @param roleId
     */
    private String isDefault;

    public void setRoleId(Long roleId) 
    {
        this.roleId = roleId;
    }

    public Long getRoleId() 
    {
        return roleId;
    }
    public void setRoleName(String roleName) 
    {
        this.roleName = roleName;
    }

    public String getRoleName() 
    {
        return roleName;
    }
    public void setRoleKey(String roleKey) 
    {
        this.roleKey = roleKey;
    }

    public String getRoleKey() 
    {
        return roleKey;
    }
    public void setRoleSort(Long roleSort) 
    {
        this.roleSort = roleSort;
    }

    public Long getRoleSort() 
    {
        return roleSort;
    }
    public void setDataScope(String dataScope) 
    {
        this.dataScope = dataScope;
    }

    public String getDataScope() 
    {
        return dataScope;
    }
    public void setMenuCheckStrictly(Integer menuCheckStrictly) 
    {
        this.menuCheckStrictly = menuCheckStrictly;
    }

    public Integer getMenuCheckStrictly() 
    {
        return menuCheckStrictly;
    }
    public void setDeptCheckStrictly(Integer deptCheckStrictly) 
    {
        this.deptCheckStrictly = deptCheckStrictly;
    }

    public Integer getDeptCheckStrictly() 
    {
        return deptCheckStrictly;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roleId", getRoleId())
            .append("roleName", getRoleName())
            .append("roleKey", getRoleKey())
            .append("roleSort", getRoleSort())
            .append("dataScope", getDataScope())
            .append("menuCheckStrictly", getMenuCheckStrictly())
            .append("deptCheckStrictly", getDeptCheckStrictly())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
