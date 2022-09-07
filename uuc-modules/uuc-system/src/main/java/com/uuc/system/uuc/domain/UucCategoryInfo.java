package com.uuc.system.uuc.domain;

import java.util.List;

import com.uuc.common.core.annotation.Excel;
import com.uuc.common.core.web.domain.BaseEntity;
import com.uuc.system.api.domain.SysRole;
import com.uuc.system.api.model.SysMenu;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 类目信息表对象 uuc_category_info
 * 
 * @author uuc
 * @date 2022-04-19
 */
public class UucCategoryInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键Id */
    private Long id;

    /** 类目名称 */
    @Excel(name = "类目名称")
    private String categoryName;

    /** 类目顺序 */
    @Excel(name = "类目顺序")
    private Long categoryOrder;

    /** 类目标签 */
    @Excel(name = "类目标签")
    private String categoryLabel;

    /** 类目标签 */
    @Excel(name = "类目图标")
    private String icon;

    //菜单类型，0表示文字类型，1表示图文模式
    private String type;

    /** 菜单对象 */
    private List<SysMenu> sysMenus;
    /** 类目菜单关联信息 */
    private List<UucCategoryMenu> uucCategoryMenuList;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCategoryName(String categoryName) 
    {
        this.categoryName = categoryName;
    }

    public String getCategoryName() 
    {
        return categoryName;
    }
    public void setCategoryOrder(Long categoryOrder) 
    {
        this.categoryOrder = categoryOrder;
    }

    public Long getCategoryOrder() 
    {
        return categoryOrder;
    }
    public void setCategoryLabel(String categoryLabel) 
    {
        this.categoryLabel = categoryLabel;
    }

    public String getCategoryLabel() 
    {
        return categoryLabel;
    }

    public List<UucCategoryMenu> getUucCategoryMenuList()
    {
        return uucCategoryMenuList;
    }

    public void setUucCategoryMenuList(List<UucCategoryMenu> uucCategoryMenuList)
    {
        this.uucCategoryMenuList = uucCategoryMenuList;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<SysMenu> getSysMenus() {
        return sysMenus;
    }

    public void setSysMenus(List<SysMenu> sysMenus) {
        this.sysMenus = sysMenus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("categoryName", getCategoryName())
            .append("categoryOrder", getCategoryOrder())
            .append("categoryLabel", getCategoryLabel())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("remark", getRemark())
            .append("uucCategoryMenuList", getUucCategoryMenuList())
            .toString();
    }
}
