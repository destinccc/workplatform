package com.uuc.system.uuc.mapper;

import java.util.List;
import com.uuc.system.api.domain.UucModelInfo;

/**
 * 模块管理Mapper接口
 * 
 * @author uuc
 * @date 2022-04-12
 */
public interface UucModelInfoMapper 
{
    /**
     * 查询模块管理
     * 
     * @param id 模块管理主键
     * @return 模块管理
     */
    public UucModelInfo selectUucModelInfoById(Long id);

    /**
     * 查询模块管理列表
     * 
     * @param uucModelInfo 模块管理
     * @return 模块管理集合
     */
    public List<UucModelInfo> selectUucModelInfoList(UucModelInfo uucModelInfo);

    /**
     * 新增模块管理
     * 
     * @param uucModelInfo 模块管理
     * @return 结果
     */
    public int insertUucModelInfo(UucModelInfo uucModelInfo);

    /**
     * 修改模块管理
     * 
     * @param uucModelInfo 模块管理
     * @return 结果
     */
    public int updateUucModelInfo(UucModelInfo uucModelInfo);

    /**
     * 删除模块管理
     * 
     * @param id 模块管理主键
     * @return 结果
     */
    public int deleteUucModelInfoById(Long id);

    /**
     * 批量删除模块管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUucModelInfoByIds(Long[] ids);

    public UucModelInfo selectUucModelInfoByCLientId(String clientId);
}
