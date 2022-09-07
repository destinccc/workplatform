package com.uuc.system.uuc.domain;

import com.uuc.common.core.annotation.Excel;
import com.uuc.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 用户应用关联对象 ucc_user_applicaiton
 * 
 * @author uuc
 * @date 2022-04-01
 */
public class UccUserApplicaiton extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户代码 */
    @Excel(name = "用户代码")
    private String userCode;

    /** 应用代码 */
    @Excel(name = "应用代码")
    private String applicationCode;

    public void setUserCode(String userCode) 
    {
        this.userCode = userCode;
    }

    public String getUserCode() 
    {
        return userCode;
    }
    public void setApplicationCode(String applicationCode) 
    {
        this.applicationCode = applicationCode;
    }

    public String getApplicationCode() 
    {
        return applicationCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userCode", getUserCode())
            .append("applicationCode", getApplicationCode())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
