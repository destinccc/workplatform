package com.uuc.system.uuc.mapper;

import java.util.List;
import com.uuc.system.uuc.domain.UucApplication;

/**
 * 应用信息Mapper接口
 * 
 * @author uuc
 * @date 2022-04-01
 */
public interface UucApplicationMapper 
{
    /**
     * 查询应用信息
     * 
     * @param id 应用信息主键
     * @return 应用信息
     */
    public UucApplication selectUucApplicationById(Long id);

    /**
     * 查询应用信息列表
     * 
     * @param uucApplication 应用信息
     * @return 应用信息集合
     */
    public List<UucApplication> selectUucApplicationList(UucApplication uucApplication);

    /**
     * 新增应用信息
     * 
     * @param uucApplication 应用信息
     * @return 结果
     */
    public int insertUucApplication(UucApplication uucApplication);

    /**
     * 修改应用信息
     * 
     * @param uucApplication 应用信息
     * @return 结果
     */
    public int updateUucApplication(UucApplication uucApplication);

    /**
     * 删除应用信息
     * 
     * @param id 应用信息主键
     * @return 结果
     */
    public int deleteUucApplicationById(Long id);

    /**
     * 批量删除应用信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUucApplicationByIds(Long[] ids);
}
