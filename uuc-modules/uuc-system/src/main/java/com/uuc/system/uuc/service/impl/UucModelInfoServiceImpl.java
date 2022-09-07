package com.uuc.system.uuc.service.impl;

import java.util.List;

import com.uuc.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uuc.system.uuc.mapper.UucModelInfoMapper;
import com.uuc.system.api.domain.UucModelInfo;
import com.uuc.system.uuc.service.IUucModelInfoService;

/**
 * 模块管理Service业务层处理
 * 
 * @author uuc
 * @date 2022-04-12
 */
@Service
public class UucModelInfoServiceImpl implements IUucModelInfoService 
{
    @Autowired
    private UucModelInfoMapper uucModelInfoMapper;

    /**
     * 查询模块管理
     * 
     * @param id 模块管理主键
     * @return 模块管理
     */
    @Override
    public UucModelInfo selectUucModelInfoById(Long id)
    {
        return uucModelInfoMapper.selectUucModelInfoById(id);
    }

    /**
     * 查询模块管理列表
     * 
     * @param uucModelInfo 模块管理
     * @return 模块管理
     */
    @Override
    public List<UucModelInfo> selectUucModelInfoList(UucModelInfo uucModelInfo)
    {
        return uucModelInfoMapper.selectUucModelInfoList(uucModelInfo);
    }

    /**
     * 新增模块管理
     * 
     * @param uucModelInfo 模块管理
     * @return 结果
     */
    @Override
    public int insertUucModelInfo(UucModelInfo uucModelInfo)
    {
        uucModelInfo.setCreateTime(DateUtils.getNowDate());
        return uucModelInfoMapper.insertUucModelInfo(uucModelInfo);
    }

    /**
     * 修改模块管理
     * 
     * @param uucModelInfo 模块管理
     * @return 结果
     */
    @Override
    public int updateUucModelInfo(UucModelInfo uucModelInfo)
    {
        uucModelInfo.setUpdateTime(DateUtils.getNowDate());
        return uucModelInfoMapper.updateUucModelInfo(uucModelInfo);
    }

    /**
     * 批量删除模块管理
     * 
     * @param ids 需要删除的模块管理主键
     * @return 结果
     */
    @Override
    public int deleteUucModelInfoByIds(Long[] ids)
    {
        return uucModelInfoMapper.deleteUucModelInfoByIds(ids);
    }

    /**
     * 删除模块管理信息
     * 
     * @param id 模块管理主键
     * @return 结果
     */
    @Override
    public int deleteUucModelInfoById(Long id)
    {
        return uucModelInfoMapper.deleteUucModelInfoById(id);
    }

    @Override
    public UucModelInfo selectUucModelInfoByCLientId(String clientId) {
        return uucModelInfoMapper.selectUucModelInfoByCLientId(clientId);
    }
}
