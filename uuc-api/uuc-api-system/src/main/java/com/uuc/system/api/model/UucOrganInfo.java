package com.uuc.system.api.model;

import com.uuc.common.core.annotation.Excel;
import com.uuc.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 机构信息对象 uuc_organ_info
 *
 * @author uuc
 * @date 2022-04-01
 */
public class UucOrganInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 组织机构ID */
    private Long id;

    /** 机构编码 */
    @Excel(name = "机构编码")
    private String organCode;

    /** 机构名称 */
    @Excel(name = "机构名称")
    private String organName;

    /** 机构简称 */
    @Excel(name = "机构简称")
    private String shortName;

    /** 注册地址 */
    @Excel(name = "注册地址")
    private String organRegistration;

    /** 纳税识别号 */
    @Excel(name = "纳税识别号")
    private String taxIdentiNumber;

    /** 企业法人 */
    @Excel(name = "企业法人")
    private String corporate;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String contactTel;

    /** 企业性质 */
    @Excel(name = "企业性质")
    private String enterpriseNature;

    /** 开户行编码 */
    @Excel(name = "开户行编码")
    private String bankCode;

    /** 开户行名称 */
    @Excel(name = "开户行名称")
    private String bankName;

    /** 开户行地址 */
    @Excel(name = "开户行地址")
    private String bankAddr;

    /** 企业邮箱 */
    @Excel(name = "企业邮箱")
    private String email;

    /** 机构状态(0正常1停用) */
    @Excel(name = "机构状态(0正常1停用)")
    private String status;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setOrganCode(String organCode)
    {
        this.organCode = organCode;
    }

    public String getOrganCode()
    {
        return organCode;
    }
    public void setOrganName(String organName)
    {
        this.organName = organName;
    }

    public String getOrganName()
    {
        return organName;
    }
    public void setShortName(String shortName)
    {
        this.shortName = shortName;
    }

    public String getShortName()
    {
        return shortName;
    }
    public void setOrganRegistration(String organRegistration)
    {
        this.organRegistration = organRegistration;
    }

    public String getOrganRegistration()
    {
        return organRegistration;
    }
    public void setTaxIdentiNumber(String taxIdentiNumber)
    {
        this.taxIdentiNumber = taxIdentiNumber;
    }

    public String getTaxIdentiNumber()
    {
        return taxIdentiNumber;
    }
    public void setCorporate(String corporate)
    {
        this.corporate = corporate;
    }

    public String getCorporate()
    {
        return corporate;
    }
    public void setContactTel(String contactTel)
    {
        this.contactTel = contactTel;
    }

    public String getContactTel()
    {
        return contactTel;
    }
    public void setEnterpriseNature(String enterpriseNature)
    {
        this.enterpriseNature = enterpriseNature;
    }

    public String getEnterpriseNature()
    {
        return enterpriseNature;
    }
    public void setBankCode(String bankCode)
    {
        this.bankCode = bankCode;
    }

    public String getBankCode()
    {
        return bankCode;
    }
    public void setBankName(String bankName)
    {
        this.bankName = bankName;
    }

    public String getBankName()
    {
        return bankName;
    }
    public void setBankAddr(String bankAddr)
    {
        this.bankAddr = bankAddr;
    }

    public String getBankAddr()
    {
        return bankAddr;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("organCode", getOrganCode())
            .append("organName", getOrganName())
            .append("shortName", getShortName())
            .append("organRegistration", getOrganRegistration())
            .append("taxIdentiNumber", getTaxIdentiNumber())
            .append("corporate", getCorporate())
            .append("contactTel", getContactTel())
            .append("enterpriseNature", getEnterpriseNature())
            .append("bankCode", getBankCode())
            .append("bankName", getBankName())
            .append("bankAddr", getBankAddr())
            .append("email", getEmail())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
