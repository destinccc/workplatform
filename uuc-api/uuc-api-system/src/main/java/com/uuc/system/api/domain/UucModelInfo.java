package com.uuc.system.api.domain;

import com.uuc.common.core.annotation.Excel;
import com.uuc.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 模块管理对象 uuc_model_info
 * 
 * @author uuc
 * @date 2022-04-12
 */
public class UucModelInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 模块编码 */
    @Excel(name = "模块编码")
    private String clientId;

    /** 模块名 */
    @Excel(name = "模块名")
    private String clientName;

    /** 私钥 */
    @Excel(name = "私钥")
    private String clientRsaPrivate;

    /** 公钥 */
    @Excel(name = "公钥")
    private String clientRsaPublic;

    /** 模块bash路径，如http://ip:port */
    @Excel(name = "模块bash路径，如http://ip:port")
    private String baseUrl;

    /** 客户端密钥 */
    private String clientSecret;

    /**
     * 绑定的顶级组织
     * @param id
     */
    private String deptCode;

    /**
     * 绑定部门的名称
     * @param id
     */
    private String deptName;
    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setClientId(String clientId) 
    {
        this.clientId = clientId;
    }

    public String getClientId() 
    {
        return clientId;
    }
    public void setClientName(String clientName) 
    {
        this.clientName = clientName;
    }

    public String getClientName() 
    {
        return clientName;
    }
    public void setClientRsaPrivate(String clientRsaPrivate) 
    {
        this.clientRsaPrivate = clientRsaPrivate;
    }

    public String getClientRsaPrivate() 
    {
        return clientRsaPrivate;
    }
    public void setClientRsaPublic(String clientRsaPublic) 
    {
        this.clientRsaPublic = clientRsaPublic;
    }

    public String getClientRsaPublic() 
    {
        return clientRsaPublic;
    }
    public void setBaseUrl(String baseUrl) 
    {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() 
    {
        return baseUrl;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("clientId", getClientId())
            .append("clientName", getClientName())
            .append("clientRsaPrivate", getClientRsaPrivate())
            .append("clientRsaPublic", getClientRsaPublic())
            .append("baseUrl", getBaseUrl())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("clientSecret", getClientSecret())
            .toString();
    }
}
