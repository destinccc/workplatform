package com.uuc.system.api.model;

import com.uuc.common.core.annotation.Excel;
import com.uuc.common.core.web.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 用户组织对象 uuc_user_dept
 *
 * @author uuc
 * @date 2022-04-01
 */
@Data
public class UucUserDept extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户编码 */
    @Excel(name = "用户编码")
    private String userCode;

    /** 用户姓名 */
    @Excel(name = "用户姓名")
    private String userName;

    /** 组织编码 */
    @Excel(name = "组织编码")
    private String deptCode;

    /** 是否组织负责人(0否1是) */
    @Excel(name = "是否组织负责人(0否1是)")
    private String adminFlag;

    public void setUserCode(String userCode)
    {
        this.userCode = userCode;
    }

    public String getUserCode()
    {
        return userCode;
    }
    public void setDeptCode(String deptCode)
    {
        this.deptCode = deptCode;
    }

    public String getDeptCode()
    {
        return deptCode;
    }
    public void setAdminFlag(String adminFlag)
    {
        this.adminFlag = adminFlag;
    }

    public String getAdminFlag()
    {
        return adminFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userCode", getUserCode())
            .append("deptCode", getDeptCode())
            .append("adminFlag", getAdminFlag())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
