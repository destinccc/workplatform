package com.uuc.system.uuc.domain;

import com.uuc.common.core.annotation.Excel;
import com.uuc.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 应用信息对象 uuc_application
 * 
 * @author uuc
 * @date 2022-04-01
 */
public class UucApplication extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键Id */
    private Long id;

    /** 应用代码 */
    @Excel(name = "应用代码")
    private String applicationCode;

    /** 应用名称 */
    @Excel(name = "应用名称")
    private String applicationName;

    /** 应用类型 */
    @Excel(name = "应用类型")
    private String applicationType;

    /** 应用密钥 */
    @Excel(name = "应用密钥")
    private String applicationSecret;

    /** 应用描述 */
    @Excel(name = "应用描述")
    private String applicationDesc;

    /** 应用logo */
    @Excel(name = "应用logo")
    private String applicationLogo;

    /** 应用标签 */
    @Excel(name = "应用标签")
    private String applicationLabel;

    /** 应用排序 */
    @Excel(name = "应用排序")
    private Long applicationOrderNum;

    /** 父级应用代码 */
    @Excel(name = "父级应用代码")
    private String parentApplicationCode;

    /** 级别 */
    @Excel(name = "级别")
    private Long level;

    /** 应用访问路径 */
    @Excel(name = "应用访问路径")
    private String applicationUrl;

    /** 删除标识(0正常1删除) */
    private String delFlag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setApplicationCode(String applicationCode) 
    {
        this.applicationCode = applicationCode;
    }

    public String getApplicationCode() 
    {
        return applicationCode;
    }
    public void setApplicationName(String applicationName) 
    {
        this.applicationName = applicationName;
    }

    public String getApplicationName() 
    {
        return applicationName;
    }
    public void setApplicationType(String applicationType) 
    {
        this.applicationType = applicationType;
    }

    public String getApplicationType() 
    {
        return applicationType;
    }
    public void setApplicationSecret(String applicationSecret) 
    {
        this.applicationSecret = applicationSecret;
    }

    public String getApplicationSecret() 
    {
        return applicationSecret;
    }
    public void setApplicationDesc(String applicationDesc) 
    {
        this.applicationDesc = applicationDesc;
    }

    public String getApplicationDesc() 
    {
        return applicationDesc;
    }
    public void setApplicationLogo(String applicationLogo) 
    {
        this.applicationLogo = applicationLogo;
    }

    public String getApplicationLogo() 
    {
        return applicationLogo;
    }
    public void setApplicationLabel(String applicationLabel) 
    {
        this.applicationLabel = applicationLabel;
    }

    public String getApplicationLabel() 
    {
        return applicationLabel;
    }
    public void setApplicationOrderNum(Long applicationOrderNum) 
    {
        this.applicationOrderNum = applicationOrderNum;
    }

    public Long getApplicationOrderNum() 
    {
        return applicationOrderNum;
    }
    public void setParentApplicationCode(String parentApplicationCode) 
    {
        this.parentApplicationCode = parentApplicationCode;
    }

    public String getParentApplicationCode() 
    {
        return parentApplicationCode;
    }
    public void setLevel(Long level) 
    {
        this.level = level;
    }

    public Long getLevel() 
    {
        return level;
    }
    public void setApplicationUrl(String applicationUrl) 
    {
        this.applicationUrl = applicationUrl;
    }

    public String getApplicationUrl() 
    {
        return applicationUrl;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("applicationCode", getApplicationCode())
            .append("applicationName", getApplicationName())
            .append("applicationType", getApplicationType())
            .append("applicationSecret", getApplicationSecret())
            .append("applicationDesc", getApplicationDesc())
            .append("applicationLogo", getApplicationLogo())
            .append("applicationLabel", getApplicationLabel())
            .append("applicationOrderNum", getApplicationOrderNum())
            .append("parentApplicationCode", getParentApplicationCode())
            .append("level", getLevel())
            .append("applicationUrl", getApplicationUrl())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
