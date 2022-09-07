package com.uuc.system.uuc.mapper;

import java.util.List;
import com.uuc.system.uuc.domain.UucPostInfo;

/**
 * 职位信息Mapper接口
 * 
 * @author uuc
 * @date 2022-04-01
 */
public interface UucPostInfoMapper 
{
    /**
     * 查询职位信息
     * 
     * @param id 职位信息主键
     * @return 职位信息
     */
    public UucPostInfo selectUucPostInfoById(Long id);

    /**
     * 查询职位信息列表
     * 
     * @param uucPostInfo 职位信息
     * @return 职位信息集合
     */
    public List<UucPostInfo> selectUucPostInfoList(UucPostInfo uucPostInfo);

    /**
     * 新增职位信息
     * 
     * @param uucPostInfo 职位信息
     * @return 结果
     */
    public int insertUucPostInfo(UucPostInfo uucPostInfo);

    /**
     * 修改职位信息
     * 
     * @param uucPostInfo 职位信息
     * @return 结果
     */
    public int updateUucPostInfo(UucPostInfo uucPostInfo);

    /**
     * 删除职位信息
     * 
     * @param id 职位信息主键
     * @return 结果
     */
    public int deleteUucPostInfoById(Long id);

    /**
     * 批量删除职位信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUucPostInfoByIds(Long[] ids);
}
