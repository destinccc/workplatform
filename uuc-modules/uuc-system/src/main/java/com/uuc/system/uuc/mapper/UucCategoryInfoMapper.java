package com.uuc.system.uuc.mapper;

import java.util.List;
import com.uuc.system.uuc.domain.UucCategoryInfo;
import com.uuc.system.uuc.domain.UucCategoryMenu;

/**
 * 类目信息表Mapper接口
 * 
 * @author uuc
 * @date 2022-04-19
 */
public interface UucCategoryInfoMapper 
{
    /**
     * 查询类目信息表
     * 
     * @param id 类目信息表主键
     * @return 类目信息表
     */
    public UucCategoryInfo selectUucCategoryInfoById(Long id);

    /**
     * 查询类目信息表列表
     * 
     * @param uucCategoryInfo 类目信息表
     * @return 类目信息表集合
     */
    public List<UucCategoryInfo> selectUucCategoryInfoList(UucCategoryInfo uucCategoryInfo);

    /**
     * 查询所有类目信息表
     * @return
     */
    public List<UucCategoryInfo> selectUucCategoryInfoListVo(UucCategoryInfo uucCategoryInfo);

    /**
     * 新增类目信息表
     * 
     * @param uucCategoryInfo 类目信息表
     * @return 结果
     */
    public int insertUucCategoryInfo(UucCategoryInfo uucCategoryInfo);

    /**
     * 修改类目信息表
     * 
     * @param uucCategoryInfo 类目信息表
     * @return 结果
     */
    public int updateUucCategoryInfo(UucCategoryInfo uucCategoryInfo);

    /**
     * 删除类目信息表
     * 
     * @param id 类目信息表主键
     * @return 结果
     */
    public int deleteUucCategoryInfoById(Long id);

    /**
     * 批量删除类目信息表
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUucCategoryInfoByIds(Long[] ids);

    /**
     * 批量删除类目菜单关联
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUucCategoryMenuByCategoryIds(Long[] ids);
    
    /**
     * 批量新增类目菜单关联
     * 
     * @param uucCategoryMenuList 类目菜单关联列表
     * @return 结果
     */
    public int batchUucCategoryMenu(List<UucCategoryMenu> uucCategoryMenuList);
    

    /**
     * 通过类目信息表主键删除类目菜单关联信息
     * 
     * @param id 类目信息表ID
     * @return 结果
     */
    public int deleteUucCategoryMenuByCategoryId(Long id);
}
