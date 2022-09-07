package com.uuc.system.uuc.domain;

import com.uuc.common.core.annotation.Excel;
import com.uuc.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 用户角色关联对象 ucc_user_role
 * 
 * @author uuc
 * @date 2022-04-01
 */
public class UccUserRole extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户代码 */
    @Excel(name = "用户代码")
    private String userCode;

    /** 角色ID */
    @Excel(name = "角色ID")
    private Long roleId;

    public void setUserCode(String userCode) 
    {
        this.userCode = userCode;
    }

    public String getUserCode() 
    {
        return userCode;
    }
    public void setRoleId(Long roleId) 
    {
        this.roleId = roleId;
    }

    public Long getRoleId() 
    {
        return roleId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userCode", getUserCode())
            .append("roleId", getRoleId())
            .toString();
    }
}
