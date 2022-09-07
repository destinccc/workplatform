package com.uuc.system.uuc.domain;

import com.uuc.common.core.annotation.Excel;
import com.uuc.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 用户菜单访问记录对象 uuc_user_visit
 * 
 * @author uuc
 * @date 2022-04-19
 */
public class UucUserVisit extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键Id */
    private Long id;

    /** 用户代码 */
    @Excel(name = "用户代码")
    private String userCode;

    /** 用户编号 */
    @Excel(name = "用户编号")
    private Long userId;

    /** 菜单编号 */
    @Excel(name = "菜单编号")
    private Long menuId;

    private String path;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserCode(String userCode) 
    {
        this.userCode = userCode;
    }

    public String getUserCode() 
    {
        return userCode;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setMenuId(Long menuId) 
    {
        this.menuId = menuId;
    }

    public Long getMenuId() 
    {
        return menuId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userCode", getUserCode())
            .append("userId", getUserId())
            .append("menuId", getMenuId())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .toString();
    }
}
