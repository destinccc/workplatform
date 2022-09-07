package com.uuc.system.uuc.service.impl;

import java.util.List;

import com.uuc.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uuc.system.uuc.mapper.UucPostInfoMapper;
import com.uuc.system.uuc.domain.UucPostInfo;
import com.uuc.system.uuc.service.IUucPostInfoService;

/**
 * 职位信息Service业务层处理
 * 
 * @author uuc
 * @date 2022-04-01
 */
@Service
public class UucPostInfoServiceImpl implements IUucPostInfoService 
{
    @Autowired
    private UucPostInfoMapper uucPostInfoMapper;

    /**
     * 查询职位信息
     * 
     * @param id 职位信息主键
     * @return 职位信息
     */
    @Override
    public UucPostInfo selectUucPostInfoById(Long id)
    {
        return uucPostInfoMapper.selectUucPostInfoById(id);
    }

    /**
     * 查询职位信息列表
     * 
     * @param uucPostInfo 职位信息
     * @return 职位信息
     */
    @Override
    public List<UucPostInfo> selectUucPostInfoList(UucPostInfo uucPostInfo)
    {
        return uucPostInfoMapper.selectUucPostInfoList(uucPostInfo);
    }

    /**
     * 新增职位信息
     * 
     * @param uucPostInfo 职位信息
     * @return 结果
     */
    @Override
    public int insertUucPostInfo(UucPostInfo uucPostInfo)
    {
        uucPostInfo.setCreateTime(DateUtils.getNowDate());
        return uucPostInfoMapper.insertUucPostInfo(uucPostInfo);
    }

    /**
     * 修改职位信息
     * 
     * @param uucPostInfo 职位信息
     * @return 结果
     */
    @Override
    public int updateUucPostInfo(UucPostInfo uucPostInfo)
    {
        uucPostInfo.setUpdateTime(DateUtils.getNowDate());
        return uucPostInfoMapper.updateUucPostInfo(uucPostInfo);
    }

    /**
     * 批量删除职位信息
     * 
     * @param ids 需要删除的职位信息主键
     * @return 结果
     */
    @Override
    public int deleteUucPostInfoByIds(Long[] ids)
    {
        return uucPostInfoMapper.deleteUucPostInfoByIds(ids);
    }

    /**
     * 删除职位信息信息
     * 
     * @param id 职位信息主键
     * @return 结果
     */
    @Override
    public int deleteUucPostInfoById(Long id)
    {
        return uucPostInfoMapper.deleteUucPostInfoById(id);
    }
}
