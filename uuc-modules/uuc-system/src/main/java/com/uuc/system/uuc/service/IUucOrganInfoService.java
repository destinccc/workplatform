package com.uuc.system.uuc.service;

import java.util.List;
import com.uuc.system.api.model.UucOrganInfo;

/**
 * 机构信息Service接口
 *
 * @author uuc
 * @date 2022-04-01
 */
public interface IUucOrganInfoService
{
    /**
     * 查询机构信息
     *
     * @param id 机构信息主键
     * @return 机构信息
     */
    public UucOrganInfo selectUucOrganInfoById(Long id);

    /**
     * 查询机构信息列表
     *
     * @param uucOrganInfo 机构信息
     * @return 机构信息集合
     */
    public List<UucOrganInfo> selectUucOrganInfoList(UucOrganInfo uucOrganInfo);

    /**
     * 新增机构信息
     *
     * @param uucOrganInfo 机构信息
     * @return 结果
     */
    public int insertUucOrganInfo(UucOrganInfo uucOrganInfo);

    /**
     * 修改机构信息
     *
     * @param uucOrganInfo 机构信息
     * @return 结果
     */
    public int updateUucOrganInfo(UucOrganInfo uucOrganInfo);

    /**
     * 批量删除机构信息
     *
     * @param ids 需要删除的机构信息主键集合
     * @return 结果
     */
    public int deleteUucOrganInfoByIds(Long[] ids);

    /**
     * 删除机构信息信息
     *
     * @param id 机构信息主键
     * @return 结果
     */
    public int deleteUucOrganInfoById(Long id);
}
