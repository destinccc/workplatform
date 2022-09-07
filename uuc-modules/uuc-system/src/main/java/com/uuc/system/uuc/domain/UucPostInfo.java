package com.uuc.system.uuc.domain;

import com.uuc.common.core.annotation.Excel;
import com.uuc.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 职位信息对象 uuc_post_info
 * 
 * @author uuc
 * @date 2022-04-01
 */
public class UucPostInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 职位ID */
    private Long id;

    /** 职位编码 */
    @Excel(name = "职位编码")
    private String postCode;

    /** 职位名称 */
    @Excel(name = "职位名称")
    private String postName;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Integer postSort;

    /** 状态(0正常1停用) */
    @Excel(name = "状态(0正常1停用)")
    private String status;

    /** 删除标识(0正常1删除) */
    private String delFlag;

    /** 版本锁 */
    @Excel(name = "版本锁")
    private Integer version;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setPostCode(String postCode) 
    {
        this.postCode = postCode;
    }

    public String getPostCode() 
    {
        return postCode;
    }
    public void setPostName(String postName) 
    {
        this.postName = postName;
    }

    public String getPostName() 
    {
        return postName;
    }
    public void setPostSort(Integer postSort) 
    {
        this.postSort = postSort;
    }

    public Integer getPostSort() 
    {
        return postSort;
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
    public void setVersion(Integer version) 
    {
        this.version = version;
    }

    public Integer getVersion() 
    {
        return version;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("postCode", getPostCode())
            .append("postName", getPostName())
            .append("postSort", getPostSort())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("version", getVersion())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
