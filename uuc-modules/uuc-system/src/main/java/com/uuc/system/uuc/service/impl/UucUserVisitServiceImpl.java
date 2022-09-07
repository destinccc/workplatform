package com.uuc.system.uuc.service.impl;

import java.util.List;
import com.uuc.common.core.utils.DateUtils;
import com.uuc.system.api.model.SysMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uuc.system.uuc.mapper.UucUserVisitMapper;
import com.uuc.system.uuc.domain.UucUserVisit;
import com.uuc.system.uuc.service.IUucUserVisitService;

/**
 * 用户菜单访问记录Service业务层处理
 * 
 * @author uuc
 * @date 2022-04-19
 */
@Service
public class UucUserVisitServiceImpl implements IUucUserVisitService 
{
    @Autowired
    private UucUserVisitMapper uucUserVisitMapper;

    /**
     * 查询用户菜单访问记录
     * 
     * @param id 用户菜单访问记录主键
     * @return 用户菜单访问记录
     */
    @Override
    public UucUserVisit selectUucUserVisitById(Long id)
    {
        return uucUserVisitMapper.selectUucUserVisitById(id);
    }

    /**
     * 查询用户菜单访问记录列表
     * 
     * @param uucUserVisit 用户菜单访问记录
     * @return 用户菜单访问记录
     */
    @Override
    public List<UucUserVisit> selectUucUserVisitList(UucUserVisit uucUserVisit)
    {
        return uucUserVisitMapper.selectUucUserVisitList(uucUserVisit);
    }

    /**
     * 新增用户菜单访问记录
     * 
     * @param uucUserVisit 用户菜单访问记录
     * @return 结果
     */
    @Override
    public int insertUucUserVisit(UucUserVisit uucUserVisit)
    {
        uucUserVisit.setCreateTime(DateUtils.getNowDate());
        return uucUserVisitMapper.insertUucUserVisit(uucUserVisit);
    }

    /**
     * 修改用户菜单访问记录
     * 
     * @param uucUserVisit 用户菜单访问记录
     * @return 结果
     */
    @Override
    public int updateUucUserVisit(UucUserVisit uucUserVisit)
    {
        return uucUserVisitMapper.updateUucUserVisit(uucUserVisit);
    }

    /**
     * 批量删除用户菜单访问记录
     * 
     * @param ids 需要删除的用户菜单访问记录主键
     * @return 结果
     */
    @Override
    public int deleteUucUserVisitByIds(Long[] ids)
    {
        return uucUserVisitMapper.deleteUucUserVisitByIds(ids);
    }

    /**
     * 删除用户菜单访问记录信息
     * 
     * @param id 用户菜单访问记录主键
     * @return 结果
     */
    @Override
    public int deleteUucUserVisitById(Long id)
    {
        return uucUserVisitMapper.deleteUucUserVisitById(id);
    }

    /**
     * 查询用户菜单访问最近的10条记录
     *
     * @param id 用户菜单访问记录主键
     * @return 用户菜单访问记录
     */
    @Override
    public List<SysMenu> selectUucUserRecentVisit(Long id)
    {
        return uucUserVisitMapper.selectUucUserRecentVisit(id);
    }
}
