package com.uuc.system.uuc.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uuc.system.uuc.mapper.UucCategoryMenuMapper;
import com.uuc.system.uuc.domain.UucCategoryMenu;
import com.uuc.system.uuc.service.IUucCategoryMenuService;

/**
 * 类目菜单关联Service业务层处理
 * 
 * @author uuc
 * @date 2022-04-19
 */
@Service
public class UucCategoryMenuServiceImpl implements IUucCategoryMenuService 
{
    @Autowired
    private UucCategoryMenuMapper uucCategoryMenuMapper;

    /**
     * 查询类目菜单关联
     * 
     * @param id 类目菜单关联主键
     * @return 类目菜单关联
     */
    @Override
    public UucCategoryMenu selectUucCategoryMenuById(Long id)
    {
        return uucCategoryMenuMapper.selectUucCategoryMenuById(id);
    }

    /**
     * 查询类目菜单关联列表
     * 
     * @param uucCategoryMenu 类目菜单关联
     * @return 类目菜单关联
     */
    @Override
    public List<UucCategoryMenu> selectUucCategoryMenuList(UucCategoryMenu uucCategoryMenu)
    {
        return uucCategoryMenuMapper.selectUucCategoryMenuList(uucCategoryMenu);
    }

    /**
     * 新增类目菜单关联
     * 
     * @param uucCategoryMenu 类目菜单关联
     * @return 结果
     */
    @Override
    public int insertUucCategoryMenu(UucCategoryMenu uucCategoryMenu)
    {
        return uucCategoryMenuMapper.insertUucCategoryMenu(uucCategoryMenu);
    }

    /**
     * 修改类目菜单关联
     * 
     * @param uucCategoryMenu 类目菜单关联
     * @return 结果
     */
    @Override
    public int updateUucCategoryMenu(UucCategoryMenu uucCategoryMenu)
    {
        return uucCategoryMenuMapper.updateUucCategoryMenu(uucCategoryMenu);
    }

    /**
     * 批量删除类目菜单关联
     * 
     * @param ids 需要删除的类目菜单关联主键
     * @return 结果
     */
    @Override
    public int deleteUucCategoryMenuByIds(Long[] ids)
    {
        return uucCategoryMenuMapper.deleteUucCategoryMenuByIds(ids);
    }

    /**
     * 删除类目菜单关联信息
     * 
     * @param id 类目菜单关联主键
     * @return 结果
     */
    @Override
    public int deleteUucCategoryMenuById(Long id)
    {
        return uucCategoryMenuMapper.deleteUucCategoryMenuById(id);
    }
}
