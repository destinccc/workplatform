package com.uuc.system.uuc.service.impl;

import java.util.List;

import com.uuc.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uuc.system.uuc.mapper.UucRoleMapper;
import com.uuc.system.uuc.domain.UucRole;
import com.uuc.system.uuc.service.IUucRoleService;

/**
 * 角色信息Service业务层处理
 * 
 * @author uuc
 * @date 2022-04-01
 */
@Service
public class UucRoleServiceImpl implements IUucRoleService 
{
    @Autowired
    private UucRoleMapper uucRoleMapper;

    /**
     * 查询角色信息
     * 
     * @param roleId 角色信息主键
     * @return 角色信息
     */
    @Override
    public UucRole selectUucRoleByRoleId(Long roleId)
    {
        return uucRoleMapper.selectUucRoleByRoleId(roleId);
    }

    /**
     * 查询角色信息列表
     * 
     * @param uucRole 角色信息
     * @return 角色信息
     */
    @Override
    public List<UucRole> selectUucRoleList(UucRole uucRole)
    {
        return uucRoleMapper.selectUucRoleList(uucRole);
    }

    /**
     * 新增角色信息
     * 
     * @param uucRole 角色信息
     * @return 结果
     */
    @Override
    public int insertUucRole(UucRole uucRole)
    {
        uucRole.setCreateTime(DateUtils.getNowDate());
        return uucRoleMapper.insertUucRole(uucRole);
    }

    /**
     * 修改角色信息
     * 
     * @param uucRole 角色信息
     * @return 结果
     */
    @Override
    public int updateUucRole(UucRole uucRole)
    {
        uucRole.setUpdateTime(DateUtils.getNowDate());
        return uucRoleMapper.updateUucRole(uucRole);
    }

    /**
     * 批量删除角色信息
     * 
     * @param roleIds 需要删除的角色信息主键
     * @return 结果
     */
    @Override
    public int deleteUucRoleByRoleIds(Long[] roleIds)
    {
        return uucRoleMapper.deleteUucRoleByRoleIds(roleIds);
    }

    /**
     * 删除角色信息信息
     * 
     * @param roleId 角色信息主键
     * @return 结果
     */
    @Override
    public int deleteUucRoleByRoleId(Long roleId)
    {
        return uucRoleMapper.deleteUucRoleByRoleId(roleId);
    }
}
