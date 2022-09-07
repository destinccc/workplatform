package com.uuc.system.uuc.service;

import java.util.List;
import com.uuc.system.uuc.domain.UucCategoryInfo;
import com.uuc.system.uuc.domain.UucCategoryMenu;

/**
 * 类目信息表Service接口
 * 
 * @author uuc
 * @date 2022-04-19
 */
public interface IUucCategoryInfoService 
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
     * 批量删除类目信息表
     * 
     * @param ids 需要删除的类目信息表主键集合
     * @return 结果
     */
    public int deleteUucCategoryInfoByIds(Long[] ids);

    /**
     * 删除类目信息表信息
     * 
     * @param id 类目信息表主键
     * @return 结果
     */
    public int deleteUucCategoryInfoById(Long id);

    /**
     * 查询所有类目列表
     */
    public List<UucCategoryInfo> selectUucCategoryInfoListVo(UucCategoryInfo uucCategoryInfo);
}
