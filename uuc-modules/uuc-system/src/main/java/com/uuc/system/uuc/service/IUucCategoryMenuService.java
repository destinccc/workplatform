package com.uuc.system.uuc.service;

import java.util.List;
import com.uuc.system.uuc.domain.UucCategoryMenu;

/**
 * 类目菜单关联Service接口
 * 
 * @author uuc
 * @date 2022-04-19
 */
public interface IUucCategoryMenuService 
{
    /**
     * 查询类目菜单关联
     * 
     * @param id 类目菜单关联主键
     * @return 类目菜单关联
     */
    public UucCategoryMenu selectUucCategoryMenuById(Long id);

    /**
     * 查询类目菜单关联列表
     * 
     * @param uucCategoryMenu 类目菜单关联
     * @return 类目菜单关联集合
     */
    public List<UucCategoryMenu> selectUucCategoryMenuList(UucCategoryMenu uucCategoryMenu);

    /**
     * 新增类目菜单关联
     * 
     * @param uucCategoryMenu 类目菜单关联
     * @return 结果
     */
    public int insertUucCategoryMenu(UucCategoryMenu uucCategoryMenu);

    /**
     * 修改类目菜单关联
     * 
     * @param uucCategoryMenu 类目菜单关联
     * @return 结果
     */
    public int updateUucCategoryMenu(UucCategoryMenu uucCategoryMenu);

    /**
     * 批量删除类目菜单关联
     * 
     * @param ids 需要删除的类目菜单关联主键集合
     * @return 结果
     */
    public int deleteUucCategoryMenuByIds(Long[] ids);

    /**
     * 删除类目菜单关联信息
     * 
     * @param id 类目菜单关联主键
     * @return 结果
     */
    public int deleteUucCategoryMenuById(Long id);
}
