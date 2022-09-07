package com.uuc.system.uuc.service.impl;

import java.util.List;

import com.uuc.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uuc.system.uuc.mapper.UucOrganInfoMapper;
import com.uuc.system.api.model.UucOrganInfo;
import com.uuc.system.uuc.service.IUucOrganInfoService;

/**
 * 机构信息Service业务层处理
 *
 * @author uuc
 * @date 2022-04-01
 */
@Service
public class UucOrganInfoServiceImpl implements IUucOrganInfoService
{
    @Autowired
    private UucOrganInfoMapper uucOrganInfoMapper;

    /**
     * 查询机构信息
     *
     * @param id 机构信息主键
     * @return 机构信息
     */
    @Override
    public UucOrganInfo selectUucOrganInfoById(Long id)
    {
        return uucOrganInfoMapper.selectUucOrganInfoById(id);
    }

    /**
     * 查询机构信息列表
     *
     * @param uucOrganInfo 机构信息
     * @return 机构信息
     */
    @Override
    public List<UucOrganInfo> selectUucOrganInfoList(UucOrganInfo uucOrganInfo)
    {
        return uucOrganInfoMapper.selectUucOrganInfoList(uucOrganInfo);
    }

    /**
     * 新增机构信息
     *
     * @param uucOrganInfo 机构信息
     * @return 结果
     */
    @Override
    public int insertUucOrganInfo(UucOrganInfo uucOrganInfo)
    {
        uucOrganInfo.setCreateTime(DateUtils.getNowDate());
        return uucOrganInfoMapper.insertUucOrganInfo(uucOrganInfo);
    }

    /**
     * 修改机构信息
     *
     * @param uucOrganInfo 机构信息
     * @return 结果
     */
    @Override
    public int updateUucOrganInfo(UucOrganInfo uucOrganInfo)
    {
        uucOrganInfo.setUpdateTime(DateUtils.getNowDate());
        return uucOrganInfoMapper.updateUucOrganInfo(uucOrganInfo);
    }

    /**
     * 批量删除机构信息
     *
     * @param ids 需要删除的机构信息主键
     * @return 结果
     */
    @Override
    public int deleteUucOrganInfoByIds(Long[] ids)
    {
        return uucOrganInfoMapper.deleteUucOrganInfoByIds(ids);
    }

    /**
     * 删除机构信息信息
     *
     * @param id 机构信息主键
     * @return 结果
     */
    @Override
    public int deleteUucOrganInfoById(Long id)
    {
        return uucOrganInfoMapper.deleteUucOrganInfoById(id);
    }
}
