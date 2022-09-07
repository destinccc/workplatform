package com.uuc.system.uuc.service.impl;

import java.util.List;

import com.uuc.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uuc.system.uuc.mapper.UucApplicationMapper;
import com.uuc.system.uuc.domain.UucApplication;
import com.uuc.system.uuc.service.IUucApplicationService;

/**
 * 应用信息Service业务层处理
 * 
 * @author uuc
 * @date 2022-04-01
 */
@Service
public class UucApplicationServiceImpl implements IUucApplicationService 
{
    @Autowired
    private UucApplicationMapper uucApplicationMapper;

    /**
     * 查询应用信息
     * 
     * @param id 应用信息主键
     * @return 应用信息
     */
    @Override
    public UucApplication selectUucApplicationById(Long id)
    {
        return uucApplicationMapper.selectUucApplicationById(id);
    }

    /**
     * 查询应用信息列表
     * 
     * @param uucApplication 应用信息
     * @return 应用信息
     */
    @Override
    public List<UucApplication> selectUucApplicationList(UucApplication uucApplication)
    {
        return uucApplicationMapper.selectUucApplicationList(uucApplication);
    }

    /**
     * 新增应用信息
     * 
     * @param uucApplication 应用信息
     * @return 结果
     */
    @Override
    public int insertUucApplication(UucApplication uucApplication)
    {
        uucApplication.setCreateTime(DateUtils.getNowDate());
        return uucApplicationMapper.insertUucApplication(uucApplication);
    }

    /**
     * 修改应用信息
     * 
     * @param uucApplication 应用信息
     * @return 结果
     */
    @Override
    public int updateUucApplication(UucApplication uucApplication)
    {
        uucApplication.setUpdateTime(DateUtils.getNowDate());
        return uucApplicationMapper.updateUucApplication(uucApplication);
    }

    /**
     * 批量删除应用信息
     * 
     * @param ids 需要删除的应用信息主键
     * @return 结果
     */
    @Override
    public int deleteUucApplicationByIds(Long[] ids)
    {
        return uucApplicationMapper.deleteUucApplicationByIds(ids);
    }

    /**
     * 删除应用信息信息
     * 
     * @param id 应用信息主键
     * @return 结果
     */
    @Override
    public int deleteUucApplicationById(Long id)
    {
        return uucApplicationMapper.deleteUucApplicationById(id);
    }
}
